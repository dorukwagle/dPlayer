package com.doruk.dplayer.controllers;


import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.contracts.MediaSelectEvent;
import com.doruk.dplayer.models.HomeModel;
import com.doruk.dplayer.views.HomeView;
import com.doruk.dplayer.views.PlayerView;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class HomeController implements Controllers {
    private Scene scene;
    private HomeView homeView;
    private File[] medias;
    private HomeModel model;
    private MediaSelectEvent selectionCompleted;

    public HomeController(){
        homeView = new HomeView();
        scene = new Scene(homeView);
        model = new HomeModel();

        homeView.getBtnChooseFile().setOnAction(event -> {
            medias = model.validateChosenFiles(homeView.chooseFiles());
            selectionCompleted();
        });
        homeView.getBtnChooseAudioDir().setOnAction(event -> {
            medias = model.readAudioDirectory(homeView.chooseDirectory());
            selectionCompleted();
        });
        homeView.getBtnChooseVideoDir().setOnAction(event -> {
            medias = model.readVideoDirectory(homeView.chooseDirectory());
            selectionCompleted();
        });
    }

    public void setOnComplete(MediaSelectEvent event){
        selectionCompleted = event;
    }

    private void selectionCompleted(){
        if (medias == null || medias.length == 0)
            return;
        selectionCompleted.onSelectionComplete(medias);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public Button getPreferenceButton(){
        return homeView.getBtnPreferences();
    }
}
