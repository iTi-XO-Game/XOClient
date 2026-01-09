package com.mycompany.clientside.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableErrorMessages();
        isPasswordVisible = false;
        isConfirmPasswordVisible = false;

        passTxtPlain.setVisible(false);
        confirmPassTxtPlain.setVisible(false);
    }

    // ================= TOGGLE PASSWORD =================
    @FXML
    private void togglePassword(ActionEvent event) {
        if (isPasswordVisible) {
            passTxtHidden.setText(passTxtPlain.getText());
            passTxtPlain.setVisible(false);
            passTxtHidden.setVisible(true);
            isPasswordVisible = false;
        } else {
            passTxtPlain.setText(passTxtHidden.getText());
            passTxtHidden.setVisible(false);
            passTxtPlain.setVisible(true);
            isPasswordVisible = true;
        }
    }

    // ================= TOGGLE CONFIRM PASSWORD =================
    @FXML
    private void toggleConfirmPassword(ActionEvent event) {
        if (isConfirmPasswordVisible) {
            confirmPassTxtHidden.setText(confirmPassTxtPlain.getText());
            confirmPassTxtPlain.setVisible(false);
            confirmPassTxtHidden.setVisible(true);
            isConfirmPasswordVisible = false;
        } else {
            confirmPassTxtPlain.setText(confirmPassTxtHidden.getText());
            confirmPassTxtHidden.setVisible(false);
            confirmPassTxtPlain.setVisible(true);
            isConfirmPasswordVisible = true;
        }
    }

    @FXML
    private void handelCheck(ActionEvent event) {
        if (!validateData()) return;

        String theSecretKey = secretTxt.getText();
        System.out.println(theSecretKey);

        if(Objects.equals(theSecretKey, "ITI"))
        {
            System.out.println("Done");

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An Error Ocurred");
            alert.setHeaderText("Secret Key Is Wrong");
            alert.showAndWait();
        }
    }

    // ================= NAVIGATE LOGIN =================
    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            App.setRoot(Screens.LOGIN_SCREEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ================= VALIDATION =================
    private boolean validateData() {
        boolean isValid = true;

        if (secretTxt.getText().isBlank()) {
            secretErrorMessageLabel.setText("Secret can't be empty");
            enableSecretError();
            isValid = false;
        } else {
            disableSecretError();
        }

        String password = getPassword();
        if (password.isEmpty()) {
            passwordErrorMessageLabel.setText("Password can't be empty");
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

    // ================= GETTERS =================
    private String getPassword() {
        return isPasswordVisible ? passTxtPlain.getText() : passTxtHidden.getText();
    }

    private String getConfirmationPassword() {
        return isConfirmPasswordVisible
                ? confirmPassTxtPlain.getText()
                : confirmPassTxtHidden.getText();
    }

    // ================= ERRORS HANDLING =================
    private void disableErrorMessages() {
        disableSecretError();
        disablePasswordError();
        disableConfirmPasswordError();
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
}
