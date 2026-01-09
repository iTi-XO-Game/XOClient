/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.models;

import com.mycompany.clientside.client.StatusCode;

/**
 *
 * @author Depogramming
 */

public class AuthResponse {
    private StatusCode statusCode;
    private String errorMessage;
    private static Integer id;
    private static String userName;

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        AuthResponse.id = id;
    }

    public static String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        AuthResponse.userName = userName;
    }

    public AuthResponse(StatusCode statusCode, String errorMessage, Integer id, String userName) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        AuthResponse.id = id;
        AuthResponse.userName = userName;
    }

    public AuthResponse() {
    }

}
