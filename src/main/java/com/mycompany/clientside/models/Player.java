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
    private final String username;
    private final int wins;
    private final int draws;
    private final int losses;

    public int getDraws() {
        return draws;
    }
    private final boolean isInGame;
    public int getId() {
        return id;
    }

    public Player(int id, String username, int wins, int losses, boolean inGame, int draws) {
        this.id = id;
        this.username = username;
        this.wins = wins;
        this.losses = losses;
        this.isInGame = inGame;
        this.draws = draws;
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public boolean isIsInGame(){
        return this.isInGame;
    }
}
