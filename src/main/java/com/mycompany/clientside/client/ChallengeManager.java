/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.client;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.controllers.PlayerRequestScreenController;
import com.mycompany.clientside.controllers.PvpGameScreenController;
import com.mycompany.clientside.models.Challenge;
import com.mycompany.clientside.models.Challenge.ChallengeAction;
import com.mycompany.clientside.models.Player;
import com.mycompany.clientside.models.UserSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

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

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private Runnable resetPlayerCard;
    private final Map<String, Stage> openedDialogs = new ConcurrentHashMap<>();
    private final Map<String, Challenge> pendingChallenges = new ConcurrentHashMap<>();

    public void listenToChallenges() {
        ClientManager clientManager = ClientManager.getInstance();

        Player player1 = UserSession.currentPlayer;

        Challenge request = new Challenge(
                "", ChallengeAction.LISTEN, player1, new Player(), ""
        );

        clientManager.sendListener(request, EndPoint.CHALLENGE,
                response -> {
                    
                    Challenge challenge = JsonUtils.fromJson(response, Challenge.class);

                    switch (challenge.getAction()) {
                        case ChallengeAction.SEND -> {
                            pendingChallenges.put(challenge.getId(), challenge);
                            Platform.runLater(() -> {
                                showChallengeReceived(challenge);
                            });
                        }
                        case ChallengeAction.ACCEPT -> {
                            closeDialog(challenge);
                            waitForGame(challenge);
                        }
                        case ChallengeAction.DECLINE, ChallengeAction.CANCEL -> {
                            closeDialog(challenge);
                        }
                        case ChallengeAction.ERROR -> {

                            showError(challenge);
                        }
                    }
                }
        );
    }

    private void closeDialog(Challenge challenge) {
        pendingChallenges.remove(challenge.getId());
        if (resetPlayerCard != null) {
            resetPlayerCard.run();
            resetPlayerCard = null;
        }
        Platform.runLater(() -> {
            Stage stage = openedDialogs.remove(challenge.getId());
            if (stage != null) {
                stage.close();
            }
        });
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

            stage.setTitle("Challenge Received!");
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

            stage.setOnCloseRequest(e -> {
                executor.submit(() -> {
                    openedDialogs.remove(challenge.getId());
                    if (pendingChallenges.remove(challenge.getId()) != null) {
                        declineChallenge(challenge);
                    }
                });
            });

            openedDialogs.put(challenge.getId(), stage);
            stage.show();

        } catch (IOException e) {

        }
    }

    public void sendChallenge(Player opponent, Runnable resetPlayerCard) {
        ClientManager clientManager = ClientManager.getInstance();

        this.resetPlayerCard = resetPlayerCard;
        Player player1 = UserSession.currentPlayer;
        Challenge request = new Challenge(
                UUID.randomUUID().toString(), ChallengeAction.SEND, player1, opponent, ""
        );

        pendingChallenges.put(request.getId(), request);
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

            stage.setTitle("Challenge sent!");
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> {
                executor.submit(() -> {
                    openedDialogs.remove(challenge.getId());
                    if (pendingChallenges.remove(challenge.getId()) != null) {
                        cancelChallenge(challenge);
                    }
                });
            });
            openedDialogs.put(challenge.getId(), stage);
            stage.show();

        } catch (IOException e) {

        }
    }

    private void cancelChallenge(Challenge challenge) {
        pendingChallenges.remove(challenge.getId());
        
        ClientManager clientManager = ClientManager.getInstance();

        if (resetPlayerCard != null) {
            resetPlayerCard.run();
            resetPlayerCard = null;
        }

        Player player = UserSession.currentPlayer;
        Challenge request = new Challenge(
                challenge.getId(), ChallengeAction.CANCEL, player, challenge.getReceiver(), ""
        );

        clientManager.send(request, EndPoint.CHALLENGE,
                ignored -> {
                }
        );
    }

    private void declineChallenge(Challenge challenge) {
        pendingChallenges.remove(challenge.getId());
        ClientManager clientManager = ClientManager.getInstance();

        Player player = UserSession.currentPlayer;
        Challenge request = new Challenge(
                challenge.getId(), ChallengeAction.DECLINE, player, challenge.getSender(), ""
        );

        clientManager.send(request, EndPoint.CHALLENGE,
                ignored -> {
                }
        );
    }

    private void waitForGame(Challenge challenge) {

        pendingChallenges.remove(challenge.getId());
        closeAllDialogs();

        PvpGameScreenController.challenge = challenge;
        Platform.runLater(() -> {
            try {
                App.setRoot(Screens.PVP_GAME_SCREEN);
            } catch (IOException ex) {
            }
        });
    }

    private void acceptChallenge(Challenge challenge) {
        pendingChallenges.remove(challenge.getId());
        closeAllDialogs();
        
        PvpGameScreenController.challenge = challenge;
        
        ClientManager clientManager = ClientManager.getInstance();

        Player player = UserSession.currentPlayer;
        Challenge request = new Challenge(
                challenge.getId(), ChallengeAction.ACCEPT, player, challenge.getSender(), ""
        );
        clientManager.send(request, EndPoint.CHALLENGE,
                response -> {
                    Challenge chall = JsonUtils.fromJson(response, Challenge.class);
                    switch (chall.getAction()) {
                        case ChallengeAction.DONE -> {
                            Platform.runLater(() -> {
                                try {
                                    App.setRoot(Screens.PVP_GAME_SCREEN);
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

    public void closeAllDialogs() {
        Platform.runLater(() -> {

            List<Stage> stagesToClose = new ArrayList<>(openedDialogs.values());

            for (Stage stage : stagesToClose) {
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }

        });
    }
}
