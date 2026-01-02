/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.serverhandler.communication;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Depogramming
 */
public class RecievingResponsesThread extends SocketInitializer implements Runnable {

    private static RecievingResponsesThread instance;
    private DataInputStream reader;
    String request;

    private RecievingResponsesThread() {
        try {
            reader = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to init receiver", ex);
        }
    }

    public static RecievingResponsesThread getInstance() {
        if (instance == null) {
            instance = new RecievingResponsesThread();
        }
        return instance;
    }

    @Override
    public void run() {

        try {
            while ((request = reader.readUTF()) != null) {
                //should desrialize that request and do some event based on that 
                System.out.println("Server: " + request);
            }
        } catch (IOException ex) {
            System.out.println("there's no active server!");//didn't handled yet
            ex.printStackTrace();
        }
    }
}
