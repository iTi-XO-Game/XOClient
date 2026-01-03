package com.mycompany.clientside.client;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientManager {

    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 8181;

    private volatile Socket socket;
    private volatile BufferedReader reader;
    private volatile PrintWriter writer;

    private volatile ExecutorService executor;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    
    private final AtomicInteger requestIdGenerator = new AtomicInteger(0);
    private final Map<Integer, ClientCallback> requestCallbacks = new ConcurrentHashMap<>();

    private static final ClientManager INSTANCE = new ClientManager();

    public static ClientManager getInstance() {
        return INSTANCE;
    }

    private ClientManager() {}

    private void connectToServer() {
        if (!isConnected.compareAndSet(false, true)) {
            return;
        }

        executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            
            executor.submit(() -> {
                receiveMessages();
            });

        } catch (IOException ex) {
            disconnect();
        }
    }

    private void receiveMessages() {
        try {
            String response;
            while ((response = reader.readLine()) != null && !Thread.currentThread().isInterrupted()) {

                int separatorIndex = response.indexOf("|");
                
                if (separatorIndex == -1) continue;
                
                int requestId ;
                try {
                    requestId = Integer.parseInt(response.substring(0, separatorIndex).trim());
                }catch (NumberFormatException e) {
                    continue;
                }
                String body = response.substring(separatorIndex + 1).trim();
                
                ClientCallback callback = requestCallbacks.remove(requestId);
                
                if (callback != null){
                    executor.submit(() -> callback.onSuccess(body));
                }
            }
        } catch (IOException ex) {
            
        } finally {
            disconnect();
        }
    }

    public void send(String messageJson, ClientCallback callback) {
        
        connectToServer();
        
        if (!isConnected.get() || writer == null) {
            callback.onFailure("Server Error. please, try again later!");
            return;
        }
        
        int requestId = requestIdGenerator.incrementAndGet();
        
        requestCallbacks.put(requestId, callback);
        
        writer.println(requestId + "|" + messageJson);
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
            if (socket != null) socket.close();
        } catch (IOException e) {}
        socket = null;

        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
        
        requestCallbacks.forEach((id, call) -> call.onFailure(""));
        requestCallbacks.clear();
    }

    public boolean isConnected() {
        return isConnected.get();
    }

}
