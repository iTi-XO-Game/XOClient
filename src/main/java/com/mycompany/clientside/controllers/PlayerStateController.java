/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.clientside.client.ClientManager;
import com.mycompany.clientside.client.EndPoint;
import com.mycompany.clientside.client.JsonUtils;
import com.mycompany.clientside.models.GameHistory;
import com.mycompany.clientside.models.GamesHistoryRequest;
import com.mycompany.clientside.models.GamesHistoryResponse;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import com.mycompany.clientside.models.GameModel;
import java.io.IOException;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    List<GameHistory> gameModels2 = new ArrayList<>();
    ClientManager clientManager ;

    @FXML
    private VBox gameRowsContainer;

    //thinking on getting that value from the constructor
    private int MY_ID ;
    @FXML
    private Button navigateBackButton;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        clientManager = ClientManager.getInstance();

//        clientManager.send(null,EndPoint.PLAYER_ID,responseJson -> {
//            MY_ID = JsonUtils.fromJson(responseJson,int.class);
//        });

        MY_ID = 3;

        gettingGamesHistory();
    }

    //this function should fill the data from the database
    private void gettingGamesHistory() {


        GamesHistoryRequest gamesHistoryRequest = new GamesHistoryRequest(MY_ID);
        clientManager.send(gamesHistoryRequest, EndPoint.PLAYER_GAMES_HISTORY, response ->
        {

            GamesHistoryResponse gamesHistoryResponse = JsonUtils.fromJson(response, GamesHistoryResponse.class);

            gameModels2 = gamesHistoryResponse.getGameModels();

            Platform.runLater(this::displayGames);

        });

}

    public void displayGames() {
        gameRowsContainer.getChildren().clear();

        for (GameHistory game : gameModels2) {
            HBox row = createGameRow(game);
            gameRowsContainer.getChildren().add(row);
        }
    }

    private HBox createGameRow(GameHistory game) {
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

        if (game.getWinnerId() == null) {
            setupStatusLabel(resultLabel, "Draw", "#F1F5F9", "#64748B");

        } else if (game.getWinnerId() == MY_ID) {
            setupStatusLabel(resultLabel, "Victory", "#E6F9ED", "#2ECC71");

        } else {
            setupStatusLabel(resultLabel, "Defeat", "#FEE2E2", "#EF4444");
        }


        Label opponentLabel = new Label("Player " + (game.getPlayerXId() == MY_ID ? game.getPlayerOId() : game.getPlayerXId()));
       long time = game.getGameDate();
        LocalDateTime dateTime = Instant
                .ofEpochMilli(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        Label dateLabel = new Label(dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        resultContainer.prefWidthProperty().bind(row.widthProperty().divide(4));
        opponentLabel.prefWidthProperty().bind(row.widthProperty().divide(4));
        dateLabel.prefWidthProperty().bind(row.widthProperty().divide(4));

        opponentLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(opponentLabel, Priority.ALWAYS);
        dateLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(dateLabel, Priority.ALWAYS);

        row.getChildren().addAll(resultContainer, opponentLabel, dateLabel);


        return row;
    }

    private void setupStatusLabel(Label lbl, String text, String bg, String textFill) {
        lbl.setText(text);
        lbl.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + textFill + "; "
                + "-fx-background-radius: 10; -fx-padding: 5 10 5 10; -fx-font-weight: bold;");
    }

    @FXML
    private void navigateBack(ActionEvent event) {
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }

}
