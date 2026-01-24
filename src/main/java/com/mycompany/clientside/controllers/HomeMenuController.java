/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.AlertBuilder;
import com.mycompany.clientside.client.ClientManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Hossam
 */
public class HomeMenuController implements Initializable {

    @FXML
    private Button logOutButton;
    @FXML
    private Button singleModeButton;
    @FXML
    private Button localModeButton;
    @FXML
    private Button onlineModeButton;
    @FXML
    private Button scoreButton;
    @FXML
    private Button replayButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void onLogOut(ActionEvent event) {
        AlertBuilder alertBuilder = new AlertBuilder();
        alertBuilder
                .setTitle("Log out")
                .setSubTitle("Are you sure you want to log out from the game?")
                .setAcceptText("Log out")
                .setCancelText("Cancel")
                .setCancellable(true)
                .setDanger(true)
                .setOnAccept(() -> {
                    ClientManager.getInstance().sendLogout();
                    try {
                        App.setRoot(Screens.LOGIN_SCREEN);
                    } catch (IOException ex) {
                        System.err.println("Cannot go to login screen");
                    }
                })
                .setOnCancel(() -> {})
                .show();
    }

    @FXML
    private void onSingleClick(ActionEvent event) {
        try {
            App.setRoot(Screens.DIFFICULTY_SCREEN);
        } catch (IOException ex) {
            System.err.println("Cannot go to difficulty screen");
        }
    }

    @FXML
    private void onLocalModeClick(ActionEvent event) {
        try {
            App.setRoot(Screens.GAME_SCREEN);
        } catch (IOException ex) {
            System.err.println("Cannot go to game screen");
        }
    }

    @FXML
    private void onOnlineModeClick(ActionEvent event) {
        try {
            App.setRoot(Screens.ONLINE_MULTIPLAYER_SCREEN);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }

    @FXML
    private void onScoreClick(ActionEvent event) {
        try {
            App.setRoot(Screens.STATS_SCREEN);
        } catch (IOException ex) {
            System.err.println("Cannot go to score screen");
        }
    }

    @FXML
    private void onReplayClick(ActionEvent event) {
        try {
            App.setRoot(Screens.REPLAYS_SCREEN);
        } catch (IOException ex) {
            System.err.println("Cannot go to replays screen");
        }
    }

}
