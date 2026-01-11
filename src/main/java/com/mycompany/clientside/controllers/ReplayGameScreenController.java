/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.models.Move;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Dell
 */
public class ReplayGameScreenController {

    private final String X = "X";
    private final String O = "O";
    private static int difficulty;

    @FXML
    private Label npcLabel;

    @FXML
    private Button b00, b01, b02;
    @FXML
    private Button b10, b11, b12;
    @FXML
    private Button b20, b21, b22;

    private Button[][] board;
    private char[][] boardForAlgorithm;
    private String currentPlayer = X;
    @FXML
    private Label scoreX;
    @FXML
    private Label scoreO;
    @FXML
    private Button replayButton;
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

    private static List<Move> replayMoves;
    private Timeline replayTimeline;

    public static void setReplayMoves(List<Move> moves) {
        replayMoves = moves;
    }

    @FXML
    public void initialize() {

        board = new Button[][]{
            {b00, b01, b02},
            {b10, b11, b12},
            {b20, b21, b22}
        };

        setupBoard();
        startReplay();
    }

    private void setupBoard() {
        forEachButton(btn -> {
            btn.setText("");
            btn.setDisable(true);
            btn.getStyleClass().removeAll("x-text", "o-text", "winning-cell");
        });
    }

    private void updateTurnUI(String player) {

        playerXCard.getStyleClass().remove("current-player");
        playerOCard.getStyleClass().remove("current-player");
        if (player.equals(X)) {

            playerXCard.getStyleClass().add("current-player");
            playerOCard.getStyleClass().remove("current-player");

            turnXLabel.setVisible(true);
            turnOLabel.setVisible(false);
        } else {
            playerOCard.getStyleClass().add("current-player");
            playerXCard.getStyleClass().remove("current-player");

            turnOLabel.setVisible(true);
            turnXLabel.setVisible(false);
        }
    }

    @FXML
    private void handleMove(ActionEvent event) {

        Button clicked = (Button) event.getSource();

        if (!clicked.getText().isBlank()) {
            return;
        }

        clicked.setText(currentPlayer);

        clicked.getStyleClass().add(
                currentPlayer.equals(X) ? "x-text" : "o-text"
        );

        updateTurnUI(currentPlayer);

        if (checkIfWinner()) {
            playerXCard.getStyleClass().remove("current-player");
            playerOCard.getStyleClass().remove("current-player");
            turnXLabel.setVisible(false);
            turnOLabel.setVisible(false);
            if (currentPlayer.equals(X)) {
                int currentScore = Integer.parseInt(scoreX.getText());
                scoreX.setText(String.valueOf(currentScore + 1));
            } else {
                int currentScore = Integer.parseInt(scoreO.getText());
                scoreO.setText(String.valueOf(currentScore + 1));
            }
            forEachButton((btn) -> {
                btn.setDisable(true);
            });
            showEndGameAlert(currentPlayer + " Wins!");

            return;
        }

        if (isDraw()) {
            forEachButton((btn) -> {
                btn.setDisable(true);
            });
            playerXCard.getStyleClass().remove("current-player");
            playerOCard.getStyleClass().remove("current-player");
            turnXLabel.setVisible(false);
            turnOLabel.setVisible(false);
            showEndGameAlert("It is a Draw!");
            return;
        }

        currentPlayer = (currentPlayer.equals(X)) ? O : X;

    }

    private boolean isDraw() {
        for (Button[] row : board) {
            for (Button b : row) {
                if (b.getText().isBlank()) {
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

    private void showEndGameAlert(String header) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(header);
        alert.setContentText("Restart the game?");

        ButtonType restartBtn = new ButtonType("Restart");
        ButtonType cancelBtn = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(restartBtn, cancelBtn);

        Optional<ButtonType> result = alert.showAndWait();

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

    private void startReplay() {

        if (replayMoves == null || replayMoves.isEmpty()) {
            return;
        }

        if (replayTimeline != null) {
            replayTimeline.stop();
        }

        setupBoard();

        replayTimeline = new Timeline();
        final int[] index = {0};

        replayTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(800), e -> {

                    if (index[0] >= replayMoves.size()) {
                        replayTimeline.stop();

                        playerXCard.getStyleClass().remove("current-player");
                        playerOCard.getStyleClass().remove("current-player");
                        turnXLabel.setVisible(false);
                        turnOLabel.setVisible(false);
                        return;
                    }

                    Move move = replayMoves.get(index[0]);
                    Button btn = board[move.getRow()][move.getCol()];

                    btn.setText(move.getPlayer());
                    btn.getStyleClass().removeAll("x-text", "o-text");

                    if ("X".equals(move.getPlayer())) {
                        btn.getStyleClass().add("x-text");
                    } else {
                        btn.getStyleClass().add("o-text");
                    }
                    updateTurnUI(move.getPlayer());
                    index[0]++;
                })
        );

        replayTimeline.setCycleCount(replayMoves.size());
        playerXCard.getStyleClass().add("current-player");
        playerOCard.getStyleClass().remove("current-player");
        turnXLabel.setVisible(true);
        turnOLabel.setVisible(false);

        replayTimeline.playFromStart();
    }

    private void forEachButton(Consumer<Button> action) {
        for (Button[] row : board) {
            for (Button b : row) {
                action.accept(b);
            }
        }
    }

    @FXML
    private void onReplayClick(ActionEvent event) {
        startReplay();
    }

    @FXML
    private void onExitClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Leave Replay Game?");
        alert.setHeaderText("Are you sure you want to Leave this replay game?");

        ButtonType buttonYes = new ButtonType("Leave");
        ButtonType buttonNo = new ButtonType("cancel");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonYes) {

            try {
                App.setRoot(Screens.REPLAYS_SCREEN);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
