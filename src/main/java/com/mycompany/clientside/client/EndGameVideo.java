package com.mycompany.clientside.client;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class EndGameVideo {

    private static final String DRAW_VIDEO = "/com/mycompany/clientside/Videos/draw.mp4";
    private static final String WIN_VIDEO = "/com/mycompany/clientside/Videos/win.mp4";
    private static final String EXTERNAL_VIDEO_DIR = "videos/";

    private static Media drawMedia;
    private static Media winMedia;

    private static MediaPlayer nextDrawPlayer;
    private static MediaPlayer nextWinPlayer;

    static {
        //This will not block to open new session
        drawMedia = loadMedia(true);
        winMedia = loadMedia(false);

        //This can be block but we creat each one new mediaPlayer to avoid this problem
        prepareNextPlayer(true);
        prepareNextPlayer(false);
    }

    private static void prepareNextPlayer(boolean isDraw) {
        Platform.runLater(() -> {
            Media media = isDraw ? drawMedia : winMedia;
            if (media == null) return;

            MediaPlayer player = new MediaPlayer(media);
            player.setMute(true);

            //I run the video here in background so it can cash and be ready to start any time
            player.setOnReady(() -> {
                player.play();
                player.pause(); // this is the last state that must to be the mediaPlay on
                player.setMute(false);
                player.setVolume(0.7);
                if (isDraw)
                    nextDrawPlayer = player;
                else
                    nextWinPlayer = player;

                System.out.println(" Video Cached & Ready");
            });
        });
    }

    public static void showEndGameVideo(String eventMessage, boolean isDraw) {
        Platform.runLater(() -> {
            MediaPlayer readyPlayer = isDraw ? nextDrawPlayer : nextWinPlayer;

            // In case it doesn't work after all of these
            if (readyPlayer == null || readyPlayer.getStatus() != MediaPlayer.Status.PAUSED) {
                System.out.println("Media player still Can't cash");
                Media media = isDraw ? drawMedia : winMedia;
                if (media != null) {
                    MediaPlayer mediaPlayer2 = new MediaPlayer(media);
                    mediaPlayer2.setOnReady(() -> display(eventMessage, mediaPlayer2, isDraw));
                }
                return;
            }

            display(eventMessage, readyPlayer, isDraw);
        });
    }

    private static void display(String title, MediaPlayer player, boolean isDraw) {
        MediaView mediaView = new MediaView(player);

        mediaView.setSmooth(true);
        mediaView.setCache(true);
        mediaView.setCacheHint(javafx.scene.CacheHint.SPEED);

        StackPane root = new StackPane(mediaView);
        root.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);

        mediaView.fitWidthProperty().bind(scene.widthProperty());
        mediaView.fitHeightProperty().bind(scene.heightProperty());

        stage.setOnCloseRequest(e -> {
            player.stop();
            player.dispose();
            prepareNextPlayer(isDraw);
        });

        player.setOnEndOfMedia(() -> {
            player.stop();
            player.dispose();
            prepareNextPlayer(isDraw);
            Platform.runLater(stage::close);
        });

        stage.show();
        player.play();
    }

    private static Media loadMedia(boolean isDraw) {
        String url = getVideoUrl(isDraw);
        return (url != null) ? new Media(url) : null;
    }

    private static String getVideoUrl(boolean isDraw) {
        String fileName = isDraw ? "draw.mp4" : "win.mp4";

        File externalFile = new File(EXTERNAL_VIDEO_DIR + fileName);
        if (externalFile.exists()) return externalFile.toURI().toString();

        //If the External had a problem return to get the path from internal
        var res = EndGameVideo.class.getResource(isDraw ? DRAW_VIDEO : WIN_VIDEO);
        return (res != null) ? res.toExternalForm() : null;
    }
}
