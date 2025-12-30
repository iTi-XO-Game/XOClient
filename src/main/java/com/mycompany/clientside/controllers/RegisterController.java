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
        // todo
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
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
