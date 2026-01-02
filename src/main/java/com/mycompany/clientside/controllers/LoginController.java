/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.serverhandler.message.Header;
import com.mycompany.clientside.serverhandler.message.LogIn;
import com.mycompany.clientside.serverhandler.message.Message;
import com.mycompany.clientside.serverhandler.communication.RecievingResponsesThread;
import com.mycompany.clientside.serverhandler.communication.SendingRequests;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.google.gson.Gson;

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
    private Thread receiverThread;
    Gson gson;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gson = new Gson();
        startReceiverThread();

    }

    @FXML
    private void handelForgetPassHyperLink(ActionEvent event) {
    }

    @FXML
    private void handelLogin(ActionEvent event) {

            String username = usernameTxt.getText();
            String password = passTxt.getText();
            Message msg = new Message(
                    new Header(Header.MessageType.REQUEST, Header.ActionType.LOGIN),
                    new LogIn(username, password)
            );

            String json = gson.toJson(msg);

            // 1. Send request
            SendingRequests sendingRequests = new SendingRequests(json);

            
            

    }

    private void showLoginFailed() {
        // TODO: Alert dialog
    }

    private void startReceiverThread() {
        if (receiverThread != null && receiverThread.isAlive()) {
            return;
        }

        RecievingResponsesThread receiver
                = RecievingResponsesThread.getInstance();

        receiverThread = new Thread(receiver);
        receiverThread.setDaemon(true);
        receiverThread.start();
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
