package com.doruk.dplayer.views;

import com.doruk.dplayer.contracts.MediaPlayerInterface;
import com.doruk.dplayer.mediaplayer.DMediaPlayer;
import com.doruk.dplayer.utilities.IconsProvider;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.concurrent.CompletableFuture;


public class PlayerView extends BorderPane {

    private MenuBar menuBar;
    private VideoControlPanel controlPanel;
    private Drawer drawer;

    private IconsProvider icons;
    private MediaPlayerInterface mediaPlayer;
    private ImageView mediaView;
    private StackPane playerHolder;
    private ImageView view;


    public PlayerView(Stage stage) {
        view = new ImageView();
        icons = new IconsProvider();
        menuBar = new MenuBar(icons);
        setTop(menuBar);

        playerHolder = new StackPane();
        playerHolder.setAlignment(Pos.TOP_LEFT);
        setCenter(playerHolder);

        playerHolder.setBackground(Background.fill(Paint.valueOf("black")));
        drawer = new Drawer(playerHolder, 0.25f);
        mediaPlayer = new DMediaPlayer();
        mediaView = mediaPlayer.getMediaView();
        playerHolder.getChildren().add(mediaView);
        StackPane.setAlignment(mediaView, Pos.CENTER);
        try {
            mediaPlayer.load("/home/doruk/Downloads/Video/test3.mkv");
//            mediaPlayer.load("/home/doruk/Downloads/Video/song.mp4");
//            mediaPlayer.load("/data/Movies/Gangster_(2022)_Hindi_Dubbed_720p.mp4");
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }

//        mediaPlayer.play();

        playerHolder.getChildren().add(drawer);

        controlPanel = new VideoControlPanel(icons);
        setBottom(controlPanel);

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
                float height = (float) (getHeight() - menuBar.getHeight() - controlPanel.getHeight());
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
}
