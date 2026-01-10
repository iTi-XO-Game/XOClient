/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.client;

/**
 *
 * @author Hossam
 */
public enum EndPoint {
    
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    LOGOUT("LOGOUT"),
    
    LOBBY("LOBBY"), // 1 listener
    CHALLENGE("CHALLENGE"), // 1 listener
    GAME("GAME");
    
    private final String code;
    
    EndPoint(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public static EndPoint fromString(String actionName) {
        for (EndPoint endPoint : values()) {
            if (endPoint.code.equals(actionName)) return endPoint;
        }
        throw new IllegalArgumentException("Unknown endpoint: " + actionName);
    }
}
