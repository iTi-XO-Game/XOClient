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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mohamed
 */
public class PlayerRequestScreenController implements Initializable {

    @FXML
    private Label challengerName;
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

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            root.getScene().getWindow().setX(e.getScreenX() - xOffset);
            root.getScene().getWindow().setY(e.getScreenY() - yOffset);
        });
        Rectangle clip = new Rectangle();
        clip.setArcWidth(20);  // radius in pixels
        clip.setArcHeight(20);
        clip.widthProperty().bind(root.widthProperty());
        clip.heightProperty().bind(root.heightProperty());
        root.setClip(clip);
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
        closeDialog();
    }

    @FXML
    private void acceptChallenge(ActionEvent event) {
        sendAccept.accept(challenge);
        closeDialog();
    }

    @FXML
    private void cancelChallenge(ActionEvent event) {
        sendCancel.accept(challenge);
        closeDialog();
    }

    public void setChallengeData(Challenge challenge, Player player) {
        this.challenge = challenge;
        challengerName.setText(player.getUsername());
    }
    
    private void closeDialog() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();  
    }
}
