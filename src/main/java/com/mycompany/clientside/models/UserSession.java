/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.models;

/**
 *
 * @author Depogramming
 */
public class UserSession {

    private static volatile Integer userId;
    private static volatile String username;
    public static volatile Player currentPlayer;

    public static Integer getUserId() {
        return userId;
    }

    public static void setUserId(Integer userId) {
        UserSession.userId = userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(Player currentPlayer) {
        UserSession.currentPlayer = currentPlayer;
    }
}
