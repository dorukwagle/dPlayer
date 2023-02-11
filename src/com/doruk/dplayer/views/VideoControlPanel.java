package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.IconsProvider;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class VideoControlPanel extends VBox {

    public VideoControlPanel(IconsProvider icons){
        ToolBar panel = new ToolBar();

        BorderPane controlBase = new BorderPane();
        HBox.setHgrow(controlBase, Priority.ALWAYS);
        BorderPane sliderBox = new BorderPane();

        Label currentPosition = new Label("00:00:00");
        Slider seekBar = new Slider();

        Label totalRemainingPosition = new Label("00:00:00");
        sliderBox.setLeft(currentPosition);
        sliderBox.setCenter(seekBar);
        sliderBox.setRight(totalRemainingPosition);


        BorderPane btnBoxBase = new BorderPane();
        HBox btnBox = new HBox();
        btnBoxBase.setLeft(btnBox);
        HBox volControl = new HBox();
        btnBoxBase.setRight(volControl);

        Button playPause = new Button();
        playPause.setGraphic(icons.createGraphic("/res/pause_icon_white.png", 10, 10));

        Button previousBtn = new Button();
        previousBtn.setGraphic(icons.createGraphic("/res/previous_icon_white.png", 10, 10));

        Button stopBtn = new Button();
        stopBtn.setGraphic(icons.createGraphic("/res/play_icon_white.png", 10, 10));

        Button nextBtn = new Button();
        nextBtn.setGraphic(icons.createGraphic("/res/next_icon_white.png", 10, 10));


        Button audioBtn = new Button();
        audioBtn.setGraphic(icons.createGraphic("/res/volume_max_icon_white.png", 10, 10));

        Slider volumeSlider = new Slider();
        Label volumeLabel = new Label("25%");
        volumeLabel.setBackground(Background.fill(Paint.valueOf("red")));
        BorderPane volumeVisual = new BorderPane();
        volumeVisual.setTop(volumeSlider);
        volumeVisual.setBottom(volumeLabel);

        btnBox.getChildren().addAll(playPause, previousBtn, stopBtn, nextBtn);
        volControl.getChildren().addAll(audioBtn, volumeSlider, volumeLabel);

        Button drawerBtn = new Button();
        drawerBtn.setGraphic(icons.createGraphic("/res/navigation_drawer_white.png", 30, 30));
        panel.getItems().add(drawerBtn);

        panel.getItems().add(controlBase);
        controlBase.setTop(sliderBox);
//        controlBase.setBackground(Background.fill(Paint.valueOf("red")));
        controlBase.setBottom(btnBoxBase);
        getChildren().add(panel);
    }
}
