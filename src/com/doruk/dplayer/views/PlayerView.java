package com.doruk.dplayer.views;

import com.sun.jna.platform.win32.COM.Wbemcli;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;


public class PlayerView extends StackPane {

    private Drawer drawer;
    private StackPane playerHolder;


    public PlayerView(ImageView mediaView, MenuBar menuBar, VideoControlPanel controlPanel) {
        BorderPane top = new BorderPane();
        top.setMaxHeight(top.getHeight());
        top.setTop(menuBar);

        playerHolder = new StackPane();
        playerHolder.setAlignment(Pos.TOP_LEFT);

        playerHolder.setBackground(Background.fill(Paint.valueOf("black")));
        drawer = new Drawer(playerHolder, 0.6f);

        playerHolder.getChildren().add(mediaView);
        StackPane.setAlignment(mediaView, Pos.CENTER);

        playerHolder.getChildren().add(drawer);
        BorderPane center = new BorderPane();
        center.setCenter(playerHolder);

        BorderPane bottom = new BorderPane();
        bottom.setMaxHeight(controlPanel.getHeight());
        bottom.setBottom(controlPanel);

        getChildren().addAll(center, top, bottom);

        setAlignment(center, Pos.CENTER);
        setAlignment(top, Pos.TOP_CENTER);
        setAlignment(bottom, Pos.BOTTOM_CENTER);
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public StackPane getPlayerHolder() {
        return playerHolder;
    }
}
