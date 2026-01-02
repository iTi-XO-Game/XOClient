/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.serverhandler.communication;

import java.io.DataInputStream;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Depogramming
 */
public class RecievingResponsesThread extends SocketInitializer implements Runnable {

    private static RecievingResponsesThread instance;
    private DataInputStream reader;
    StringProperty request;

    private RecievingResponsesThread() {
        request = new SimpleStringProperty();

        if (socket == null) {
            System.out.println("the server is not available, but the application won't crash... yaaay");
            return;
        }
        try {
            reader = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Server is not available at startup.");
        }
    }

    public StringProperty requestProperty() {
        return request;
    }

    public static RecievingResponsesThread getInstance() {
        if (instance == null) {
            instance = new RecievingResponsesThread();
        }
        return instance;
    }

    @Override
    public void run() {
        if (reader == null) {
            System.out.println("Receiver thread stopped: no server connection.");
            return;
        }
        String incomingData;
        try {
            while ((incomingData = reader.readUTF()) != null) {
                final String finalData = incomingData;
                Platform.runLater(() -> {
                    request.set(finalData);
                });
            }
        } catch (IOException ex) {
            System.out.println("there's no active server!");//didn't handled yet
            ex.printStackTrace();
        }
    }
}
