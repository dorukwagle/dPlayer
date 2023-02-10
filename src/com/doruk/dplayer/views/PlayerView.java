package com.doruk.dplayer.views;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;


public class PlayerView extends BorderPane {

    private ToggleGroup audioTracksToggle, subtitleToggle;
    private Menu audioTracks, subtitle, preference;

    public PlayerView(){
        setTop(createMenuBar());



        setBottom(createControlPanel());
    }

    private VBox createMenuBar(){
        MenuBar menuBar = new MenuBar();
        Menu playback = new Menu("Playback");
        playback.setGraphic(createGraphic("/res/play_icon_white.png", 30, 30));

        Menu playSpeed = new Menu("Playback Speed");
        RadioMenuItem firstQuartile = new RadioMenuItem("0.25X");
        RadioMenuItem secondQuartile = new RadioMenuItem("0.5X");
        RadioMenuItem thirdQuartile = new RadioMenuItem("0.75X");
        RadioMenuItem normal = new RadioMenuItem("Normal");
        normal.setSelected(true);
        RadioMenuItem oneAnd1Quartile = new RadioMenuItem("1.25X");
        RadioMenuItem oneAnd2Quartile = new RadioMenuItem("1.5X");
        RadioMenuItem oneAnd3Quartile = new RadioMenuItem("1.75X");
        RadioMenuItem doubleQuartile = new RadioMenuItem("2X");
        playSpeed.getItems().addAll(firstQuartile, secondQuartile, thirdQuartile, normal, oneAnd1Quartile,
                oneAnd2Quartile, oneAnd3Quartile, doubleQuartile);

        playback.getItems().add(playSpeed);

        CheckMenuItem soundOnly = new CheckMenuItem("Audio Only");
        playback.getItems().add(soundOnly);

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(firstQuartile, secondQuartile, thirdQuartile, normal, oneAnd1Quartile,
                oneAnd2Quartile, oneAnd3Quartile, doubleQuartile);

        subtitle = new Menu("Subtitles");
        subtitle.setGraphic(createGraphic("/res/subtitles_icon_white.png", 20, 20));
        RadioMenuItem disableSubtitle = new RadioMenuItem("Disable");
        subtitle.getItems().add(disableSubtitle);

        audioTracks = new Menu("Audio Tracks");
        audioTracks.setGraphic(createGraphic("/res/volume_max_icon_white.png", 20, 20));

        preference = new Menu("Preferences");
        preference.setGraphic(createGraphic("/res/settings_icon_white.png", 20, 20));

        subtitleToggle = new ToggleGroup();
        subtitleToggle.getToggles().add(disableSubtitle);
        audioTracksToggle = new ToggleGroup();

        menuBar.getMenus().addAll(playback, subtitle, audioTracks, preference);
        return new VBox(menuBar);
    }

    private VBox createControlPanel(){
        Button btn = new Button("hello");
        ToolBar panel = new ToolBar(btn);

        return new VBox(panel);
    }

    public void addSubtitleItem(String text){
        RadioMenuItem item = new RadioMenuItem(text);
        subtitle.getItems().add(item);
        subtitleToggle.getToggles().add(item);
    }

    public void addAudioTrackItem(String text){
        RadioMenuItem item = new RadioMenuItem(text);
        audioTracks.getItems().add(item);
        audioTracksToggle.getToggles().add(item);
    }

    public ImageView createGraphic(String path, int height, int width){
        ImageView img = new ImageView(
                new Image(this.getClass().getResourceAsStream(path))
        );
        img.setPreserveRatio(true);
        img.setFitHeight(height);
        img.setFitWidth(width);
        return img;
    }
}
