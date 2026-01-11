/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.ChallengeManager;
import com.mycompany.clientside.client.ClientManager;
import com.mycompany.clientside.client.EndPoint;
import com.mycompany.clientside.client.JsonUtils;
import com.mycompany.clientside.models.StatusCode;
import com.mycompany.clientside.models.AuthRequest;
import com.mycompany.clientside.models.AuthResponse;
import com.mycompany.clientside.models.UserSession;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passTxt;
    @FXML
    private TextField passTxtPlain;
    @FXML
    private Hyperlink forgetPassHyperLink;
    @FXML
    private Button loginBtn;
    @FXML
    private Hyperlink createAccountHyperLink;
    @FXML
    private ImageView eyeIcon;
    @FXML
    private Label usernameErrorMessageLabel;
    @FXML
    private Label passwordErrorMessageLabel;
    boolean isPasswordVisible;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableErrorMessages();
        isPasswordVisible = false;
        passTxtPlain.setVisible(false);
    }

    @FXML
    private void handelForgetPassHyperLink(ActionEvent event) {
        try {
            App.setRoot(Screens.FORGOT_SCREEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handelLogin(ActionEvent event) {

        if (!validateData()) {
            return;
        }

        ClientManager clientManager = ClientManager.getInstance();

        AuthRequest loginRequest = new AuthRequest(usernameTxt.getText(), getPassword());
        clientManager.send(loginRequest, EndPoint.LOGIN, response -> {
            try {
                AuthResponse loginResponse = JsonUtils.fromJson(response, AuthResponse.class);

                Platform.runLater(() -> {
                    if (loginResponse.getStatusCode() == StatusCode.ERROR) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("An Error Ocurred");
                        alert.setHeaderText(loginResponse.getErrorMessage());
                        alert.showAndWait();
                    } else {
                        UserSession.setUserId(loginResponse.getId());
                        UserSession.setUsername(loginResponse.getUsername());
                        UserSession.setCurrentPlayer(loginResponse.getPlayer());
                        ChallengeManager.getInstance().listenToChallenges();
                        try {
                            App.setRoot(Screens.HOME_SCREEN);
                        } catch (IOException ex) {
                        }
                    }
                });
            } catch (Exception e) {

            }

        });
    }

    @FXML
    private void navigateToRegister(ActionEvent event) {
        try {
            App.setRoot(Screens.REGISTER_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }

    @FXML
    void togglePassword(ActionEvent event) {
        if (isPasswordVisible) {

            passTxt.setText(passTxtPlain.getText());
            passTxt.setVisible(true);
            passTxtPlain.setVisible(false);

            updateIcon("images/show_password_eye.png");
        } else {

            passTxtPlain.setText(passTxt.getText());
            passTxtPlain.setVisible(true);
            passTxt.setVisible(false);

            updateIcon("images/hide_password_eye.png");
        }
        isPasswordVisible = !isPasswordVisible;
    }

    private void updateIcon(String path) {
        Image img = new Image(App.class.getResource(path).toExternalForm());
        eyeIcon.setImage(img);
    }

    private String getPassword() {
        if (isPasswordVisible) {
            return passTxtPlain.getText();
        }
        return passTxt.getText();
    }

    private boolean validateData() {
        String username = usernameTxt.getText();
        String password = getPassword();
        boolean isValid = true;
        if (username.isBlank()) {
            usernameErrorMessageLabel.setText("User Name Can't Be Empty");
            isValid = false;
            enableusernameError();
        } else if (username.length() < 3) {
            usernameErrorMessageLabel.setText("User Name Length Must Be Bigger Than 3");
            enableusernameError();
            isValid = false;
        } else {
            disableusernameError();
        }
        if (password.isEmpty()) {
            passwordErrorMessageLabel.setText("Password Can't Be Empty");
            isValid = false;
            enablePasswordError();
        } else if (password.length() < 6) {
            passwordErrorMessageLabel.setText("Password Length Must Be Higher Than 6");
            isValid = false;
            enablePasswordError();
        } else {
            disablePasswordError();
        }
        return isValid;
    }

    private void disableErrorMessages() {
        disableusernameError();
        disablePasswordError();
    }

    private void enableusernameError() {
        usernameErrorMessageLabel.setManaged(true);
        usernameErrorMessageLabel.setVisible(true);
    }

    private void enablePasswordError() {
        passwordErrorMessageLabel.setManaged(true);
        passwordErrorMessageLabel.setVisible(true);
    }

    private void disableusernameError() {
        usernameErrorMessageLabel.setManaged(false);
        usernameErrorMessageLabel.setVisible(false);
    }

    private void disablePasswordError() {
        passwordErrorMessageLabel.setManaged(false);
        passwordErrorMessageLabel.setVisible(false);
    }

}
