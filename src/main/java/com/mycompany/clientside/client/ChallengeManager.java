/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.client;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.controllers.PlayerRequestScreenController;
import com.mycompany.clientside.models.AuthManager;
import com.mycompany.clientside.models.Challenge;
import com.mycompany.clientside.models.Challenge.ChallengeAction;
import com.mycompany.clientside.models.Player;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Hossam
 */
public class ChallengeManager {

    private static final ChallengeManager INSTANCE = new ChallengeManager();

    public static ChallengeManager getInstance() {
        return INSTANCE;
    }

    private ChallengeManager() {
    }

    private Runnable resetPlayerCard;
    private final Map<String, Stage> openedDialogs = new ConcurrentHashMap<>();
    private final Map<String, Challenge> acceptedChallenge = new ConcurrentHashMap<>();

    public void listenToChallenges() {
        ClientManager clientManager = ClientManager.getInstance();

        Player player1 = AuthManager.currentPlayer;
        // todo remove and make
        Player player2 = Player.getDummyPlayer(-1);

        Challenge request = new Challenge(
                "", ChallengeAction.LISTEN, player1, player2, ""
        );

        clientManager.sendListener(request, EndPoint.CHALLENGE,
                response -> {
                    Challenge challenge = JsonUtils.fromJson(response, Challenge.class);

                    switch (challenge.getAction()) {
                        case ChallengeAction.SEND -> {
                            Platform.runLater(() -> {
                                showChallengeReceived(challenge);
                            });
                        }
                        case ChallengeAction.ACCEPT ->
                            sendGame(challenge);
                        case ChallengeAction.DECLINE -> {
                            if (resetPlayerCard != null) {
                                resetPlayerCard.run();
                                resetPlayerCard = null;
                            }
                            Stage stage = openedDialogs.remove(challenge.getId());
                            if (stage != null) {
                                stage.close();
                            }
                        }
                        case ChallengeAction.CANCEL -> {
                            Stage stage = openedDialogs.remove(challenge.getId());
                            if (stage != null) {
                                stage.close();
                            }
                        }
                        case ChallengeAction.ERROR ->
                            showError(challenge);
                    }
                }
        );
    }

    private void showChallengeReceived(Challenge challenge) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(Screens.PLAYER_REQUEST_DIALOG + ".fxml"));
            Parent root = loader.load();
            PlayerRequestScreenController controller = loader.getController();

            controller.setChallengeData(challenge, challenge.getSender());

            controller.setAcceptChallenge(this::acceptChallenge);
            controller.setDeclineChallenge(this::declineChallenge);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            openedDialogs.put(challenge.getId(), stage);
            stage.setOnCloseRequest(e -> {
                if (!acceptedChallenge.containsKey(challenge.getId())) {
                    declineChallenge(challenge);
                }
            });

            stage.setTitle("Challenge Received!");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {

        }
    }

    public void sendChallenge(Player opponent, Runnable resetPlayerCard) {
        ClientManager clientManager = ClientManager.getInstance();

        this.resetPlayerCard = resetPlayerCard;
        Player player1 = AuthManager.currentPlayer;
        Challenge request = new Challenge(
                UUID.randomUUID().toString(), ChallengeAction.SEND, player1, opponent, ""
        );

        clientManager.send(request, EndPoint.CHALLENGE,
                ignored -> {
                }
        );
        showSendingChallenge(request);
    }

    private void showSendingChallenge(Challenge challenge) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(Screens.PLAYER_REQUEST_DIALOG + ".fxml"));
            Parent root = loader.load();
            PlayerRequestScreenController controller = loader.getController();
            controller.setChallengeData(challenge, challenge.getReceiver());

            controller.setCancelChallenge(this::cancelChallenge);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            openedDialogs.put(challenge.getId(), stage);
            stage.setOnCloseRequest(e -> {
                if (!acceptedChallenge.containsKey(challenge.getId())) {
                    cancelChallenge(challenge);
                }
            });
            stage.setTitle("Challenge Received!");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {

        }
    }

    private void cancelChallenge(Challenge challenge) {
        ClientManager clientManager = ClientManager.getInstance();

        if (resetPlayerCard != null) {
            resetPlayerCard.run();
            resetPlayerCard = null;
        }

        Player player = AuthManager.currentPlayer;
        Challenge request = new Challenge(
                challenge.getId(), ChallengeAction.CANCEL, player, challenge.getReceiver(), ""
        );

        clientManager.send(request, EndPoint.CHALLENGE,
                ignored -> {
                }
        );
    }

    private void declineChallenge(Challenge challenge) {
        ClientManager clientManager = ClientManager.getInstance();

        Player player = AuthManager.currentPlayer;
        Challenge request = new Challenge(
                challenge.getId(), ChallengeAction.DECLINE, player, challenge.getSender(), ""
        );

        clientManager.send(request, EndPoint.CHALLENGE,
                ignored -> {
                }
        );
    }

    private void sendGame(Challenge challenge) {

        acceptedChallenge.put(challenge.getId(), challenge);
        closeAllDialogs();
        // todo send game to GAME endpoint
        Platform.runLater(() -> {
            try {
                App.setRoot(Screens.GAME_SCREEN);
            } catch (IOException ex) {
            }
        });
    }

    private void acceptChallenge(Challenge challenge) {
        ClientManager clientManager = ClientManager.getInstance();

        Player player = AuthManager.currentPlayer;
        Challenge request = new Challenge(
                challenge.getId(), ChallengeAction.ACCEPT, player, challenge.getSender(), ""
        );

        // todo Add game listerner
        clientManager.send(request, EndPoint.CHALLENGE,
                response -> {

                    Challenge chall = JsonUtils.fromJson(response, Challenge.class);

                    switch (chall.getAction()) {
                        case ChallengeAction.DONE -> {
                            acceptedChallenge.put(challenge.getId(), challenge);
                            closeAllDialogs();
                            Platform.runLater(() -> {
                                try {
                                    App.setRoot(Screens.GAME_SCREEN);
                                } catch (IOException ex) {
                                }
                            });
                        }
                        case ChallengeAction.ERROR -> {
                            showError(challenge);
                        }
                    }
                }
        );
    }

    private void showError(Challenge challenge) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An Error Ocurred");
            alert.setHeaderText(challenge.getErrorMessage());
            alert.showAndWait();
            if (resetPlayerCard != null) {
                resetPlayerCard.run();
                resetPlayerCard = null;
            }
        });
    }

    private void closeAllDialogs() {
        Platform.runLater(() -> {
            openedDialogs.forEach((key, stage) -> {
                stage.close();
            });
            openedDialogs.clear();
            acceptedChallenge.clear();
        });
    }
}
