/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.models;

/**
 *
 * @author lenovo
 */
public class Move {
    private final String Player;
    private final int ColIndex;
    private final int rowIndex;
    
    public Move(String player, int rowIndex, int colIndex) {
        this.Player = player;
        this.rowIndex = rowIndex;
        this.ColIndex = colIndex;
    }
    public String getPlayer() { return Player; }
    public int getRow() { return rowIndex; }
    public int getCol() { return ColIndex; }
}
