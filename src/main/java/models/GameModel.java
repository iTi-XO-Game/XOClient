/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;

/**
 *
 * @author Depogramming
 */
public class GameModel {
    int gameId;
    int playerOneId;
    int playerTwoId;
    int winnerId; //should be 0 if it's draw
    LocalDateTime startTime;
    LocalDateTime endTime;
    
}