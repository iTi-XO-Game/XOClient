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
import com.mycompany.clientside.models.LobbyData;
import com.mycompany.clientside.models.LobbyData.LobbyAction;
import com.mycompany.clientside.models.Player;
import com.mycompany.clientside.models.UserSession;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Depogramming
 */
public class OnlineMultiPlayerController implements Initializable {

    Map<Integer, OnlinePlayerCardController> playerControllers = new HashMap<>();

    @FXML
    private VBox playersContainer;
    @FXML
    private Label onlinePlayersCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listenToLobbyEvents();
    }

    private void listenToLobbyEvents() {
        ClientManager clientManager = ClientManager.getInstance();

        Player currentPlayer = UserSession.currentPlayer;
        LobbyData request = new LobbyData(
                currentPlayer.getId(), 
                LobbyAction.LISTEN, 
                new Player(),
                List.of());

        clientManager.sendListener(request, EndPoint.LOBBY, response -> {

            LobbyData lobby = JsonUtils.fromJson(response, LobbyData.class);

            Platform.runLater(() -> {

                switch (lobby.getLobbyAction()) {
                    case LISTEN ->
                        addPlayerList(lobby.getAllPlayers());

                    case ADD_ONE -> {
                        Player updatedPlayer = lobby.getUpdatedPlayer();
                        OnlinePlayerCardController controller = playerControllers.get(updatedPlayer.getId());
                        int count = Integer.parseInt(onlinePlayersCount.getText()) + 1;
                        onlinePlayersCount.setText("" + count);
                        if (controller != null) {
                            controller.setPlayer(updatedPlayer);
                        } else {
                            addPlayer(lobby.getUpdatedPlayer());
                        }
                    }

                    case REMOVE_ONE -> {
                        Player updatedPlayer = lobby.getUpdatedPlayer();
                        OnlinePlayerCardController controller = playerControllers.remove(updatedPlayer.getId());
                        int count = Integer.parseInt(onlinePlayersCount.getText()) - 1;
                        onlinePlayersCount.setText("" + count);
                        if (controller != null) {
                            playersContainer.getChildren()
                                    .remove(controller.getRoot());
                        }
                    }

                    case ERROR -> {
                    }

                }

            });

        });
    }

    public void addPlayerList(List<Player> players) {
        playersContainer.getChildren().clear();

        onlinePlayersCount.setText(players.size() + "");
        players.forEach(this::addPlayer);

    }

    public void addPlayer(Player player) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(Screens.ONLINE_PLAYER_CARD + ".fxml"));
            Parent card = loader.load();
            OnlinePlayerCardController controller = loader.getController();
            controller.setPlayer(player);
            playersContainer.getChildren().add(card);
            playerControllers.put(player.getId(), controller);
        } catch (IOException ex) {
        }
    }

    @FXML
    private void refreshFunction(ActionEvent event) {

        listenToLobbyEvents();
    }

    @FXML
    private void navigateBack(ActionEvent event) {
        ClientManager.getInstance().removeListener(EndPoint.LOBBY.getCode());
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo add alert!
        }
    }
}
