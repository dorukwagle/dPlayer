package com.doruk.dplayer.views;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;


public class PlayerView extends BorderPane {

    private Drawer drawer;
    private StackPane playerHolder;


    public PlayerView(ImageView mediaView, MenuBar menuBar, VideoControlPanel controlPanel) {
        setTop(menuBar);

        playerHolder = new StackPane();
        playerHolder.setAlignment(Pos.TOP_LEFT);
        setCenter(playerHolder);

        playerHolder.setBackground(Background.fill(Paint.valueOf("black")));
        drawer = new Drawer(playerHolder, 0.4f);

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
