package com.mycompany.clientside.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.*;
import com.mycompany.clientside.models.AuthRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ForgotScreenController implements Initializable {

    @FXML
    private TextField secretTxt;
    @FXML
    private Label secretErrorMessageLabel;

    @FXML
    private TextField passTxtPlain;
    @FXML
    private PasswordField passTxtHidden;
    @FXML
    private ImageView eyeIcon;
    @FXML
    private Label passwordErrorMessageLabel;

    @FXML
    private TextField confirmPassTxtPlain;
    @FXML
    private PasswordField confirmPassTxtHidden;
    @FXML
    private ImageView confirmEyeIcon;
    @FXML
    private Label confirmPasswordErrorMessageLabel;

    @FXML
    private Button checkBtn;
    @FXML
    private Hyperlink loginHyperLink;

    private boolean isPasswordVisible;
    private boolean isConfirmPasswordVisible;
    @FXML
    private TextField usernameField;
    @FXML
    private Label usernameLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableErrorMessages();
        isPasswordVisible = false;
        isConfirmPasswordVisible = false;

        passTxtPlain.setVisible(false);
        passTxtPlain.setManaged(false);
        confirmPassTxtPlain.setManaged(false);
        confirmPassTxtPlain.setVisible(false);
    }

    @FXML
    private void togglePassword(ActionEvent event) {
        if (isPasswordVisible) {
            passTxtHidden.setText(passTxtPlain.getText());
            passTxtPlain.setVisible(false);
            passTxtPlain.setManaged(false);
            passTxtHidden.setVisible(true);
            passTxtHidden.setManaged(true);
            updatePasswordIcon("images/show_password_eye.png");
            isPasswordVisible = false;
        } else {
            passTxtPlain.setText(passTxtHidden.getText());
            passTxtHidden.setVisible(false);
            passTxtHidden.setManaged(false);
            passTxtPlain.setVisible(true);
            passTxtPlain.setManaged(true);
            isPasswordVisible = true;
            updatePasswordIcon("images/hide_password_eye.png");
        }
    }

    @FXML
    private void toggleConfirmPassword(ActionEvent event) {
        if (isConfirmPasswordVisible) {
            confirmPassTxtHidden.setText(confirmPassTxtPlain.getText());
            confirmPassTxtPlain.setVisible(false);
            confirmPassTxtPlain.setManaged(false);
            confirmPassTxtHidden.setVisible(true);
            confirmPassTxtHidden.setManaged(true);
            isConfirmPasswordVisible = false;
            updateConfirmPasswordIcon("images/show_password_eye.png");
        } else {
            confirmPassTxtPlain.setText(confirmPassTxtHidden.getText());
            confirmPassTxtHidden.setVisible(false);
            confirmPassTxtHidden.setManaged(false);
            confirmPassTxtPlain.setVisible(true);
            confirmPassTxtPlain.setManaged(true);
            isConfirmPasswordVisible = true;
            updateConfirmPasswordIcon("images/hide_password_eye.png");
        }
    }

    @FXML
    private void handelCheck(ActionEvent event) {
        if (!validateData()) {
            return;
        }

        String theSecretKey = secretTxt.getText();

        if (theSecretKey.equals("ITI")) {

            ClientManager clientManager = ClientManager.getInstance();

            String username = usernameField.getText();
            String pass = getPassword();

            AuthRequest authRequest = new AuthRequest(username, pass);


            clientManager.send(authRequest, EndPoint.UPDATE_USER_PASS, responseJson
                    -> {

                Platform.runLater(()
                        -> {
                    boolean isDone = JsonUtils.fromJson(responseJson, Boolean.class);
                    AlertBuilder alertBuilder = new AlertBuilder();
                    if (isDone) {
                        alertBuilder
                                .setTitle("Password was updated")
                                .setSubTitle("Your password was updated successfully :)")
                                .setAcceptText("Go to log in")
                                .show();
                        navigateToLogin(null);
                    } else {
                        alertBuilder
                                .setTitle("Username Not Found")
                                .setSubTitle("Can't Find This username. Please, try again.")
                                .setAcceptText("Dismiss")
                                .show();
                    }

                });

            });
        } else {
            AlertBuilder alertBuilder = new AlertBuilder();
            alertBuilder
                    .setTitle("An Error Occurred")
                    .setSubTitle("Secret Key Is Wrong")
                    .setAcceptText("Dismiss")
                    .show();
        }
    }

    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            App.setRoot(Screens.LOGIN_SCREEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateData() {
        boolean isValid = true;

        if (secretTxt.getText().isBlank()) {
            secretErrorMessageLabel.setText("Secret can't be empty");
            enableSecretError();
            isValid = false;
        } else {
            disableSecretError();
        }

        String username = usernameField.getText();
        if (username.isEmpty()) {
            usernameLabel.setText("Username can't be empty");
            enableusernameEror();
            isValid = false;
        } else if (username.length() < 3) {
            usernameLabel.setText("User Name Length Must Be Bigger Than 3");
            enableusernameEror();
            isValid = false;
        } else {
            disableusernameEror();
        }

        String password = getPassword();
        if (password.isEmpty()) {
            passwordErrorMessageLabel.setText("Password can't be empty");
            enablePasswordError();
            isValid = false;
        } else if (password.length() < 6) {
            passwordErrorMessageLabel.setText("Password Length Must Be 6 or more letters");
            enablePasswordError();
            isValid = false;
        } else {
            disablePasswordError();
        }

        if (!getConfirmationPassword().equals(password)) {
            confirmPasswordErrorMessageLabel.setText("Passwords don't match");
            enableConfirmPasswordError();
            isValid = false;
        } else {
            disableConfirmPasswordError();
        }

        return isValid;
    }

    private String getPassword() {
        return isPasswordVisible ? passTxtPlain.getText() : passTxtHidden.getText();
    }

    private String getConfirmationPassword() {
        return isConfirmPasswordVisible
                ? confirmPassTxtPlain.getText()
                : confirmPassTxtHidden.getText();
    }

    private void disableErrorMessages() {
        disableSecretError();
        disablePasswordError();
        disableConfirmPasswordError();
        disableusernameEror();
    }

    private void updateConfirmPasswordIcon(String path) {
        try {
            Image img = new Image(App.class.getResource(path).toExternalForm());
            confirmEyeIcon.setImage(img);
        } catch (Exception e) {
        }
    }

    private void updatePasswordIcon(String path) {
        try {
            Image img = new Image(App.class.getResource(path).toExternalForm());
            eyeIcon.setImage(img);
        } catch (Exception e) {

        }
    }

    private void enableSecretError() {
        secretErrorMessageLabel.setManaged(true);
        secretErrorMessageLabel.setVisible(true);
    }

    private void disableSecretError() {
        secretErrorMessageLabel.setManaged(false);
        secretErrorMessageLabel.setVisible(false);
    }

    private void enablePasswordError() {
        passwordErrorMessageLabel.setManaged(true);
        passwordErrorMessageLabel.setVisible(true);
    }

    private void disablePasswordError() {
        passwordErrorMessageLabel.setManaged(false);
        passwordErrorMessageLabel.setVisible(false);
    }

    private void enableConfirmPasswordError() {
        confirmPasswordErrorMessageLabel.setManaged(true);
        confirmPasswordErrorMessageLabel.setVisible(true);
    }

    private void disableConfirmPasswordError() {
        confirmPasswordErrorMessageLabel.setManaged(false);
        confirmPasswordErrorMessageLabel.setVisible(false);
    }

    private void enableusernameEror() {
        usernameLabel.setManaged(true);
        usernameLabel.setVisible(true);
    }

    private void disableusernameEror() {
        usernameLabel.setManaged(false);
        usernameLabel.setVisible(false);
    }
}
