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

public class LoginResponse {
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

    public int getId() {
        return id;
    }

    public static void setId(int id) {
        LoginResponse.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        LoginResponse.userName = userName;
    }

    public LoginResponse(StatusCode statusCode, String errorMessage, Integer id, String userName) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        LoginResponse.id = id;
        LoginResponse.userName = userName;
    }

    public LoginResponse() {
    }

}
