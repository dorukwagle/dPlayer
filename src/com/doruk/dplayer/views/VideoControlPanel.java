package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.ResourceProvider;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class VideoControlPanel extends VBox {

    private Button drawerBtn, playPause, previousBtn, stopBtn, nextBtn, audioBtn;
    private Label currentPosition, totalRemainingPosition, volumeLabel;
    private Slider seekBar, volumeSlider;

    public VideoControlPanel(ResourceProvider icons){
        ToolBar panel = new ToolBar();

        BorderPane controlBase = new BorderPane();
        HBox.setHgrow(controlBase, Priority.ALWAYS);
        BorderPane sliderBox = new BorderPane();

        currentPosition = new Label("00:00:00");
        seekBar = new Slider();
        seekBar.setCursor(Cursor.OPEN_HAND);
        seekBar.setMax(1000);
        seekBar.setMin(0);
        customizeFill(seekBar);

        totalRemainingPosition = new Label("00:00:00");
        totalRemainingPosition.setCursor(Cursor.OPEN_HAND);
        sliderBox.setLeft(currentPosition);
        sliderBox.setCenter(seekBar);
        sliderBox.setRight(totalRemainingPosition);


        BorderPane btnBoxBase = new BorderPane();
        HBox btnBox = new HBox();
        btnBoxBase.setLeft(btnBox);
        HBox volControl = new HBox();
        btnBoxBase.setRight(volControl);

        playPause = new Button();
        playPause.setGraphic(icons.getIcon("pause_icon", 20, 20));
        HBox.setMargin(playPause, new Insets(0, 20, 0, 0));

        previousBtn = new Button();
        previousBtn.setGraphic(icons.getIcon("previous_icon", 15, 15));

        stopBtn = new Button();
        stopBtn.setGraphic(icons.getIcon("stop_icon", 20, 20));

        nextBtn = new Button();
        nextBtn.setGraphic(icons.getIcon("next_icon", 15, 15));


        audioBtn = new Button();
        audioBtn.setGraphic(icons.getIcon("volume_max_icon", 20, 20));

        volumeSlider = new Slider();
        volumeSlider.setMax(100);
        volumeSlider.setMin(0);
        customizeFill(volumeSlider);
        volumeSlider.setValue(25);
        HBox.setMargin(volumeSlider, new Insets(5, 0, 0, 0));

        volumeLabel = new Label("25%");

        btnBox.getChildren().addAll(playPause, previousBtn, stopBtn, nextBtn);
        volControl.getChildren().addAll(audioBtn, volumeSlider, volumeLabel);

        drawerBtn = new Button();
        drawerBtn.setGraphic(icons.getIcon("navigation_drawer_icon", 30, 30));
        panel.getItems().add(drawerBtn);

        panel.getItems().add(controlBase);
        controlBase.setTop(sliderBox);
        controlBase.setBottom(btnBoxBase);
        getChildren().add(panel);
    }

    private void customizeFill(Slider slider){
        slider.valueProperty().addListener((obs, oldValue, newValue) -> {
            double percentage = 100.0 * newValue.doubleValue() / slider.getMax();
            String style = String.format(
                    "-track-color: linear-gradient(to right, " +
                            "-fx-accent 0%%, " +
                            "-fx-accent %1$.1f%%, " +
                            "-default-track-color %1$.1f%%, " +
                            "-default-track-color 100%%);",
                    percentage);
            slider.setStyle(style);
        });
    }

    public Button getDrawerBtn() {
        return drawerBtn;
    }

    public Button getPlayPause() {
        return playPause;
    }

    public Button getPreviousBtn() {
        return previousBtn;
    }

    public Button getStopBtn() {
        return stopBtn;
    }

    public Button getNextBtn() {
        return nextBtn;
    }

    public Button getAudioBtn() {
        return audioBtn;
    }

    public Label getCurrentPosition() {
        return currentPosition;
    }

    public Label getTotalRemainingPosition() {
        return totalRemainingPosition;
    }

    public Label getVolumeLabel() {
        return volumeLabel;
    }

    public Slider getSeekBar() {
        return seekBar;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }
}
