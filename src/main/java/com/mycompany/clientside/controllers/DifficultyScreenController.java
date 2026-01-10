/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Mohamed
 */
public class DifficultyScreenController implements Initializable {

    @FXML
    private ToggleGroup difficultyGroup;
    /**
     * Initializes the controller class.
     */

    public enum Difficulty
    {
        Easy ,
        Mid ,
        Hard
    }

    Difficulty currentDifficulty;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        currentDifficulty = Difficulty.Easy;
        difficultyGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                oldToggle.setSelected(true);
            }
        });

    }

    @FXML
    private void clickEasy(ActionEvent event) {
        currentDifficulty = Difficulty.Easy;

    }

    @FXML
    private void clickMedium(ActionEvent event) {
        currentDifficulty = Difficulty.Mid;

    }

    @FXML
    private void clickHard(ActionEvent event) {
        currentDifficulty = Difficulty.Hard;

    }

    @FXML
    private void clickRecordGame(ActionEvent event) {
    }

    @FXML
    private void clickPlayGame(ActionEvent event) {
        try {
            AIGameScreenController.setDifficulty(currentDifficulty.ordinal());
            App.setRoot(Screens.AI_GAME_SCREEN);
            
        } catch (IOException ex) {
        }
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        try {
            // todo add alert!
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Back To Menu");
            alert.setHeaderText("Are you sure you want Back To Menu?");

            ButtonType buttonYes = new ButtonType("Back");
            ButtonType buttonNo = new ButtonType("cancel");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == buttonYes) {
                App.setRoot(Screens.HOME_SCREEN);
            }
        }catch (IOException ex){}
        }

}
