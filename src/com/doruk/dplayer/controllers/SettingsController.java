package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.utilities.PreferencesManager;
import com.doruk.dplayer.views.SettingsView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.concurrent.CompletableFuture;

public class SettingsController implements Controllers {
    private Scene scene;
    private SettingsView settingsView;
    private PreferencesManager preference;

    private CheckBox resumeMedia, lightTheme;
    private ComboBox<Integer> resumeMediaLength;

    private Label shortBackJump, shortFrontJump, mediumBackJump, mediumFrontJump, longBackJump, longFrontJump,
            volumeUp, volumeDown;

    private ComboBox<Integer> shortJump, mediumJump, longJump;



    public SettingsController(){
        settingsView = new SettingsView();
        scene = new Scene(settingsView);
        preference = new PreferencesManager();

        resumeMedia = settingsView.getResumeMedia();
        lightTheme = settingsView.getLightTheme();
        resumeMediaLength = settingsView.getResumeMediaLength();
        shortBackJump = settingsView.getShortBackJump();
        shortFrontJump = settingsView.getShortFrontJump();
        mediumBackJump = settingsView.getMediumBackJump();
        mediumFrontJump = settingsView.getMediumFrontJump();
        longBackJump = settingsView.getLongBackJump();
        longFrontJump = settingsView.getLongFrontJump();
        volumeUp = settingsView.getVolumeUp();
        volumeDown = settingsView.getVolumeDown();
        shortJump = settingsView.getShortJump();
        mediumJump = settingsView.getMediumJump();
        longJump = settingsView.getLongJump();

        loadPreferences();
        settingsView.getSaveSettings().setOnAction(this::savePreferences);
        settingsView.getResetSettings().setOnAction((event) -> {
            preference.resetDefault();
            loadPreferences();
        });
    }

    private void loadPreferences(){
        resumeMedia.setSelected(preference.isResumePlayback());
        lightTheme.setSelected(preference.isLightThemeChecked());
        resumeMediaLength.setValue((int)preference.getResumePlaybackLength());

        shortJump.setValue((int)preference.getShortJumpDuration());
        mediumJump.setValue((int)preference.getMediumJumpDuration());
        longJump.setValue((int)preference.getLongJumpDuration());
    }

    private void savePreferences(ActionEvent event){
        preference.setResumePlayback(resumeMedia.isSelected());
        preference.setLightThemeChecked(lightTheme.isSelected());
        preference.setResumePlaybackLength(resumeMediaLength.getValue());

        preference.setShortJumpDuration(shortJump.getValue());
        preference.setMediumJumpDuration(mediumJump.getValue());
        preference.setLongJumpDuration(longJump.getValue());
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void scrollToBottom(){
        //        scroll the scrollbar to bottom after some time
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            settingsView.getScroller().setVvalue(1.0);
        });
    }

    public Button getDoneSettings(){
        return settingsView.getDoneSettings();
    }
}
