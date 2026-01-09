/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.models;

import java.util.List;

/**
 *
 * @author Dell
 */
public class GameRecord {

    private String gameName;
    private String difficulty;
    private List<Move> moves;

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public String getGameName() {
        return gameName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public GameRecord(String gameName, String difficulty, List<Move> moves) {
        this.gameName = gameName;
        this.difficulty = difficulty;
        this.moves = moves;
    }

}
