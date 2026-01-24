package com.mycompany.clientside.client;

import com.mycompany.clientside.models.UserSession;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;

public class ClientManager {

    public static String IP_ADDRESS = "127.0.0.1";
    public static int PORT = 8181;

    private volatile Socket socket;
    private volatile BufferedReader reader;
    private volatile PrintWriter writer;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    private final AtomicInteger requestIdGenerator = new AtomicInteger(0);

    private enum CallbackType {
        REQUEST,
        LISTENER
    }

    private final Map<Integer, ClientCallback> requestCallbacks = new ConcurrentHashMap<>();
    private final Map<String, ClientCallback> listenerCallbacks = new ConcurrentHashMap<>();

    private static final ClientManager INSTANCE = new ClientManager();

    public static ClientManager getInstance() {
        return INSTANCE;
    }

    private ClientManager() {
    }

    private void connectToServer() {
        if (!isConnected.compareAndSet(false, true)) {
            return;
        }

        try {
            socket = new Socket(IP_ADDRESS, PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            executor.submit(this::receiveMessages);

        } catch (IOException ex) {
            disconnect();
        }
    }

    private void receiveMessages() {
        try {
            String response;
            while ((response = reader.readLine()) != null && !Thread.currentThread().isInterrupted()) {


                int firstSplit = response.indexOf("|");
                int secondSplit = response.indexOf("|", firstSplit + 1);

                if (firstSplit == -1 || secondSplit == -1) {
                    continue;
                }

                String endPointString = response.substring(0, firstSplit);

                String callbackId = response.substring(firstSplit + 1, secondSplit);

                String responseJson = response.substring(secondSplit + 1);

                int requestId;
                try {
                    requestId = Integer.parseInt(callbackId);
                } catch (NumberFormatException e) {
                    continue;
                }

                ClientCallback requestCallback = requestCallbacks.remove(requestId);
                ClientCallback listenerCallback = listenerCallbacks.get(endPointString);

                if (responseJson.isBlank()) {
                    continue;
                }

                if (requestCallback != null) {
                    executor.submit(() -> requestCallback.onSuccess(responseJson));
                } else if (listenerCallback != null) {
                    executor.submit(() -> listenerCallback.onSuccess(responseJson));
                }
            }
        } catch (IOException ex) {

        } finally {
            disconnect();
        }
    }

    public <T> void send(T request, EndPoint endPoint, ClientCallback callback) {
        executor.submit(() -> {
            forwardToServer(request, endPoint, callback, CallbackType.REQUEST);
        });
    }

    public <T> void sendListener(T request, EndPoint endPoint, ClientCallback callback) {
        executor.submit(() -> {
            forwardToServer(request, endPoint, callback, CallbackType.LISTENER);
        });
    }

    private <T> void forwardToServer(T request, EndPoint endPoint, ClientCallback callback, CallbackType callbackType) {

        connectToServer();

        if (!isConnected.get() || writer == null) {
            if (endPoint != EndPoint.LOGOUT) {
                showServerDisconnectedAlert();
            }
            return;
        }

        String messageJson = JsonUtils.toJson(request);
        int requestId = requestIdGenerator.incrementAndGet();

        String endPointCode = endPoint.getCode();

        switch (callbackType) {
            case CallbackType.LISTENER -> {
                listenerCallbacks.put(endPointCode, callback);
                requestId = -1;
            }
            case CallbackType.REQUEST ->
                requestCallbacks.put(requestId, callback);
        }

        String req = endPointCode + "|" + requestId + "|" + messageJson;

        writer.println(req);
    }

    public void disconnect() {
        if (!isConnected.compareAndSet(true, false)) {
            return;
        }
        if (writer != null) {
            writer.close();
        }
        writer = null;
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
            }
        }
        reader = null;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
        socket = null;

        requestCallbacks.forEach((id, call) -> call.onFailure());
        requestCallbacks.clear();
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void removeListener(String endPointCode) {
        listenerCallbacks.remove(endPointCode);
    }

    private void showServerDisconnectedAlert() {
        Platform.runLater(() -> {
            AlertBuilder alertBuilder = new AlertBuilder();
            alertBuilder
                    .setTitle("An Error Occurred")
                    .setSubTitle("Unable to connect to the server. please, try again later!")
                    .setAcceptText("Dismiss")
                    .show();
        });
    }

    public void sendLogout() {
        if (writer != null && UserSession.currentPlayer != null) {
            writer.println(EndPoint.LOGOUT.getCode() + "|" + -1 + "|" + UserSession.currentPlayer.getId());
        }
        UserSession.currentPlayer = null;
        listenerCallbacks.clear();
        requestCallbacks.clear();
    }
}
