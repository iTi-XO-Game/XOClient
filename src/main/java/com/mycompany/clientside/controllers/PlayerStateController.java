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
import javafx.scene.layout.Region;
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
    private int draws = 0;
    private int losses = 0;
    @FXML
    private Button navigateBackButton;
    @FXML
    private Label winCounterLabel;
    @FXML
    private Label drawCounterLabel;
    @FXML
    private Label losesCounterLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientManager = ClientManager.getInstance();

        MY_ID = UserSession.getUserId();
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
            draws = 0;
            losses = 0;
            for (GameHistory game : gameModels) {
                if (game.getWinnerId() != null && game.getWinnerId() == MY_ID) {
                    wins++;
                } else if (game.getWinnerId() != null) {
                    losses++;
                } else {
                    draws++;
                }
            }

            Platform.runLater(() -> setWinsAndLosesLabels(wins, draws, losses));
            new Thread(this::getOpponentsUserName).start();

        });
    }

    public void setWinsAndLosesLabels(int wins, int draws, int losses) {
        winCounterLabel.setText(wins + "");
        drawCounterLabel.setText(draws + "");
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
        row.setAlignment(Pos.CENTER);
        row.getStyleClass().add("gameRow");

        Label resultLabel = new Label();

        HBox resultContainer = new HBox(resultLabel);

        if (game.getWinnerId() == null) {
            resultLabel.setText("Draw");
            resultLabel.getStyleClass().add("drawResultLabel");
            resultContainer.getStyleClass().add("drawResultContainer");
        } else if (game.getWinnerId() == MY_ID) {
            resultLabel.setText("Win");
            resultLabel.getStyleClass().add("winResultLabel");
            resultContainer.getStyleClass().add("winResultContainer");
        } else {
            resultLabel.setText("Defeat");
            resultLabel.getStyleClass().add("lossResultLabel");
            resultContainer.getStyleClass().add("lossResultContainer");
        }

        int opponentId = game.getPlayerXId() == MY_ID ? game.getPlayerOId() : game.getPlayerXId();

        Label opponentLabel = new Label(opponentNames.get(opponentId));
        opponentLabel.getStyleClass().add("opponentNameLabel");

        long time = game.getGameDate();
        LocalDateTime dateTime = Instant
                .ofEpochMilli(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        Label dateLabel = new Label(dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a")));
        dateLabel.getStyleClass().add("dateLabel");

        Region r1 = new Region();
        HBox.setHgrow(r1, Priority.ALWAYS);
        Region r2 = new Region();
        HBox.setHgrow(r2, Priority.ALWAYS);
        Region r3 = new Region();
        HBox.setHgrow(r3, Priority.ALWAYS);

        row.getChildren().addAll(resultContainer, r1, opponentLabel, r2, dateLabel, r3);

        return row;
    }

    private void getOpponentsUserName() {
        OpponentNamesRequest opponentNamesRequest
                = new OpponentNamesRequest(getOpponentIds());

        clientManager.send(opponentNamesRequest, EndPoint.OPPONENT_NAMES, responseJson -> {

            OpponentNamesResponse res
                    = JsonUtils.fromJson(responseJson, OpponentNamesResponse.class);
            opponentNames = res.getOpponentsMap();

            // NOW the data exists → update UI
            Platform.runLater(this::displayGames);
        });
    }

    private List<Integer> getOpponentIds() {
        List<Integer> temp = new ArrayList<>();

        for (GameHistory game : gameModels) {
            int opponentId = game.getPlayerXId() == MY_ID ? game.getPlayerOId() : game.getPlayerXId();
            temp.add(opponentId);
        }
        return temp;
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
