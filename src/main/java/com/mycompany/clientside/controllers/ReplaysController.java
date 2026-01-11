/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.clientside.controllers;

import com.mycompany.clientside.App;
import com.mycompany.clientside.Screens;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mycompany.clientside.models.GameRecord;
import com.mycompany.clientside.models.GamesWrapper;
import com.mycompany.clientside.models.Move;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class ReplaysController implements Initializable {

    @FXML
    private Button playVideoBtn;
    @FXML
    private Button delBtn;
    @FXML
    private VBox gamesContainer;
    @FXML
    private Label emptyLabel;

    private static final String RECORD_FILE = "games_record.json";
    private GamesWrapper wrapper;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRecordedGames();
    }

    private void updateEmptyState() {
        boolean isEmpty = gamesContainer.getChildren().isEmpty();

        emptyLabel.setText("No recorded games found.");
        emptyLabel.setVisible(isEmpty);
        emptyLabel.setManaged(isEmpty);
    }

    private void loadRecordedGames() {
        new Thread(this::loadRecordedGamesRunnable).start();
    }

    private void loadRecordedGamesRunnable() {
        File file = AIGameScreenController.getRecordFile();

        if (!file.exists()) {
            Platform.runLater(this::updateEmptyState);
            return;
        }
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            wrapper = gson.fromJson(reader, GamesWrapper.class);

            if (wrapper == null || wrapper.getGames().isEmpty()) {
                Platform.runLater(this::updateEmptyState);
                return;
            }

            Platform.runLater(() -> {
                for (GameRecord game : wrapper.getGames()) {
                    addGame(game);
                }
                updateEmptyState();
            });

        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error reading game records: " + e.getMessage());
            Platform.runLater(this::updateEmptyState);
        }
    }

    private void addGame(GameRecord game) {

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label date = new Label(game.getGameName());
        date.getStyleClass().add("small-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button playBtn = createIconButton(
                "Play",
                "/com/mycompany/clientside/images/play.png",
                "primary-btn"
        );

        Button deleteBtn = createIconButton(
                "Delete",
                "/com/mycompany/clientside/images/delete.png",
                "login-btn"
        );

        playBtn.setOnAction(e -> {
            playReplay(game);
        });

        deleteBtn.setOnAction(e -> {
            deleteGame(game, row);
        });

        row.getChildren().addAll(date, spacer, playBtn, deleteBtn);

        if (!gamesContainer.getChildren().isEmpty()) {
            gamesContainer.getChildren().add(new Separator());
        }
        gamesContainer.getChildren().add(row);

    }

    private void saveWrapperToFile() {

        File file = AIGameScreenController.getRecordFile();
        if (file == null) {
            System.err.println("Unable to access record file for saving");
            return;
        }

        if (wrapper == null || wrapper.getGames() == null) {
            System.err.println("Invalid wrapper data");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File tempFile = new File(file.getParent(), AIGameScreenController.TEMP_FILE_NAME);

        try (FileWriter writer = new FileWriter(tempFile)) {
            gson.toJson(wrapper, writer);

            if (file.exists() && !file.delete()) {
                System.err.println("Failed to delete old file");
                return;
            }

            if (!tempFile.renameTo(file)) {
                System.err.println("Failed to save file");
            }

        } catch (IOException e) {
            System.err.println("Error saving game records: " + e.getMessage());
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    private void playReplay(GameRecord game) {
        System.out.println(" replays controller Moves from game: " + game.getMoves().size());
        for (Move m : game.getMoves()) {
            System.out.println(m.getPlayer() + " -> " + m.getRow() + "," + m.getCol());
        }
        try {
            ReplayGameScreenController.setReplayMoves(game.getMoves());
            App.setRoot(Screens.REPLAY_GAME_SCREEN);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Button createIconButton(String text, String iconPath, String styleClass) {

        ImageView icon = null;

        try {
            icon = new ImageView(
                    new javafx.scene.image.Image(
                            getClass().getResourceAsStream(iconPath)
                    )
            );

            icon.setFitWidth(14);
            icon.setFitHeight(14);
            icon.setPreserveRatio(true);

        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        Label label = new Label(text);
        label.setTextFill(Color.WHITE);

        HBox content;
        if (icon != null) {
            content = new HBox(5, icon, label);
        } else {
            content = new HBox(label);
        }

        content.setAlignment(Pos.CENTER);

        Button button = new Button();
        button.setGraphic(content);
        button.getStyleClass().add(styleClass);

        return button;
    }

    private void deleteGame(GameRecord game, HBox row) {

        int index = gamesContainer.getChildren().indexOf(row);

        if (index + 1 < gamesContainer.getChildren().size()
                && gamesContainer.getChildren().get(index + 1) instanceof Separator) {

            gamesContainer.getChildren().remove(index + 1);

        } else if (index - 1 >= 0
                && gamesContainer.getChildren().get(index - 1) instanceof Separator) {

            gamesContainer.getChildren().remove(index - 1);
        }

        gamesContainer.getChildren().remove(row);

        wrapper.getGames().remove(game);
        saveWrapperToFile();
        updateEmptyState();

    }

    @FXML
    private void navigateBack(ActionEvent event) {
        try {
            App.setRoot(Screens.HOME_SCREEN);
        } catch (IOException ex) {
            // todo make an alert!
        }
    }

}
