/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import com.mycompany.clientside.client.EndGameVideo;
import com.mycompany.clientside.models.GameRecord;
import com.mycompany.clientside.models.GamesWrapper;
import com.mycompany.clientside.models.Move;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Depogramming
 */
public class AIGameScreenController implements Initializable {

    private final String X = "X";
    private final String O = "O";
    private static int difficulty;

    private static final String APP_DIR_NAME = "TicTacToeReplays";
    public static final String RECORD_FILE_NAME = "games_record.json";
    public static final String TEMP_FILE_NAME = "games_record.tmp";
    private static volatile boolean recordMode = false;

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

    private boolean isAiMove = false;
    @FXML
    private Label recordingLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        recordingLabel.setVisible(recordMode);

        switch (difficulty) {
            case 0 ->
                npcLabel.setText("Easy NPC");
            case 1 ->
                npcLabel.setText("Medium NPC");
            case 2 ->
                npcLabel.setText("Undefeatable");
        }
        board = new Button[][]{
            {b00, b01, b02},
            {b10, b11, b12},
            {b20, b21, b22},};
        boardForAlgorithm = new char[][]{
            {'_', '_', '_'},
            {'_', '_', '_'},
            {'_', '_', '_'},};

        scoreX.setText("0"); // zero
        scoreO.setText("0"); // zero
        playerXCard.getStyleClass().add("current-player");
        turnXLabel.setVisible(true);

    }

    public static void setDifficulty(int difficulty) {
        AIGameScreenController.difficulty = difficulty;
    }

    public static void setRecordMode(boolean record) {
        recordMode = record;
    }

    private String generateGameName() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }

    @FXML
    private void handleMove(ActionEvent event) {

        Button clicked = (Button) event.getSource();

        if (isAiMove || !clicked.getText().isBlank()) {
            return;
        }
        isAiMove = true;

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
//            Platform.runLater(()->
//            {
                EndGameVideo.showEndGameVideo(currentPlayer + " Wins!",false);
                //restartGame();
//            });
            saveGameToFile();
            //showEndGameAlert(currentPlayer + " Wins!");

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
//            Platform.runLater(()->
//            {
                EndGameVideo.showEndGameVideo("It is a Draw!",true);
                //restartGame();
//            });
            saveGameToFile();
            //showEndGameAlert("It is a Draw!");
            return;
        }

        currentPlayer = (currentPlayer.equals(X)) ? O : X;

        handleNPCMove();
        //here, will just handle the ai logic..
        // but first, I need to sout the current difficulty level (done)
        //1. call a function to initiate the minmax logic, it should recieve a 3*3 matrix or something
        //2. after getting the output from that function, print O on the specified output and do hossam's logic
        //3. switch the current player to O
    }

    private void handleNPCMove() {

        new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
            Platform.runLater(this::handleNPCMoveRunnable);
        }).start();
    }

    private void handleNPCMoveRunnable() {
        Move aiMove;
        switch (difficulty) {
            case 2:
                aiMove = findBestMove(boardForAlgorithm, currentPlayer.charAt(0), 9);
                break;
            case 1:
                aiMove = findBestMove(boardForAlgorithm, currentPlayer.charAt(0), 2);
                break;
            default:
                List<int[]> availableMoves = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (boardForAlgorithm[i][j] == '_') {
                            availableMoves.add(new int[]{i, j});
                        }
                    }
                }
                Random random = new Random();
                int[] choice = availableMoves.get(random.nextInt(availableMoves.size()));
                aiMove = new Move(currentPlayer, choice[0], choice[1]);
                break;
        }
        Button clicked = board[aiMove.getRow()][aiMove.getCol()];
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
//            Platform.runLater(()->
//            {
                EndGameVideo.showEndGameVideo(currentPlayer + " Wins!",false);
                //restartGame();
//            });
            saveGameToFile();
            //showEndGameAlert(currentPlayer + " Wins!");

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
//            Platform.runLater(()->
//            {
                EndGameVideo.showEndGameVideo("It is a Draw!",true);
                //restartGame();
//            });
            saveGameToFile();
            //showEndGameAlert("It is a Draw!");
            return;
        }

        currentPlayer = (currentPlayer.equals(X)) ? O : X;

        isAiMove = false;
    }

    private void saveMove(Button clicked, String player) {
        int row, col = 0;
        outer:
        for (row = 0; row < 3; row++) {
            for (col = 0; col < 3; col++) {
                if (board[row][col] == clicked) {
                    boardForAlgorithm[row][col] = player.charAt(0);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
        isAiMove = false;
        forEachButton((btn) -> {
            btn.setText("");
            btn.getStyleClass().remove("o-text");
            btn.getStyleClass().remove("x-text");
            btn.getStyleClass().remove("winning-cell");
            btn.setDisable(false);
        });
        boardForAlgorithm = new char[][]{
            {'_', '_', '_'},
            {'_', '_', '_'},
            {'_', '_', '_'},};
        currentPlayer = X;
    }

    @FXML
    private void onExitClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
                ex.printStackTrace();
            }
        }
    }

    private Move findBestMove(char board[][], char player, int h) {
        int bestVal = -1000;

        int bestMoveRow = -1;
        int bestMoveCol = -1;

        // Traverse all cells, evaluate minimax function 
        // for all empty cells. And return the cell 
        // with optimal value.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if cell is empty
                if (board[i][j] == '_') {
                    // Make the move
                    board[i][j] = player;

                    // compute evaluation function for this
                    // move.
                    int moveVal = minimax(board, 0, false, player, h);

                    // Undo the move
                    board[i][j] = '_';

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal) {
                        bestMoveRow = i;
                        bestMoveCol = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return new Move(player + "", bestMoveRow, bestMoveCol);
    }

    private int minimax(char[][] board, int depth, boolean isMax, char aiPlayer, int h) {

        char opponent = (aiPlayer == 'X') ? 'O' : 'X';
        int score = evaluate(board, aiPlayer);

        if (score == 10) {
            return score;
        }
        if (score == -10) {
            return score;
        }

        if (isFull(board)) {
            return 0;
        }
        if (h == depth) {
            return score;
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = aiPlayer;
                        best = Math.max(best,
                                minimax(board, depth + 1, !isMax, aiPlayer, h));
                        board[i][j] = '_';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = opponent;
                        best = Math.min(best,
                                minimax(board, depth + 1, !isMax, aiPlayer, h));
                        board[i][j] = '_';
                    }
                }
            }
            return best;
        }
    }

    private int evaluate(char[][] b, char aiPlayer) {

        // check rows
        for (int row = 0; row < 3; row++) {
            if (b[row][0] != '_'
                    && b[row][0] == b[row][1]
                    && b[row][1] == b[row][2]) {

                return (b[row][0] == aiPlayer) ? 10 : -10;
            }
        }

        // check columns
        for (int col = 0; col < 3; col++) {
            if (b[0][col] != '_'
                    && b[0][col] == b[1][col]
                    && b[1][col] == b[2][col]) {

                return (b[0][col] == aiPlayer) ? 10 : -10;
            }
        }

        // check diagonal
        if (b[0][0] != '_'
                && b[0][0] == b[1][1]
                && b[1][1] == b[2][2]) {

            return (b[0][0] == aiPlayer) ? 10 : -10;
        }

        // check second diagonal
        if (b[0][2] != '_'
                && b[0][2] == b[1][1]
                && b[1][1] == b[2][0]) {

            return (b[0][2] == aiPlayer) ? 10 : -10;
        }

        // draw...
        return 0;
    }

    private boolean isFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '_') {
                    return false;
                }
            }
        }
        return true;
    }

    private void saveGameToFile() {

        new Thread(this::saveGameToFileRunnable).start();

    }

    private void saveGameToFileRunnable() {
        if (!recordMode) {
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GamesWrapper wrapper;

        File file = getRecordFile();
        if (file == null) {
            System.err.println("Unable to access record file");
            return;
        }

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                wrapper = gson.fromJson(reader, GamesWrapper.class);
            } catch (JsonSyntaxException | IOException e) {
                System.err.println("Unable to read record file");
                wrapper = new GamesWrapper();
            }
        } else {
            wrapper = new GamesWrapper();
        }

        String gameName = generateGameName();

        String diff;
        diff = switch (difficulty) {
            case 0 ->
                "Easy";
            case 1 ->
                "Medium";
            case 2 ->
                "Hard";
            default ->
                "Unknown";
        };

        GameRecord record = new GameRecord(
                gameName,
                diff,
                new ArrayList<>(gameMoves)
        );

        wrapper.getGames().add(record);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(wrapper, writer);
        } catch (IOException e) {
            System.err.println("Failed to save game record: " + e.getMessage());
        }
    }

    public static File getRecordFile() {
        String userHome = System.getProperty("user.home");

        File appDir = new File(userHome, APP_DIR_NAME);

        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                System.err.println("Failed to create application directory: " + appDir.getAbsolutePath());
                return null;
            }
        }

        return new File(appDir, RECORD_FILE_NAME);
    }
}
