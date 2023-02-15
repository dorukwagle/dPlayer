package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.IconsProvider;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class MenuBar extends VBox {

    private Menu subtitle, audioTracks, preference;
    private ToggleGroup subtitleToggle, audioTracksToggle;

    public MenuBar(IconsProvider icons){
        javafx.scene.control.MenuBar menuBar = new javafx.scene.control.MenuBar();
        Menu playback = new Menu("Playback");
        playback.setGraphic(icons.getIcon("play_icon", 30, 30));

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
        subtitle.setGraphic(icons.getIcon("subtitles_icon", 20, 20));
        RadioMenuItem disableSubtitle = new RadioMenuItem("Disable");
        subtitle.getItems().add(disableSubtitle);

        audioTracks = new Menu("Audio Tracks");
        audioTracks.setGraphic(icons.getIcon("volume_max_icon", 20, 20));

        preference = new Menu("Preferences");
        preference.setGraphic(icons.getIcon("settings_icon", 20, 20));

        subtitleToggle = new ToggleGroup();
        subtitleToggle.getToggles().add(disableSubtitle);
        audioTracksToggle = new ToggleGroup();

        menuBar.getMenus().addAll(playback, subtitle, audioTracks, preference);
        getChildren().add(menuBar);
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
}
