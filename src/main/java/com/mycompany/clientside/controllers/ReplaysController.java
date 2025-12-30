/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.geometry.Pos;
import javafx.scene.shape.SVGPath;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class ReplaysController implements Initializable {


    @FXML
    private Button playVideoBtn;
    @FXML
    private Button delBtn;
    @FXML
    private VBox gamesContainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         addGame("Apr 24, 2024, 02:15 PM");
         addGame("Apr 23, 2024, 11:51 AM");
         addGame("Apr 22, 2024, 09:40 PM");
    } 
    
    
private void addGame(String dateTime) {
    HBox row = new HBox(10);
    row.setAlignment(Pos.CENTER_LEFT);

    Label date = new Label(dateTime);
    date.getStyleClass().add("small-text");

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    SVGPath playIcon = new SVGPath();
    playIcon.setContent("M8 5v14l11-7z");
    playIcon.setFill(Color.WHITE);

    Button playBtn = new Button("Play");
    playBtn.setGraphic(playIcon);
    playBtn.getStyleClass().add("primary-btn");

    SVGPath deleteIcon = new SVGPath();
    deleteIcon.setContent("M3 6h18M8 6V4h8v2M6 6l1 14h10l1-14");
    deleteIcon.setFill(Color.WHITE);

    Button deleteBtn = new Button("Delete");
    deleteBtn.setGraphic(deleteIcon);
    deleteBtn.getStyleClass().add("login-btn");

    deleteBtn.setOnAction(e -> {
        int index = gamesContainer.getChildren().indexOf(row);
        if (index > 0) {
            if (gamesContainer.getChildren().get(index - 1) instanceof Separator) {
                gamesContainer.getChildren().remove(index - 1);
            }
        }
        gamesContainer.getChildren().remove(row);
    });

    row.getChildren().addAll(date, spacer, playBtn, deleteBtn);

    if (!gamesContainer.getChildren().isEmpty()) {
        gamesContainer.getChildren().add(new Separator());
    }

    gamesContainer.getChildren().add(row);
}


    @FXML
    private void handelPlayVideo(ActionEvent event) {
    }

    @FXML
    private void handelDelVideo(ActionEvent event) {
    }

    
    @FXML
    private void navigateBack(ActionEvent event) {
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo make an alert!
        }
    }
}
