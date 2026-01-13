package com.mycompany.clientside.client;

import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class EndGameVideo {

    private static final String DRAW_VIDEO = "/com/mycompany/clientside/Videos/draw.mp4";
    private static final String WIN_VIDEO = "/com/mycompany/clientside/Videos/win.mp4";
    private static final String EXTERNAL_VIDEO_DIR = "videos/";

    // Only Media objects are static (lightweight, reusable, no locks)
    private static final Media drawMedia;
    private static final Media winMedia;

    static {
        drawMedia = loadMedia(true);
        winMedia = loadMedia(false);
    }

    private static int count = 0;

    public static void showEndGameVideo(String eventMessage, boolean isDraw) {
        Platform.runLater(() -> {
            Media media = isDraw ? drawMedia : winMedia;

            if (media == null) {
                return;
            }

            // Create a NEW MediaPlayer instance (no static, no locks)
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(0.7);
            player.setAutoPlay(false);

            // Buffer the video before displaying
            player.setOnReady(() -> bufferVideo(eventMessage, player,isDraw));

            player.setOnError(() -> {
                System.err.println("MediaPlayer error: " + player.getError());
                player.dispose();
                if (count < 3) {
                    count++;
                    showEndGameVideo(eventMessage, isDraw);
                }
            });
        });
    }


    private static void bufferVideo(String title, MediaPlayer player, boolean isDraw) {
        try {
            if (player.getStatus() ==  MediaPlayer.Status.DISPOSED )
            {
                showEndGameVideo(title,isDraw);

                return;
            }

            player.setMute(true);
            player.play();

            // After a short delay, pause and prepare for actual display
            new Thread(() -> {
                try {
                    // Let it buffer for a moment
                    Thread.sleep(200); //koko

                    Platform.runLater(() -> {
                        player.pause();
                        player.seek(Duration.ZERO);
                        player.setMute(false);

                        // Now we are ready to show the video
                        display(title, player);
                    });

                } catch (InterruptedException e) {

                    display(title, player); // Display anyway no risk no fun
                }
            }).start();

        } catch (Exception e) {

            //e.printStackTrace();
            display(title, player);
        }
    }

    private static void display(String title, MediaPlayer player) {
        Platform.runLater(() -> {
            try {

                // Create MediaView
                MediaView mediaView = new MediaView(player);
                mediaView.setSmooth(true);
                mediaView.setPreserveRatio(true);
                mediaView.setCache(true);
                mediaView.setCacheHint(javafx.scene.CacheHint.SPEED);

                // Create layout
                StackPane root = new StackPane(mediaView);
                root.setStyle("-fx-background-color: black;");

                Scene scene = new Scene(root, 800, 600);
                Stage stage = new Stage();
                stage.setTitle(title);
                stage.setScene(scene);

                // Bind MediaView size to scene
                mediaView.setCache(true);
                mediaView.setCacheHint(CacheHint.SPEED);
                mediaView.fitWidthProperty().bind(scene.widthProperty());
                mediaView.fitHeightProperty().bind(scene.heightProperty());


                Runnable cleanup = () -> {
                    Platform.runLater(() -> {


                        if (player.getStatus() != MediaPlayer.Status.DISPOSED) {
                            player.stop();
                            player.dispose(); //Frees all resources
                        }
                        if (stage.isShowing()) {
                            stage.close();
                        }

                    });
                };

                stage.setOnCloseRequest(e -> cleanup.run());
                player.setOnEndOfMedia(cleanup);
                player.setOnError(cleanup);


                // Show and play
                stage.show();
                player.seek(Duration.ZERO);
                player.play();

                count = 0;

            } catch (Exception e) {

                player.dispose();
            }
        });
    }

    private static Media loadMedia(boolean isDraw) {
        try {
            String url = getVideoUrl(isDraw);
            if (url != null) {

                return new Media(url);
            }
        } catch (Exception e) {

        }
        return null;
    }


    private static String getVideoUrl(boolean isDraw) {
        String fileName = isDraw ? "draw.mp4" : "win.mp4";

        File externalFile = new File(EXTERNAL_VIDEO_DIR + fileName);
        if (externalFile.exists()) {
            return externalFile.toURI().toString();
        }

        var resource = EndGameVideo.class.getResource(isDraw ? DRAW_VIDEO : WIN_VIDEO);
        if (resource != null) {
            return resource.toExternalForm();
        }
        return null;
    }

}