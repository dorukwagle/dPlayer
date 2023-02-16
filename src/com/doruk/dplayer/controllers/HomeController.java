package com.doruk.dplayer.controllers;


import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.views.PlayerView;
import javafx.scene.Scene;


public class HomeController implements Controllers {
    private Scene scene;
    public HomeController(){
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
