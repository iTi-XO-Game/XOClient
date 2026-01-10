/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

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
 * @author Mohamed
 */
public class ForgotScreenController implements Initializable {


    @FXML
    private TextField secretText;
    @FXML
    private TextField passTxt;
    @FXML
    private TextField conformPassTxt;
    @FXML
    private Button checkBtn;
    @FXML
    private Hyperlink loginHyperLink;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handelCheck(ActionEvent event) {
    }

    @FXML
    private void navigateToLogin(ActionEvent event) {
    }

}
