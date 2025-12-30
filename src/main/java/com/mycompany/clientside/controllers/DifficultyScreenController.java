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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickEasy(ActionEvent event) {
        System.out.println("Easy mode");
    }

    @FXML
    private void clickMedium(ActionEvent event) {
    }

    @FXML
    private void clickHard(ActionEvent event) {
    }

    @FXML
    private void clickRecordGame(ActionEvent event) {
    }

    @FXML
    private void clickPlayGame(ActionEvent event) {
        try { // todo handle the AI
            App.setRoot(Screens.GAME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }
    
}
