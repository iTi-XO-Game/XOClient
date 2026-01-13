/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.ClientManager;
import com.mycompany.clientside.client.EndGameVideo;
import com.mycompany.clientside.client.EndPoint;
import com.mycompany.clientside.client.JsonUtils;
import com.mycompany.clientside.models.ActiveGame;
import com.mycompany.clientside.models.ActiveGame.GameAction;
import com.mycompany.clientside.models.Challenge;
import com.mycompany.clientside.models.Move;
import com.mycompany.clientside.models.Player;
import com.mycompany.clientside.models.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Hossam
 */
public class PvpGameScreenController implements Initializable {

    private final char X = 'X';
    private final char O = 'O';

    @FXML
    private Button b00, b01, b02;
    @FXML
    private Button b10, b11, b12;
    @FXML
    private Button b20, b21, b22;

    private Button[][] board;
    private char currentPlayer = X;
    private char userPlayer = X;
    @FXML
    private Label scoreX;
    @FXML
    private Label scoreO;
    @FXML
    private Button restartButton;
    @FXML
    private Button exitGameButton;

    @FXML
    private VBox playerXCard;
    @FXML
    private VBox playerOCard;
    @FXML
    private Label turnXLabel;
    @FXML
    private Label turnOLabel;
    @FXML
    private Label playerXName;
    @FXML
    private Label playerOName;
    @FXML
    private Label waitOpponent;

    public static volatile Challenge challenge;
    private volatile ActiveGame activeGame = ActiveGame.DEFAULT;

    private final ClientManager clientManager = ClientManager.getInstance();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listenToGame();

        waitOpponent.setVisible(false);
        board = new Button[][]{
                {b00, b01, b02},
                {b10, b11, b12},
                {b20, b21, b22},};
        scoreX.setText("0"); // zero
        scoreO.setText("0"); // zero
        turnXLabel.setVisible(true);
        restartButton.setVisible(false);

    }

    private void listenToGame() {
        Player player = UserSession.currentPlayer;

        ActiveGame request = new ActiveGame();
        request.setId(challenge.getId());
        request.setAction(GameAction.LISTEN);
        request.setSender(player);
        request.setReceiver(challenge.getSender());
        request.makeSender();

        clientManager.sendListener(request, EndPoint.GAME,
                response -> {
                    ActiveGame game = JsonUtils.fromJson(response, ActiveGame.class);
                    Platform.runLater(() -> {
                        handleResponse(game);
                    });
                }
        );
    }

    private void handleResponse(ActiveGame game) {
        activeGame = game;
        switch (game.getAction()) {
            case GameAction.START -> {
                activeGame.setIsGameOn(true);
                resetGame(game);
            }
            case GameAction.STOP_LISTEN -> {
                activeGame.setIsGameOn(false);
                leaveGame();
            }
            case GameAction.RESTART -> {
                showAlert(
                        "Restart game?",
                        game.getSender().getUsername() + " wants to play again!",
                        "Accept",
                        "Leave to home",
                        () -> {
                            resetGame(activeGame);
                            activeGame.setAction(GameAction.START);
                            activeGame.makeSender();
                            clientManager.send(activeGame, EndPoint.GAME, response -> {
                            });
                        },
                        this::leaveGame
                );
            }
            case GameAction.MOVE -> {
                activeGame.setIsGameOn(true);

                Move latestMove = game.getLatestMove();
                Button btn = board[latestMove.getRow()][latestMove.getCol()];

                makeMove(btn, latestMove.getPlayer());
            }
            case GameAction.GIVE_UP -> {
                // show video
                showAlert(
                        "Game Over",
                        game.getErrorMessage(),
                        "Leave",
                        "",
                        this::leaveGame,
                        this::leaveGame
                );
            }
            case GameAction.ERROR -> {
                showAlert(
                        "An error occurred!",
                        game.getErrorMessage(),
                        "Leave",
                        "",
                        this::leaveGame,
                        this::leaveGame
                );
            }
        }
    }

    private void resetGame(ActiveGame game) {
        restartGame();

        String xName = game.getPlayerXid() == game.getSender().getId() ? game.getSender().getUsername() : game.getReceiver().getUsername();
        if (xName.equals(UserSession.getUsername())) {
            xName += " (You)";
        }
        playerXName.setText(xName);
        String oName = game.getPlayerOid() == game.getSender().getId() ? game.getSender().getUsername() : game.getReceiver().getUsername();
        if (oName.equals(UserSession.getUsername())) {
            oName += " (You)";
        }
        playerOName.setText(oName);

        waitOpponent.setVisible(false);
        userPlayer = game.getPlayerXid() == UserSession.getUserId() ? X : O;
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        if (makeMove(clicked, userPlayer)) {
            activeGame.setAction(GameAction.MOVE);
            activeGame.setIsGameOn(true);
            activeGame.makeSender();
            clientManager.send(activeGame, EndPoint.GAME,
                    ignored -> {

                    }
            );
        }
    }

    private boolean makeMove(Button clicked, char player) {

        if (!clicked.getText().isBlank() || player != currentPlayer) {
            return false;
        }

        activeGame.setIsGameOn(true);
        clicked.setText(currentPlayer + "");
        saveMove(clicked, currentPlayer);
        if (currentPlayer == X) {
            clicked.getStyleClass().add("x-text");
            playerXCard.getStyleClass().removeAll("current-player");
            playerOCard.getStyleClass().add("current-player");
            turnXLabel.setVisible(false);
            turnOLabel.setVisible(true);
        } else {
            clicked.getStyleClass().add("o-text");
            playerXCard.getStyleClass().add("current-player");
            playerOCard.getStyleClass().removeAll("current-player");
            turnXLabel.setVisible(true);
            turnOLabel.setVisible(false);
        }

        if (checkIfWinner()) {
            restartButton.setVisible(true);
            activeGame.setIsGameOn(false);
            playerXCard.getStyleClass().remove("current-player");
            playerOCard.getStyleClass().remove("current-player");
            turnXLabel.setVisible(false);
            turnOLabel.setVisible(false);
            if (currentPlayer == X) {
                int currentScore = Integer.parseInt(scoreX.getText());
                scoreX.setText(String.valueOf(currentScore + 1));
                activeGame.setWinnerId(activeGame.getPlayerXid());
            } else {
                int currentScore = Integer.parseInt(scoreO.getText());
                scoreO.setText(String.valueOf(currentScore + 1));
                activeGame.setWinnerId(activeGame.getPlayerOid());
            }
            forEachButton((btn) -> {
                btn.setDisable(true);
            });

            executor.submit(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                EndGameVideo.showEndGameVideo(currentPlayer + " Wins!", false);
            });
            return true;
        }

        if (isDraw()) {
            restartButton.setVisible(true);
            activeGame.setIsGameOn(false);
            activeGame.setWinnerId(-1);
            forEachButton((btn) -> {
                btn.setDisable(true);
            });
            playerXCard.getStyleClass().remove("current-player");
            playerOCard.getStyleClass().remove("current-player");
            turnXLabel.setVisible(false);
            turnOLabel.setVisible(false);

            executor.submit(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                EndGameVideo.showEndGameVideo("It is a Draw!", true);
            });
            return true;
        }

        currentPlayer = (currentPlayer == X) ? O : X;
        return true;
    }

    private void saveMove(Button clicked, char player) {
        int row, col = 0;
        outer:
        for (row = 0; row < 3; row++) {
            for (col = 0; col < 3; col++) {
                if (board[row][col] == clicked) {
                    break outer;
                }
            }
        }
        Move move = new Move(player, row, col);
        activeGame.setLatestMove(move);
    }

    private boolean isDraw() {
        for (Button[] row : board) {
            for (Button b : row) {
                if (b.getText().isBlank()) {

                    activeGame.setIsGameOn(false);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfWinner() {
        for (int i = 0; i < 3; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2])
                    || checkLine(board[0][i], board[1][i], board[2][i])) {
                return true;
            }
        }
        return checkLine(board[0][0], board[1][1], board[2][2])
                || checkLine(board[0][2], board[1][1], board[2][0]);
    }

    private boolean checkLine(Button a, Button b, Button c) {
        String temp = a.getText();

        if (!temp.isBlank() && temp.equals(b.getText()) && temp.equals(c.getText())) {
            a.getStyleClass().add("winning-cell");
            b.getStyleClass().add("winning-cell");
            c.getStyleClass().add("winning-cell");
            return true;
        }

        return false;
    }

    @FXML
    private void onRestartClick(ActionEvent event) {
        activeGame.setAction(GameAction.RESTART);
        activeGame.makeSender();
        waitOpponent.setVisible(true);
        restartButton.setDisable(true);
        clientManager.send(activeGame, EndPoint.GAME,
                response -> {
                }
        );
    }

    private void forEachButton(Consumer<Button> action) {
        for (Button[] row : board) {
            for (Button b : row) {
                action.accept(b);
            }
        }
    }

    private void restartGame() {

        restartButton.setVisible(false);
        restartButton.setDisable(false);
        playerXCard.getStyleClass().add("current-player");
        playerOCard.getStyleClass().remove("current-player");
        turnXLabel.setVisible(true);
        turnOLabel.setVisible(false);
        forEachButton((btn) -> {
            btn.setText("");
            btn.getStyleClass().remove("o-text");
            btn.getStyleClass().remove("x-text");
            btn.getStyleClass().remove("winning-cell");
            btn.setDisable(false);
        });
        currentPlayer = X;
    }

    @FXML
    private void onExitClick(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Leave Game?");
        alert.setHeaderText("Are you sure you want to Leave the game?");

        ButtonType buttonYes = new ButtonType("Leave");
        ButtonType buttonNo = new ButtonType("cancel");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonYes) {
            leaveGame();
        }
    }

    private void leaveGame() {
        activeGame.makeSender();
        if (activeGame.getIsGameOn()) {
            activeGame.setAction(GameAction.GIVE_UP);
            activeGame.setIsGameOn(false);
        } else {
            activeGame.setAction(GameAction.STOP_LISTEN);
        }
        clientManager.send(activeGame, EndPoint.GAME, responseJson -> {
        });
        clientManager.removeListener(EndPoint.GAME.getCode());
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ignored) {
        }
    }

    private void showAlert(
            String title,
            String description,
            String yesText,
            String noText,
            Runnable onYes,
            Runnable onNo
    ) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(description);

        ButtonType buttonYes = new ButtonType(yesText);

        if (!noText.isBlank()) {
            ButtonType buttonNo = new ButtonType(noText);
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
        } else {
            alert.getButtonTypes().setAll(buttonYes);
        }

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonYes) {
            onYes.run();
        } else if (!noText.isBlank()) {
            onNo.run();
        }
    }
}

