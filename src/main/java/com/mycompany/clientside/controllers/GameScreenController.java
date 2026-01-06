/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.EndGameVideo;
import com.mycompany.clientside.models.Move;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
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
 * @author lenovo
 */
public class GameScreenController implements Initializable {

    private final String X = "X";
    private final String O = "O";

    @FXML
    private Button b00, b01, b02;
    @FXML
    private Button b10, b11, b12;
    @FXML
    private Button b20, b21, b22;

    private Button[][] board;
    private String currentPlayer = X;
    @FXML
    private Label scoreX;
    @FXML
    private Label scoreO;
    @FXML
    private Button restartButton;
    @FXML
    private Button exitGameButton;

    private final ArrayList<Move> gameMoves = new ArrayList<>();
    @FXML
    private VBox playerXCard;
    @FXML
    private VBox playerOCard;
    @FXML
    private Label turnXLabel;
    @FXML
    private Label turnOLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        board = new Button[][]{
            {b00, b01, b02},
            {b10, b11, b12},
            {b20, b21, b22},};
        scoreX.setText("0"); // zero
        scoreO.setText("0"); // zero
        playerXCard.getStyleClass().add("current-player");
        turnXLabel.setVisible(true);
    }

    @FXML
    private void handleMove(ActionEvent event) {

        Button clicked = (Button) event.getSource();

        if (!clicked.getText().isBlank()) {
            return;
        }

        clicked.setText(currentPlayer);
        saveMove(clicked, currentPlayer);
        if (currentPlayer.equals(X)) {
            clicked.getStyleClass().add("x-text");
            playerXCard.getStyleClass().remove("current-player");
            playerOCard.getStyleClass().add("current-player");
            turnXLabel.setVisible(false);
            turnOLabel.setVisible(true);
        } else {
            clicked.getStyleClass().add("o-text");
            playerXCard.getStyleClass().add("current-player");
            playerOCard.getStyleClass().remove("current-player");
            turnXLabel.setVisible(true);
            turnOLabel.setVisible(false);
        }

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

            Platform.runLater(()->
            {
                EndGameVideo.showEndGameVideo(currentPlayer + " Wins!",false);
                restartGame();
            });
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
            Platform.runLater(()->
            {
                EndGameVideo.showEndGameVideo("It is a Draw!",true);
                restartGame();
            });

            return;
        }

        currentPlayer = (currentPlayer.equals(X)) ? O : X;
    }

    private void saveMove(Button clicked, String player) {
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
        gameMoves.add(move);
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
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Restart Game?");
        alert.setHeaderText("Are you sure you want to restart the game?");

        ButtonType buttonYes = new ButtonType("confirm");
        ButtonType buttonNo = new ButtonType("cancel");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonYes) {
            restartGame();
        }
    }

    private void forEachButton(Consumer<Button> action) {
        for (Button[] row : board) {
            for (Button b : row) {
                action.accept(b);
            }
        }
    }

    private void restartGame() {
        gameMoves.clear();
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

            try {
                App.setRoot(Screens.HOME_SCREEN);
            } catch (IOException ex) {
                // todo show alert
            }
        }
    }
}
