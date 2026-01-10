/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.ClientManager;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

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
        // TODO
    }

    @FXML
    private void onLogOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText("Log out");
        alert.setContentText("Are you sure you want to log out from the game?");

        ButtonType confirmBtn = new ButtonType("Log out");
        ButtonType cancelBtn = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(confirmBtn, cancelBtn);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmBtn) {
            ClientManager.getInstance().sendLogout();
            try {
                App.setRoot(Screens.LOGIN_SCREEN);
            } catch (IOException ex) {
                // todo make an alert!
            }
        }
    }

    @FXML
    private void onSingleClick(ActionEvent event) {
        try {
            App.setRoot(Screens.DIFFICULTY_SCREEN);
        } catch (IOException ex) {
            // todo make an alert!
        }
    }

    @FXML
    private void onLocalModeClick(ActionEvent event) {
        try {
            App.setRoot(Screens.GAME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
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
            // todo make an alert!
        }
    }

    @FXML
    private void onReplayClick(ActionEvent event) {
        try {
            App.setRoot(Screens.REPLAYS_SCREEN);
        } catch (IOException ex) {
            // todo make an alert!
        }
    }

}
