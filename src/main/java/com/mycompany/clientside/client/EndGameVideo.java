package com.mycompany.clientside.client;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EndGameVideo {

//    private static final String DRAW_VIDEO = "/com/mycompany/clientside/Videos/draw.mp4";
//    private static final String WIN_VIDEO = "/com/mycompany/clientside/Videos/Cristiano Ronaldo SIUUU!!!.mp4";
//
//    private static Media drawMedia;
//    private static Media winMedia;
//
//    private static MediaPlayer drawMediaPlayer;
//    private static MediaPlayer winMediaPlayer;
//
//    private static boolean drawVideoReady = false;
//    private static boolean winVideoReady = false;
//
//    static {
//        try {
//            drawMedia = new Media( EndGameVideo.class.getResource(DRAW_VIDEO).toExternalForm());
//
//            drawMediaPlayer = new MediaPlayer(drawMedia);
//            drawMediaPlayer.setVolume(0.7);
//            drawMediaPlayer.setAutoPlay(false);
//
//            drawMediaPlayer.setOnReady(() ->
//            {
//                drawVideoReady = true;
//                drawMediaPlayer.pause();
//                drawMediaPlayer.seek(drawMediaPlayer.getStartTime());
//            });
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            drawMedia = null;
//            drawMediaPlayer = null;
//        }
//
//        try {
//            winMedia = new Media(EndGameVideo.class.getResource(WIN_VIDEO).toExternalForm());
//            winMediaPlayer = new MediaPlayer(winMedia);
//            winMediaPlayer.setVolume(0.7);
//            winMediaPlayer.setAutoPlay(false);
//
//            winMediaPlayer.setOnReady(() ->
//            {
//                System.out.println("Win video is ready!");
//                winVideoReady = true;
//                winMediaPlayer.pause();
//                winMediaPlayer.seek(winMediaPlayer.getStartTime());
//            });
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            winMedia = null;
//            winMediaPlayer = null;
//        }
//    }

    public static void showEndGameVideo(String eventMessage, boolean isDraw)
    {
//        Platform.runLater(() -> {
//            try {
//                MediaPlayer mediaPlayer = isDraw ? drawMediaPlayer : winMediaPlayer;
//                boolean isReady = isDraw ? drawVideoReady : winVideoReady;
//
//                if (mediaPlayer == null) {
//                    return;
//                }
//
//                if (!isReady)
//                {
//                    System.out.println("Video not ready yet, waiting...");
//                    // Wait for video to be ready
//                    mediaPlayer.setOnReady(() ->
//                    {
//                        System.out.println("Video ready now, showing...");
//                        if (isDraw) {
//                            drawVideoReady = true;
//                        } else {
//                            winVideoReady = true;
//                        }
//                        showVideoWindow(eventMessage, mediaPlayer);
//                    });
//                    return;
//                }
//
//                showVideoWindow(eventMessage, mediaPlayer);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    private static void showVideoWindow(String eventMessage, MediaPlayer mediaPlayer)
    {
//        mediaPlayer.stop();
//        mediaPlayer.seek(mediaPlayer.getStartTime());
//
//        MediaView mediaView = new MediaView(mediaPlayer);
//        mediaView.setPreserveRatio(true);
//        mediaView.setSmooth(true);
//
//        StackPane root = new StackPane(mediaView);
//        root.setStyle("-fx-background-color: black;");
//        Scene scene = new Scene(root, 800, 600);
//
//        Stage videoStage = new Stage();
//        videoStage.setTitle(eventMessage);
//        videoStage.setScene(scene);
//        videoStage.initStyle(StageStyle.DECORATED);
//
//        mediaView.fitWidthProperty().bind(scene.widthProperty());
//        mediaView.fitHeightProperty().bind(scene.heightProperty());
//
//        videoStage.setOnCloseRequest(event ->
//        {
//            mediaPlayer.stop();
//            mediaPlayer.seek(mediaPlayer.getStartTime());
//        });
//
//        mediaPlayer.setOnEndOfMedia(() ->
//        {
//            Platform.runLater(() -> {
//                mediaPlayer.stop();
//                mediaPlayer.seek(mediaPlayer.getStartTime());
//                videoStage.close();
//            });
//        });
//
//        mediaPlayer.setOnError(() -> {
//            videoStage.close();
//        });
//
//        videoStage.show();
////        Platform.runLater(() -> {
//            mediaPlayer.play();
//            System.out.println("Video playing...");
////        });
    }
}