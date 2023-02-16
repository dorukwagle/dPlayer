package com.doruk.dplayer.controllers;


import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.views.HomeView;
import com.doruk.dplayer.views.PlayerView;
import javafx.scene.Scene;


public class HomeController implements Controllers {
    private Scene scene;
    private HomeView homeView;

    public HomeController(){
        homeView = new HomeView();
        scene = new Scene(homeView);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
