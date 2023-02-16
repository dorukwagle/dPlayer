package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import javafx.scene.Scene;

public class SettingsController implements Controllers {
    private Scene scene;

    @Override
    public Scene getScene() {
        return scene;
    }
}
