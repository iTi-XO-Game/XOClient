/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.models.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Depogramming
 */
public class OnlineMultPlayerController implements Initializable {

    ArrayList<Player> playersList;
    @FXML
    private VBox playersContainer;
    @FXML
    private Label onlinePlayersCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playersList = new ArrayList<>();
        gettingPlayersList();
        updatePlayerList(playersList);
    }

    private void gettingPlayersList() {
        //do some logic with the server...
        //but for now there's no server, I am the server
        playersList.clear();
        playersList.add(new Player(1, "Ali", 10, 1,true));
        playersList.add(new Player(2, "Ali", 20, 2,false));
        playersList.add(new Player(3, "Ali", 30, 3,true));
        playersList.add(new Player(4, "Ali", 40, 4,false));
        playersList.add(new Player(5, "Ali", 50, 5,false));
        playersList.add(new Player(6, "Ali", 60, 6,false));
        playersList.add(new Player(7, "Ali", 70, 7,false));
        onlinePlayersCount.setText(playersList.size() + "");
    }

    public void updatePlayerList(List<Player> players) {
        // Clear old data
        playersContainer.getChildren().clear();

        for (Player p : players) {
            try {
                // 1. Load the template for each player
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screens.ONLINE_MULTIPLAYER_PLAYER_CARD+".fxml"));
                Parent card = loader.load();

                // 2. Get the references to labels from the loaded FXML
                Label nameLabel = (Label) card.lookup("#nameLabel");
                Label winsLabel = (Label) card.lookup("#winsLabel");
                Label losesLabel = (Label) card.lookup("#losesLabel");
                Button btn = (Button) card.lookup("#challengeButton");
                if(p.isInGame()){
                    btn.setDisable(true);
                    btn.setText("In Game");
                }
                // 3. Set the data
                nameLabel.setText(p.getUserName());
                winsLabel.setText("W:" + p.getWins());
                losesLabel.setText("L:" + p.getLosses());

                btn.setOnAction(e -> handleChallenge(p, btn));
                // 4. Add to the main container
                playersContainer.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleChallenge(Player p, Button btn) {
        System.out.println("We clicked on button for player"+p.getId());
        btn.setDisable(true);
        btn.setText("Waiting");
        // send to server...
        // if it fails:
        // btn.setDisable(false);
    }

    @FXML
    void refreshFunction(ActionEvent event) {
        gettingPlayersList();
        updatePlayerList(playersList);
    }
        @FXML
    private void navigateBack(ActionEvent event) {
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }
}
