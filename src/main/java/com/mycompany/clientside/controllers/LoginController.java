/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.ChallengeManager;
import com.mycompany.clientside.models.AuthManager;
import com.mycompany.clientside.models.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author Dell
 */
public class LoginController implements Initializable {

    

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField passTxt;
    @FXML
    private Hyperlink forgetPassHyperLink;
    @FXML
    private Button loginBtn;
    @FXML
    private Hyperlink createAccountHyperLink;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handelForgetPassHyperLink(ActionEvent event) {
    }

    @FXML
    private void handelLogin(ActionEvent event) {
        
        int id;
        try {
            id = Integer.parseInt(usernameTxt.getText());
        } catch(NumberFormatException e) {
            id = 10;
        }
        
        AuthManager.currentPlayer = Player.getDummyPlayer(id);
        ChallengeManager.getInstance().listenToChallenges();
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }

    @FXML
    private void navigateToRegister(ActionEvent event) {
        try {
            App.setRoot(Screens.REGISTER_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }

}
