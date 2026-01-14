/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.ClientManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Depogramming
 */
public class ServerSettingsScreenController implements Initializable {

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;
    @FXML
    private Label ipErrorLabel;
    @FXML
    private Label portErrorLabel;
    @FXML
    private Label resetButtonLabel;

    @FXML
    private Label updateButtonLabel;

    @FXML
    private ImageView backButtonIcon;
    @FXML
    private VBox rootContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // set the ip and port defaults to the one on the client manager
        ipTextField.setText("127.0.0.1");
        portTextField.setText(ClientManager.PORT + "");
        toggleAllLabels(false);
        backButtonIcon.setOnMouseClicked((event) -> {
            try {
                App.setRoot(Screens.LOGIN_SCREEN);
            } catch (IOException ex) {
            }
        });
        Platform.runLater(() -> rootContainer.requestFocus());
    }

    @FXML
    void resetButtonClicked(ActionEvent event) {
        toggleAllLabels(false);

        ipTextField.setText("127.0.0.1");
        ClientManager.IP_ADDRESS = "127.0.0.1";
        portTextField.setText(ClientManager.PORT + "");
        resetButtonLabel.setText("Reset Successfully");
        toggleResetButtonLabel(true);
    }

    @FXML
    void updateButtonClicked(ActionEvent event) {
        toggleAllLabels(false);
        String ip = ipTextField.getText();
        String port = portTextField.getText();
        boolean validIP = isValidIP(ip);
        boolean validPort = isValidPort(port);
        if (validIP && validPort) {
            ClientManager.IP_ADDRESS = ipTextField.getText();
            ClientManager.PORT = Integer.parseInt(portTextField.getText());
            toggleUpdateButtonLabel(true);
            updateButtonLabel.setText("IP And Port Updated Successfully");

        }
        if (!validIP) {
            toggleIPTextField(true);
            ipErrorLabel.setText("Enter a Valid IP");

        } else {
            toggleIPTextField(false);
        }
        if (!validPort) {
            togglePortTextField(true);
            portErrorLabel.setText("Enter a valid port");

        } else {
            togglePortTextField(false);
            toggleResetButtonLabel(false);
        }
    }

    public boolean isValidIP(String ip) {

        String ipPattern
                = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        if (ip == null || ip.isEmpty()) {
            return false;
        }

        return ip.matches(ipPattern);
    }

    public boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            //if it's not a number...
            return false;
        }
    }

    private void toggleAllLabels(boolean state) {
        toggleIPTextField(state);
        togglePortTextField(state);
        toggleUpdateButtonLabel(state);
        toggleResetButtonLabel(state);
    }

    private void toggleIPTextField(boolean state) {
        ipErrorLabel.setManaged(state);
        ipErrorLabel.setVisible(state);
    }

    private void togglePortTextField(boolean state) {
        portErrorLabel.setManaged(state);
        portErrorLabel.setVisible(state);
    }

    private void toggleUpdateButtonLabel(boolean state) {
        updateButtonLabel.setManaged(state);
        updateButtonLabel.setVisible(state);
    }

    private void toggleResetButtonLabel(boolean state) {
        resetButtonLabel.setManaged(state);
        resetButtonLabel.setVisible(state);
    }
}
