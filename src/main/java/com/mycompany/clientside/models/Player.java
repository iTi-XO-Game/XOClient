/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.models;

/**
 *
 * @author Depogramming
 */
public class Player {
    private final int id;
    private final String userName;
    private final int wins;
    private final int losses;

    public int getId() {
        return id;
    }

    public Player(int id, String userName, int wins, int losses) {
        this.id = id;
        this.userName = userName;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUserName() {
        return userName;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
    
}
