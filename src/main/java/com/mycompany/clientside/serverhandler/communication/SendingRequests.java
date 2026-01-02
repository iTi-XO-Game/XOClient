/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.serverhandler.communication;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Depogramming
 */
public class SendingRequests extends SocketInitializer {

    DataOutputStream dos;

    public SendingRequests(String requestjson) {
        System.out.println("we are on the sending request");
        if (socket == null) {
            System.out.println("saddly, the server is down..");
            System.out.println("just go next");

            return;
        }
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            //this will be the serialized message
            dos.writeUTF(requestjson);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
