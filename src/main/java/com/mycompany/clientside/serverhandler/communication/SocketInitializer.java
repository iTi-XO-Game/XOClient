/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.serverhandler.communication;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Depogramming
 */
public class SocketInitializer {

    public Socket socket;

    public SocketInitializer() {
        try {
            socket = new Socket("127.0.0.1", 5000);
        } catch (IOException ex) {
            System.out.println("السيرفر المطلوب مغلق او غير متاح");
        }

    }

}
