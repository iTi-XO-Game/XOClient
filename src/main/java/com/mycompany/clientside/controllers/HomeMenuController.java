/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.helpers.NavigationHelper;
import com.mycompany.clientside.helpers.RoutesHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lenovo
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
    }

    @FXML
    private void onSingleClick(ActionEvent event) {
    }

    @FXML
    private void onLocalModeClick(ActionEvent event) {
    }

    @FXML
    private void onOnlineModeClick(ActionEvent event) {
    }

    @FXML
    private void onScoreClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RoutesHelper.PLAYER_STATE));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            NavigationHelper.navigateTo(stage, root);
        } catch (IOException ex) {
            //show dialog or something
            ex.printStackTrace();
        }
    }

    @FXML
    private void onReplayClick(ActionEvent event) {
    }

}
