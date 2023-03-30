package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.ResourceProvider;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.util.concurrent.CompletableFuture;


public class PlayerView extends StackPane {

    private Drawer drawer;
    private StackPane playerHolder;
    private Text popupLabel;
    private Popup popup;

    public PlayerView(ImageView mediaView, MenuBar menuBar, VideoControlPanel controlPanel, ResourceProvider icons) {
        var dummyControl = new VideoControlPanel(icons);
        var dummyMenu = new MenuBar(icons);

        BorderPane playerLay = new BorderPane();
        BorderPane barLay = new BorderPane();
        menuBar.setMaxHeight(0);
        controlPanel.setMaxHeight(0);
        barLay.setTop(menuBar);
        barLay.setBottom(controlPanel);

        popup = new Popup();
        popupLabel = new Text();
        popupLabel.setFill(Paint.valueOf("white"));
        popupLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));
        popup.getContent().add(popupLabel);

        playerLay.setTop(dummyMenu);


        playerHolder = new StackPane();
        playerHolder.setBackground(Background.fill(Paint.valueOf("black")));
        StackPane.setAlignment(mediaView, Pos.CENTER);
        playerHolder.getChildren().add(mediaView);

        StackPane drawerHolder = new StackPane();
        drawer = new Drawer(drawerHolder, 0.6f);
        StackPane.setAlignment(drawer, Pos.TOP_LEFT);
        drawerHolder.getChildren().add(drawer);
        barLay.setCenter(drawerHolder);

        playerLay.setCenter(playerHolder);

        playerLay.setBottom(dummyControl);
        getChildren().addAll(playerLay, barLay);

    }

    public void showPopup(String text){
        hidePopup();
        popupLabel.setText(text);
        popup.show(this, this.getWidth()/2, this.getHeight()/4);
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
                Platform.runLater(this::hidePopup);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void hidePopup(){
        if(popup.isShowing())
            popup.hide();
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public StackPane getPlayerHolder() {
        return playerHolder;
    }
}
