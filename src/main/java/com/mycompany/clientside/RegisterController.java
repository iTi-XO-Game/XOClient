/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
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
    }

    @FXML
    private void handelLoginHyperLink(ActionEvent event) {
    }

}
