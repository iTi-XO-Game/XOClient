/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.ClientManager;
import com.mycompany.clientside.client.EndPoint;
import com.mycompany.clientside.client.JsonUtils;
import com.mycompany.clientside.client.StatusCode;
import com.mycompany.clientside.models.AuthRequest;
import com.mycompany.clientside.models.AuthResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField userNameTxt;
    @FXML
    private TextField passTxt;
    @FXML
    private TextField conformPassTxt;
    @FXML
    private Button createAccountBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handelCreateAccount(ActionEvent event) {
        ClientManager clientManager = ClientManager.getInstance();
        clientManager.send(new AuthRequest("1", "2"), EndPoint.REGISTER, (response) -> {
            AuthResponse authResponse = JsonUtils.fromJson(response, AuthResponse.class);
            if (authResponse.getStatusCode() == StatusCode.ERROR) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An Error Ocurred");
                alert.setHeaderText(authResponse.getErrorMessage());
                alert.showAndWait();
            } else {
                try {
                    App.setRoot(Screens.HOME_SCREEN);
                } catch (IOException ex) {
                    // todo add alert!
                }
            }
        });
    }

    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            App.setRoot(Screens.LOGIN_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }

}
