package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.views.SettingsView;
import javafx.scene.Scene;

public class SettingsController implements Controllers {
    private Scene scene;
    private SettingsView settingsView;

    public SettingsController(){
        settingsView = new SettingsView();
        scene = new Scene(settingsView);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
