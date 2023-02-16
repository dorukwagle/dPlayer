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

    private Drawer drawer;
    private StackPane playerHolder;


    public PlayerView(ImageView mediaView, MenuBar menuBar, VideoControlPanel controlPanel) {
        setTop(menuBar);

        playerHolder = new StackPane();
        playerHolder.setAlignment(Pos.TOP_LEFT);
        setCenter(playerHolder);

        playerHolder.setBackground(Background.fill(Paint.valueOf("black")));
        drawer = new Drawer(playerHolder, 0.25f);

        playerHolder.getChildren().add(mediaView);
        StackPane.setAlignment(mediaView, Pos.CENTER);


        playerHolder.getChildren().add(drawer);
        setBottom(controlPanel);
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public StackPane getPlayerHolder() {
        return playerHolder;
    }
}
