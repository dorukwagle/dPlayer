package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.ResourceProvider;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import uk.co.caprica.vlcj.javafx.view.ResizableImageView;

import java.util.concurrent.CompletableFuture;


public class PlayerView extends StackPane {

    private Drawer drawer;
    private BorderPane barLay;
    private Text popupLabel;
    private Popup popup;

    private StackPane playerHolder;
    private VideoControlPanel controlPanel;
    private ImageView mediaView;

    public PlayerView(ImageView mediaView, MenuBar menuBar, VideoControlPanel controlPanel, ResourceProvider icons) {
        MenuBar dummyMenu = new MenuBar(icons);
        VideoControlPanel dummyControl = new VideoControlPanel(icons);
        dummyMenu.setVisible(false);
        dummyControl.setVisible(false);

        setMinSize(0, 0);

        this.controlPanel = controlPanel;
        this.mediaView = mediaView;

        BorderPane playerLay = new BorderPane();
        playerLay.setMinSize(0, 0);
        playerLay.setBackground(Background.fill(Paint.valueOf("black")));
        barLay = new BorderPane();
        menuBar.setMaxHeight(0);
        controlPanel.setMaxHeight(0);
        barLay.setTop(menuBar);
        barLay.setBottom(controlPanel);

        popup = new Popup();
        popupLabel = new Text();
        popupLabel.setFill(Paint.valueOf("white"));
        popupLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));
        popup.getContent().add(popupLabel);

        playerHolder = new StackPane();
        playerHolder.setMaxSize(0, 0);
        playerHolder.setBackground(Background.fill(Paint.valueOf("black")));
        StackPane.setAlignment(mediaView, Pos.CENTER);
        playerHolder.getChildren().add(mediaView);

        StackPane drawerHolder = new StackPane();
        drawer = new Drawer(drawerHolder, 0.6f);
        StackPane.setAlignment(drawer, Pos.TOP_LEFT);
        drawerHolder.getChildren().add(drawer);
        barLay.setCenter(drawerHolder);

        playerLay.setBackground(Background.fill(Paint.valueOf("black")));
        playerHolder.setBackground(Background.fill(Paint.valueOf("black")));

        playerLay.setTop(dummyMenu);
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

    public BorderPane getDisplayArea() {
        return barLay ;
    }

}
