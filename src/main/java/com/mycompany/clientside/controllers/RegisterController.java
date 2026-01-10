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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField usernameTxt;
    @FXML
    private Button createAccountBtn;

    @FXML
    private ImageView confirmEyeIcon;
    @FXML
    private ImageView eyeIcon;
    @FXML
    private PasswordField confirmPassTxtHidden;
    @FXML
    private TextField confirmPassTxtPlain;
    @FXML
    private PasswordField passTxtHidden;
    @FXML
    private TextField passTxtPlain;
    @FXML
    private Label passwordErrorMessageLabel;
    @FXML
    private Label confirmPasswordErrorMessageLabel;
    @FXML
    private Label usernameErrorMessageLabel;

    boolean isPasswordVisible;
    boolean isConfirmPasswordVisible;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableErrorMessages();
        isPasswordVisible = false;
        isConfirmPasswordVisible = false;
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

    @FXML
    void toggleConfirmPassword(ActionEvent event) {
        if (isConfirmPasswordVisible) {
            confirmPassTxtPlain.setVisible(false);
            confirmPassTxtHidden.setVisible(true);
            confirmPassTxtHidden.setText(confirmPassTxtPlain.getText());
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            updateConfirmPasswordIcon("/com/mycompany/clientside/images/show_password_eye.png");

        } else {
            confirmPassTxtHidden.setVisible(false);
            confirmPassTxtPlain.setVisible(true);
            confirmPassTxtPlain.setText(confirmPassTxtHidden.getText());
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            updateConfirmPasswordIcon("/com/mycompany/clientside/images/hide_password_eye.png");

        }
    }

    @FXML
    void togglePassword(ActionEvent event) {
        if (isPasswordVisible) {
            passTxtHidden.setText(passTxtPlain.getText());
            passTxtPlain.setVisible(false);
            passTxtHidden.setVisible(true);
            isPasswordVisible = !isPasswordVisible;
            updatePasswordIcon("/com/mycompany/clientside/images/show_password_eye.png");
        } else {
            passTxtPlain.setText(passTxtHidden.getText());
            passTxtHidden.setVisible(false);
            passTxtPlain.setVisible(true);
            isPasswordVisible = !isPasswordVisible;
            updatePasswordIcon("/com/mycompany/clientside/images/hide_password_eye.png");
        }
    }

    private void createAccount() {
        disableErrorMessages();

        ClientManager clientManager = ClientManager.getInstance();
        clientManager.send(new AuthRequest(usernameTxt.getText(), getPassword()), EndPoint.REGISTER, (response) -> {
            AuthResponse authResponse = JsonUtils.fromJson(response, AuthResponse.class);

            Platform.runLater(() -> {
                if (authResponse.getStatusCode() == StatusCode.ERROR) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("An Error Ocurred");
                    alert.setHeaderText(authResponse.getErrorMessage());
                    alert.showAndWait();
                } else {
                    try {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Account Creation");
                        alert.setHeaderText("Account Created Successfully!");
                        alert.showAndWait();
                        App.setRoot(Screens.LOGIN_SCREEN);
                    } catch (IOException ex) {

                    }
                }
            });
        });
    }

    private boolean validateData() {
        String username = usernameTxt.getText();
        String password = getPassword();
        String confirmPass = getConfirmationPassword();
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
        if (!confirmPass.equals(password)) {
            confirmPasswordErrorMessageLabel.setText("Passwords Don't Match");
            isValid = false;
            enableConfirmPasswordError();
        } else {
            disableConfirmPasswordError();
        }
        return isValid;
    }

    private void disableErrorMessages() {
        disableusernameError();
        disablePasswordError();
        disableConfirmPasswordError();
    }

    private void enableusernameError() {
        usernameErrorMessageLabel.setManaged(true);
        usernameErrorMessageLabel.setVisible(true);
    }

    private void enablePasswordError() {
        passwordErrorMessageLabel.setManaged(true);
        passwordErrorMessageLabel.setVisible(true);
    }

    private void enableConfirmPasswordError() {
        confirmPasswordErrorMessageLabel.setManaged(true);
        confirmPasswordErrorMessageLabel.setVisible(true);
    }

    private void disableusernameError() {
        usernameErrorMessageLabel.setManaged(false);
        usernameErrorMessageLabel.setVisible(false);
    }

    private void disablePasswordError() {
        passwordErrorMessageLabel.setManaged(false);
        passwordErrorMessageLabel.setVisible(false);
    }

    private void disableConfirmPasswordError() {
        confirmPasswordErrorMessageLabel.setManaged(false);
        confirmPasswordErrorMessageLabel.setVisible(false);

    }

    private void updateConfirmPasswordIcon(String path) {
        Image img = new Image(getClass().getResource(path).toExternalForm());
        confirmEyeIcon.setImage(img);
    }

    private void updatePasswordIcon(String path) {
        Image img = new Image(getClass().getResource(path).toExternalForm());
        eyeIcon.setImage(img);
    }

    private String getPassword() {
        if (isPasswordVisible) {
            return passTxtPlain.getText();
        }
        return passTxtHidden.getText();
    }

    private String getConfirmationPassword() {
        if (isConfirmPasswordVisible) {
            return confirmPassTxtPlain.getText();
        }
        return confirmPassTxtHidden.getText();
    }
}
