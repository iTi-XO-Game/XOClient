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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    private Label errorMessageLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessageLabel.setManaged(false);
        // TODO
    }

    @FXML
    private void handelCreateAccount(ActionEvent event) {
        if (!validateData()) {
            return;
        }
        createAccount();
    }

    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            App.setRoot(Screens.LOGIN_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }

    private void createAccount() {
        errorMessageLabel.setManaged(false);
        errorMessageLabel.setVisible(false);

        ClientManager clientManager = ClientManager.getInstance();
        clientManager.send(new AuthRequest(userNameTxt.getText(), passTxt.getText()), EndPoint.REGISTER, (response) -> {
            AuthResponse authResponse = JsonUtils.fromJson(response, AuthResponse.class);

            Platform.runLater(() -> {
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
        });
    }

    private boolean validateData() {
        String userName = userNameTxt.getText();
        String password = passTxt.getText();
        String confirmPass = conformPassTxt.getText();
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();
        if (userName.isBlank()) {
            errorMessage.append("User Name Can't Be Empty\n");
            isValid = false;
        }
        if (password.isEmpty()) {
            errorMessage.append("Password Can't Be Empty\n");
            isValid = false;
        } else if (!confirmPass.equals(password)) {
            errorMessage.append("Passwords Don't Match");
            isValid = false;
        }

        if (!isValid) {
            errorMessageLabel.setVisible(true);
            errorMessageLabel.setManaged(true);
            errorMessageLabel.setText(errorMessage.toString());
        }
        return isValid;
    }

}
