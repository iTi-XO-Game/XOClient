/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Hossam
 */
public class AlertController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label subTitle;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    @FXML
    private StackPane root;

    public Runnable accept;
    public Runnable cancel;

    private double xOffset = 0;
    private double yOffset = 0;

    public boolean actionWasTaken = false;

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
    }

    public void setOnAccept(Runnable accept) {
        this.accept = accept;
    }

    public void setOnCancel(Runnable cancel) {
        this.cancel = cancel;
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setSubTitle(String text) {
        subTitle.setText(text);
    }

    public void setAcceptText(String text) {
        acceptButton.setText(text);
    }

    public void setCancelText(String text) {
        cancelButton.setText(text);
    }

    public void seDangerButton() {
        acceptButton.getStyleClass().remove("acceptButton");
        acceptButton.getStyleClass().add("dangerButton");
    }
    public void hideCancelButton() {
        acceptButton.getStyleClass().remove("acceptButton");
        acceptButton.getStyleClass().add("cancelButton");
        cancelButton.setVisible(false);
        cancelButton.setManaged(false);
    }

    @FXML
    private void onAccept(ActionEvent event) {
        accept.run();
        actionWasTaken = true;
        closeDialog();
    }

    @FXML
    private void onCancel(ActionEvent event) {
        cancel.run();
        actionWasTaken = true;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
