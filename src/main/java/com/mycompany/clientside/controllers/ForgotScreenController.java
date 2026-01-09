/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author Mohamed
 */
public class ForgotScreenController implements Initializable {


    @FXML
    private TextField passTxt;
    @FXML
    private TextField conformPassTxt;
    @FXML
    private Hyperlink loginHyperLink;
    @FXML
    private TextField secretText;
    @FXML
    private Button checkBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handelCheck(ActionEvent event)
    {
    }

    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            App.setRoot(Screens.LOGIN_SCREEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
