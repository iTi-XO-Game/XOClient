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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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
        System.out.println(currentDifficulty.toString());

    }

    @FXML
    private void clickMedium(ActionEvent event) {
        currentDifficulty = Difficulty.Mid;
        System.out.println(currentDifficulty.toString());

    }

    @FXML
    private void clickHard(ActionEvent event) {
        currentDifficulty = Difficulty.Hard;
        System.out.println(currentDifficulty.toString());

    }

    @FXML
    private void clickRecordGame(ActionEvent event) {
    }

    @FXML
    private void clickPlayGame(ActionEvent event) {
        try {
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //saddly, it should be like that to be able to pass a variable to the other controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/clientside/screens/AIGameScreen.fxml"));
            Parent root = loader.load();
            
            AIGameScreenController controllerB = loader.getController();
            controllerB.setData(currentDifficulty.ordinal());
            App.setRoot(root);
            stage.setMaximized(true);

        } catch (IOException ex) {
            ex.printStackTrace();
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
