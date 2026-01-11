/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.models;

/**
 *
 * @author Depogramming
 */
public class AuthResponse {

    private StatusCode statusCode;
    private String errorMessage;
    private Integer id;
    private String username;

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

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public AuthResponse(StatusCode statusCode, String errorMessage, Integer id, String username) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.id = id;
        this.username = username;
    }

    public AuthResponse() {
    }

}
