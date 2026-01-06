package com.mycompany.clientside.client;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class EndGameVideo
{
    public static void showEndGameVideo(String eventMessage, boolean isdraw)
    {
        String videoPath;

        if (isdraw)
            videoPath = "draw.mp4";
        else
            videoPath = "win.mp4";
        File file = new File(videoPath);

        Media media = new Media(EndGameVideo.class.getResource("/com/mycompany/clientside/Videos/" + videoPath).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        StackPane root = new StackPane(mediaView);
        Scene scene = new Scene(root, 800, 600);

        Stage videoStage = new Stage();
        videoStage.setTitle(eventMessage);
        videoStage.setScene(scene);
        videoStage.show();

        mediaPlayer.setOnReady(mediaPlayer::play);


        videoStage.setOnCloseRequest(event ->
        {
            mediaPlayer.stop();
        });
        mediaPlayer.setOnEndOfMedia(() ->
        {
            mediaPlayer.stop();
            videoStage.close();
        });
    }
}
