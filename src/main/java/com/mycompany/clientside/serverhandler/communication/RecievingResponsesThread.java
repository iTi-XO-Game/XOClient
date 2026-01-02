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
        try {
            reader = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to init receiver", ex);
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
        String incomingData;
        try {
            while ((incomingData = reader.readUTF()) != null) {
                final String finalData = incomingData;
                Platform.runLater(() -> {
                    request.set(finalData);
                });
                System.out.println("Server: " + incomingData);
            }
        } catch (IOException ex) {
            System.out.println("there's no active server!");//didn't handled yet
            ex.printStackTrace();
        }
    }
}
