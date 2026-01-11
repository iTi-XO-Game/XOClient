/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.client.ChallengeManager;
import com.mycompany.clientside.models.Player;
import com.mycompany.clientside.models.UserSession;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Hossam
 */
public class OnlinePlayerCardController implements Initializable {

    @FXML
    private VBox cardRoot;
    @FXML
    private Label nameLabel;
    @FXML
    private Label winsLabel;
    @FXML
    private Label losesLabel;
    @FXML
    private Button challengeButton;

    private Player player;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (player.getId() == UserSession.currentPlayer.getId()) {
            challengeButton.setDisable(true);
            challengeButton.setVisible(false);
        }
        
        String username = player.getUsername();
        if (player.getId() == UserSession.currentPlayer.getId()) {
            username += " (You)";
        }
        
        nameLabel.setText(username);
        winsLabel.setText("W:" + player.getWins());
        losesLabel.setText("L:" + player.getLosses());
    }

    @FXML
    private void onChallengeClick(ActionEvent event) {
        challengeButton.setDisable(true);
        challengeButton.setText("Sending");
        ChallengeManager.getInstance().sendChallenge(player, () -> {
            Platform.runLater(() -> {
                challengeButton.setDisable(false);
                challengeButton.setText("Challenge");
            });
        });
    }
    
    public Parent getRoot() {
        return cardRoot;
    }
}
