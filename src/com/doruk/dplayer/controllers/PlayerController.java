package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.contracts.MediaPlayerInterface;
import com.doruk.dplayer.mediaplayer.DMediaPlayer;
import com.doruk.dplayer.utilities.IconsProvider;
import com.doruk.dplayer.views.Drawer;
import com.doruk.dplayer.views.MenuBar;
import com.doruk.dplayer.views.PlayerView;
import com.doruk.dplayer.views.VideoControlPanel;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

import java.awt.*;
import javafx.scene.control.Menu;
import java.util.concurrent.CompletableFuture;

public class PlayerController implements Controllers {
    private MediaPlayerInterface mediaPlayer;
    private ImageView mediaView;
    private Drawer drawer;
    private PlayerView playerView;
    private VideoControlPanel controlPanel;
    private MenuBar menuBar;
    private Scene scene;

    public PlayerController(){
        IconsProvider icons = new IconsProvider();
        mediaPlayer = new DMediaPlayer();
        mediaView = mediaPlayer.getMediaView();
        menuBar = new MenuBar(icons);
        controlPanel = new VideoControlPanel(icons);
        playerView = new PlayerView(mediaView, menuBar, controlPanel);
        scene = new Scene(playerView);

//        try {
//            mediaPlayer.load("/home/doruk/Downloads/Video/test3.mkv");
////            mediaPlayer.load("/home/doruk/Downloads/Video/song.mp4");
////            mediaPlayer.load("/data/Movies/Gangster_(2022)_Hindi_Dubbed_720p.mp4");
//        } catch (FileNotFoundException e) {
//            System.out.println(e.toString());
//        }

        controlPanel.getDrawerBtn().setOnAction(e -> {
            var check = drawer.isHidden();
            if (!check) {
                drawer.hide();
            } else {
                drawer.show();
            }
        });

        CompletableFuture.runAsync(()->{
            try {
                Thread.sleep(500);
                float height = (float) (playerView.getHeight() - menuBar.getHeight() - controlPanel.getHeight());
                mediaPlayer.setHeight(height);
//                Thread.sleep(5000);
//                Platform.runLater(() -> {
//                    stage.setFullScreen(true);
//                });

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public Scene getScene(){
        return scene;
    }

    public Button getNext(){
        return null;
    }

    public Menu getPreferenceButton(){
        return menuBar.getPreference();
    }
}
