package com.mycompany.clientside.client;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class EndGameVideo
{

    private static final String DRAW_VIDEO = "/com/mycompany/clientside/Videos/draw.mp4";
    private static final String WIN_VIDEO = "/com/mycompany/clientside/Videos/Cristiano Ronaldo SIUUU!!!.mp4";

    private static Media drawMedia;
    private static Media winMedia;

    private static MediaPlayer drawMediaPlayer;
    private static MediaPlayer winMediaPlayer;



    static
    {
        try {

            drawMedia = new Media(
                    EndGameVideo.class.getResource(DRAW_VIDEO).toExternalForm()
            );
            drawMediaPlayer = new MediaPlayer(drawMedia);
            drawMediaPlayer.setVolume(0.7);
            drawMediaPlayer.setAutoPlay(false);

            drawMediaPlayer.setOnReady(() ->
            {
                drawMediaPlayer.pause(); // Keep it ready but paused
            });

        } catch (Exception e) {
            System.err.println("Failed to load draw video: " + e.getMessage());
            drawMedia = null;
            drawMediaPlayer = null;
        }


        try
        {
            winMedia = new Media(
                    EndGameVideo.class.getResource(WIN_VIDEO).toExternalForm()
            );
            winMediaPlayer = new MediaPlayer(winMedia);
            winMediaPlayer.setVolume(0.7);
            winMediaPlayer.setAutoPlay(false);

            winMediaPlayer.setOnReady(() -> {
                winMediaPlayer.pause();
            });
        } catch (Exception e) {
            System.err.println("Failed to load win video: " + e.getMessage());
            winMedia = null;
            winMediaPlayer = null;
        }
    }


    public static void showEndGameVideo(String eventMessage, boolean isDraw) {
        Platform.runLater(() ->
        {
            try {
                MediaPlayer mediaPlayer = isDraw ? drawMediaPlayer : winMediaPlayer;

                if (mediaPlayer == null) {
                    System.err.println("Video media player not available");
                    return;
                }

                // Reset any media before to start new one
                mediaPlayer.stop();
                mediaPlayer.seek(mediaPlayer.getStartTime());

                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setPreserveRatio(true);
                mediaView.setSmooth(true);


                StackPane root = new StackPane(mediaView);
                root.setStyle("-fx-background-color: black;");
                Scene scene = new Scene(root, 800, 600);


                Stage videoStage = new Stage();
                videoStage.setTitle(eventMessage);
                videoStage.setScene(scene);
                videoStage.initStyle(StageStyle.DECORATED);

                // Set up event if the user close the stage
                videoStage.setOnCloseRequest(event ->
                {
                    mediaPlayer.stop();
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                });

                mediaPlayer.setOnEndOfMedia(() ->
                {
                    Platform.runLater(() -> {
                        mediaPlayer.stop();
                        mediaPlayer.seek(mediaPlayer.getStartTime());
                        videoStage.close();
                    });
                });

                mediaPlayer.setOnError(() ->
                {
                    System.err.println("Error playing video: " + mediaPlayer.getError());
                    videoStage.close();
                });

                videoStage.show();
                mediaPlayer.play();

            } catch (Exception e) {
                System.err.println("Failed to show end game video: " + e.getMessage());
            }
        });
    }
}