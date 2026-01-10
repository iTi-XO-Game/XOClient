/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.models.Challenge;
import com.mycompany.clientside.models.Player;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mohamed
 */
public class PlayerRequestScreenController implements Initializable {

    @FXML
    private ImageView imageIcon;
    @FXML
    private Label challengerName;
    @FXML
    private Label challengerWinRate;
    @FXML
    private Label timerDown;
    @FXML
    private Label challengerStatus;

    private Challenge challenge;
    private Consumer<Challenge> sendAccept;
    private Consumer<Challenge> sendDecline;
    private Consumer<Challenge> sendCancel;
    @FXML
    private HBox receiveButtons;
    @FXML
    private Button cancelButton;
    @FXML
    private VBox parentVBox;
    @FXML
    private StackPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setAcceptChallenge(Consumer<Challenge> sendAccept) {
        parentVBox.getChildren().remove(cancelButton);
        this.sendAccept = sendAccept;
    }

    public void setDeclineChallenge(Consumer<Challenge> sendDecline) {
        this.sendDecline = sendDecline;
    }

    public void setCancelChallenge(Consumer<Challenge> sendCancel) {
        parentVBox.getChildren().remove(receiveButtons);
        this.sendCancel = sendCancel;
    }

    @FXML
    private void declineChallenge(ActionEvent event) {
        sendDecline.accept(challenge);
    }

    @FXML
    private void acceptChallenge(ActionEvent event) {
        sendAccept.accept(challenge);
    }

    @FXML
    private void cancelChallenge(ActionEvent event) {
        sendCancel.accept(challenge);
    }

    public void setChallengeData(Challenge challenge, Player player) {
        this.challenge = challenge;
        
        int allGames = player.getWins() + player.getLosses() + player.getDraws();
        float winRate = player.getWins() * 100f / (float) allGames;
        
        challengerName.setText(player.getUsername());
        challengerWinRate.setText("Win Rate: " + String.format("%.1f", winRate) + "%");
    }
    
    public void closeDialog() {
        Platform.runLater(() -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });   
    }
}
