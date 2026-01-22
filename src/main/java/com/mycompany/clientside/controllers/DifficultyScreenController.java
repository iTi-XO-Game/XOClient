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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Mohamed
 */
public class DifficultyScreenController implements Initializable {


    @FXML
    private Button easyButton;

    @FXML
    private Button mediumButton;

    @FXML
    private Button hardButton;
    @FXML
    private ImageView easyImage;

    @FXML
    private ImageView mediumImage;

    @FXML
    private ImageView hardImage;

    private enum Difficulty {
        Easy,
        Mid,
        Hard
    }

    private Difficulty currentDifficulty;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentDifficulty = Difficulty.Easy;
        easyButton.getStyleClass().add("selected");
        new Thread(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
            Platform.runLater(() -> {
                mediumImage.setVisible(false);
                mediumImage.setManaged(false);

                hardImage.setVisible(false);
                hardImage.setManaged(false);
            });
        }).start();
    }

    @FXML
    private void clickEasy(ActionEvent event) {
        if (currentDifficulty != Difficulty.Easy){
            easyButton.getStyleClass().add("selected");
            mediumButton.getStyleClass().remove("selected");
            hardButton.getStyleClass().remove("selected");
            currentDifficulty = Difficulty.Easy;

            easyImage.setVisible(true);
            easyImage.setManaged(true);

            mediumImage.setVisible(false);
            mediumImage.setManaged(false);

            hardImage.setVisible(false);
            hardImage.setManaged(false);
        }
    }

    @FXML
    private void clickMedium(ActionEvent event) {
        if (currentDifficulty != Difficulty.Mid){
            mediumButton.getStyleClass().add("selected");
            easyButton.getStyleClass().remove("selected");
            hardButton.getStyleClass().remove("selected");
            currentDifficulty = Difficulty.Mid;

            easyImage.setVisible(false);
            easyImage.setManaged(false);

            mediumImage.setVisible(true);
            mediumImage.setManaged(true);

            hardImage.setVisible(false);
            hardImage.setManaged(false);
        }
    }

    @FXML
    private void clickHard(ActionEvent event) {
        if (currentDifficulty != Difficulty.Hard){
            hardButton.getStyleClass().add("selected");
            easyButton.getStyleClass().remove("selected");
            mediumButton.getStyleClass().remove("selected");
            currentDifficulty = Difficulty.Hard;

            hardImage.setManaged(true);
            hardImage.setVisible(true);

            easyImage.setManaged(false);
            easyImage.setVisible(false);

            mediumImage.setManaged(false);
            mediumImage.setVisible(false);
        }
    }

    @FXML
    private void clickRecordGame(ActionEvent event) {
        try {
            AIGameScreenController.setDifficulty(currentDifficulty.ordinal());
            AIGameScreenController.setRecordMode(true);
            App.setRoot(Screens.AI_GAME_SCREEN);
        } catch (IOException ex) {
            System.getLogger(DifficultyScreenController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clickPlayGame(ActionEvent event) {
        try {
            AIGameScreenController.setDifficulty(currentDifficulty.ordinal());
            AIGameScreenController.setRecordMode(false);//record mode is false
            App.setRoot(Screens.AI_GAME_SCREEN);

        } catch (IOException ex) {
        }
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
