/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import com.mycompany.clientside.models.GameModel;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Depogramming
 */
public class PlayerStateController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ArrayList<GameModel> gameModels;
    @FXML
    private VBox gameRowsContainer;

    //thinking on getting that value from the constructor
    private final int MY_ID = 100;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gameModels = new ArrayList<>();
        gettingGamesHistory();
        displayGames(gameModels);
    }

    //this function should fill the data from the database
    private void gettingGamesHistory() {
        LocalDateTime matchStart = LocalDateTime.of(2023, 10, 24, 14, 30, 0);
        LocalDateTime matchEnd = LocalDateTime.of(2023, 10, 24, 14, 34, 21);
        gameModels.add(new GameModel(1, 100, 200, 100, matchStart, matchEnd));
        gameModels.add(new GameModel(2, 100, 200, -1, matchStart, matchEnd));
        gameModels.add(new GameModel(3, 100, 200, 200, matchStart, matchEnd));
        gameModels.add(new GameModel(3, 100, 200, 200, matchStart, matchEnd));
        gameModels.add(new GameModel(3, 100, 200, 200, matchStart, matchEnd));
        gameModels.add(new GameModel(3, 100, 200, 200, matchStart, matchEnd));
    }

    public void displayGames(ArrayList<GameModel> gameModels) {
        gameRowsContainer.getChildren().clear();

        for (GameModel game : gameModels) {
            HBox row = createGameRow(game);
            gameRowsContainer.getChildren().add(row);
        }
    }

    private HBox createGameRow(GameModel game) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefHeight(60);
        row.setPadding(new Insets(0, 20, 0, 20));
        row.setStyle("-fx-border-color: #F1F5F9; -fx-border-width: 0 0 1 0;");
        // 1. Result Logic (The Container)
        Label resultLabel = new Label();
        HBox resultContainer = new HBox(resultLabel);
        resultContainer.setAlignment(Pos.CENTER_LEFT);

        resultContainer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(resultContainer, Priority.ALWAYS);

        switch (game.getWinnerId()) {
            case -1:
                setupStatusLabel(resultLabel, "Draw", "#F1F5F9", "#64748B");
                break;
            case MY_ID:
                setupStatusLabel(resultLabel, "Victory", "#E6F9ED", "#2ECC71");
                break;
            default:
                setupStatusLabel(resultLabel, "Defeat", "#FEE2E2", "#EF4444");
                break;
        }

        Label opponentLabel = new Label("Player " + (game.getPlayerOneId() == MY_ID ? game.getPlayerTwoId() : game.getPlayerOneId()));
        Label dateLabel = new Label(game.getStartTime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        Label durationLabel = new Label(game.getDuration());
        resultContainer.prefWidthProperty().bind(row.widthProperty().divide(4));
        opponentLabel.prefWidthProperty().bind(row.widthProperty().divide(4));
        dateLabel.prefWidthProperty().bind(row.widthProperty().divide(4));
        durationLabel.prefWidthProperty().bind(row.widthProperty().divide(4));

        opponentLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(opponentLabel, Priority.ALWAYS);
        dateLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(dateLabel, Priority.ALWAYS);
        durationLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(durationLabel, Priority.ALWAYS);

        row.getChildren().addAll(resultContainer, opponentLabel, dateLabel, durationLabel);

        return row;
    }

    private void setupStatusLabel(Label lbl, String text, String bg, String textFill) {
        lbl.setText(text);
        lbl.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + textFill + "; "
                + "-fx-background-radius: 10; -fx-padding: 5 10 5 10; -fx-font-weight: bold;");
    }

}
