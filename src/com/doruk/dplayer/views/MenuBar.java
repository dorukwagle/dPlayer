package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.ResourceProvider;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class MenuBar extends VBox {

    private Menu subtitle, audioTracks, controls;
    private MenuItem preference, exit;
    private CheckMenuItem soundOnly;
    private ToggleGroup subtitleToggle, audioTracksToggle, playBackSpeedGroup;
    private MenuItem addSubtitleMenu;

    public MenuBar(ResourceProvider icons){
        javafx.scene.control.MenuBar menuBar = new javafx.scene.control.MenuBar();
        Menu playback = new Menu("Playback");
        playback.setGraphic(icons.getIcon("play_icon", 30, 30));

        Menu playSpeed = new Menu("Playback Speed");
        RadioMenuItem firstQuartile = new RadioMenuItem("0.25X");
        firstQuartile.setUserData("0.25");
        RadioMenuItem secondQuartile = new RadioMenuItem("0.5X");
        secondQuartile.setUserData("0.5");
        RadioMenuItem thirdQuartile = new RadioMenuItem("0.75X");
        thirdQuartile.setUserData("0.75");
        RadioMenuItem normal = new RadioMenuItem("Normal");
        normal.setUserData("1");
        normal.setSelected(true);
        RadioMenuItem oneAnd1Quartile = new RadioMenuItem("1.25X");
        oneAnd1Quartile.setUserData("1.25");
        RadioMenuItem oneAnd2Quartile = new RadioMenuItem("1.5X");
        oneAnd2Quartile.setUserData("1.5");
        RadioMenuItem oneAnd3Quartile = new RadioMenuItem("1.75X");
        oneAnd3Quartile.setUserData("1.75");
        RadioMenuItem doubleQuartile = new RadioMenuItem("2X");
        doubleQuartile.setUserData("2");
        playSpeed.getItems().addAll(firstQuartile, secondQuartile, thirdQuartile, normal, oneAnd1Quartile,
                oneAnd2Quartile, oneAnd3Quartile, doubleQuartile);

        playback.getItems().add(playSpeed);

//        soundOnly = new CheckMenuItem("Audio Only");
//        playback.getItems().add(soundOnly);

        playBackSpeedGroup = new ToggleGroup();
        playBackSpeedGroup.getToggles().addAll(firstQuartile, secondQuartile, thirdQuartile, normal, oneAnd1Quartile,
                oneAnd2Quartile, oneAnd3Quartile, doubleQuartile);

        subtitle = new Menu("Subtitles");
        subtitle.setGraphic(icons.getIcon("subtitles_icon", 20, 20));
        addSubtitleMenu = new RadioMenuItem("Add File");
        subtitle.getItems().add(addSubtitleMenu);

        audioTracks = new Menu("Audio Tracks");
        audioTracks.setGraphic(icons.getIcon("volume_max_icon", 20, 20));

        controls = new Menu("Controls");
        controls.setGraphic(icons.getIcon("settings_icon", 20, 20));

        preference = new MenuItem("Preferences");
        exit = new MenuItem("Exit");
        controls.getItems().addAll(preference, exit);


        subtitleToggle = new ToggleGroup();
        audioTracksToggle = new ToggleGroup();

        menuBar.getMenus().addAll(playback, subtitle, audioTracks, controls);
        getChildren().add(menuBar);
    }

    public RadioMenuItem addSubtitleItem(String text){
        RadioMenuItem item = new RadioMenuItem(text);
        subtitle.getItems().add(item);
        subtitleToggle.getToggles().add(item);
        return item;
    }

    public RadioMenuItem addAudioTrackItem(String text){
        RadioMenuItem item = new RadioMenuItem(text);
        audioTracks.getItems().add(item);
        audioTracksToggle.getToggles().add(item);
        return item;
    }

    public File chooseSubtitleFile(File dir){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(dir);
        return chooser.showOpenDialog(BaseContainer.getInstance().getStage());
    }

    public MenuItem getPreference() {
        return preference;
    }

    public MenuItem getExit(){
        return exit;
    }

    public CheckMenuItem getSoundOnly() {
        return soundOnly;
    }

    public ToggleGroup getPlayBackSpeedGroup() {
        return playBackSpeedGroup;
    }

    public MenuItem getAddSubtitleMenu() {
        return addSubtitleMenu;
    }
}
