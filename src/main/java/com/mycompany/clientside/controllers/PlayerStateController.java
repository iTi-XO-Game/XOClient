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
import java.util.Map;
import java.util.ResourceBundle;

import com.mycompany.clientside.client.ClientManager;
import com.mycompany.clientside.client.EndPoint;
import com.mycompany.clientside.client.JsonUtils;
import com.mycompany.clientside.models.*;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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
    List<GameHistory> gameModels = new ArrayList<>();
    Map<Integer, String> opponentNames;

    ClientManager clientManager;

    @FXML
    private VBox gameRowsContainer;

    private int MY_ID;
    String opponentName;

    private int wins = 0;
    private int losses = 0;
    @FXML
    private Button navigateBackButton;
    @FXML
    private Label winCounterLabel;
    @FXML
    private Label losesCounterLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientManager = ClientManager.getInstance();

        MY_ID = UserSession.getUserId();

        getOpponentsUserName();
        gettingGamesHistory();
    }

    private void gettingGamesHistory() {
        // I Know it is not the best way ever to recall the server especially we call it past one but i was practicing this way

        GamesHistoryRequest gamesHistoryRequest = new GamesHistoryRequest(MY_ID);
        clientManager.send(gamesHistoryRequest, EndPoint.PLAYER_GAMES_HISTORY, response
                -> {

            GamesHistoryResponse gamesHistoryResponse = JsonUtils.fromJson(response, GamesHistoryResponse.class);

            gameModels = gamesHistoryResponse.getGameModels();

            wins = 0;
            losses = 0;
            for (GameHistory game : gameModels) {
                if (game.getWinnerId() != null && game.getWinnerId() == MY_ID) {
                    wins++;
                } else if (game.getWinnerId() != null) {
                    losses++;
                }
            }


            Platform.runLater(() -> {
                setWinsAndLosesLabels(wins, losses);
                displayGames();
            });

        });
    }

    public void setWinsAndLosesLabels(int wins, int losses) {

        winCounterLabel.setText(wins + "");
        losesCounterLabel.setText(losses + "");
    }

    public void displayGames() {
        gameRowsContainer.getChildren().clear();

        for (GameHistory game : gameModels) {
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

        int opponentId = game.getPlayerXId() == MY_ID ? game.getPlayerOId() : game.getPlayerXId();
        System.out.println("Player: " + MY_ID);
        Label opponentLabel = new Label(opponentNames.get(opponentId));

        long time = game.getGameDate();
        LocalDateTime dateTime = Instant
                .ofEpochMilli(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        Label dateLabel = new Label(dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a")));

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

    private void getOpponentsUserName()
    {
        OpponentNamesRequest opponentNamesRequest = new OpponentNamesRequest(getOpponentIds());

        clientManager.send(opponentNamesRequest,EndPoint.OPPONENT_NAMES, responseJson -> {

            OpponentNamesResponse res = JsonUtils.fromJson(responseJson, OpponentNamesResponse.class);
            opponentNames = res.getOpponentsMap();
        });

    }

    private List<Integer> getOpponentIds()
    {
        List<Integer> temp = new ArrayList<>();

        for (GameHistory game : gameModels)
        {
            int opponentId = game.getPlayerXId() == MY_ID ? game.getPlayerOId() : game.getPlayerXId();
            temp.add(opponentId);
        }

        return  temp;
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
