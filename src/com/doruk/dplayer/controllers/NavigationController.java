package com.doruk.dplayer.controllers;

import com.doruk.dplayer.views.BaseContainer;
import com.doruk.dplayer.views.HomeView;
import com.doruk.dplayer.views.PlayerView;
import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NavigationController {
    private Stage stage;
    private Map<String, Object> controllers;
    private float screenSize = 0.8f;

    public NavigationController(Parameters params){
        this.stage = BaseContainer.getInstance().getStage();
        this.controllers = new HashMap<>();
        this.controllers.put("home", new HomeController());
        this.controllers.put("player", new PlayerController());
        this.controllers.put("settings", new SettingsController());

        // control the stage size ( later read the database for settings and apply them )
        this.configWindow();
        // check if there are any arguments, and launch the player if any
        Scene scene;
        if(!params.getRaw().isEmpty())
            scene = new Scene(new PlayerView());
        else
            scene = new Scene(new HomeView());
        scene.getStylesheets().add(this.getClass().getResource("/assets/dark_theme_adv.css").toString());
//        scene.getRoot().setStyle("");
        this.stage.setScene(scene);
    }

    private void configWindow(){
        // set the screen size
        var bounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth((int) (bounds.getWidth() * screenSize));
        stage.setHeight((int) (bounds.getHeight() * screenSize));
        // position the window at the center
        double x = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) * 0.5;
        double y = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) * 0.4;
        stage.setX(x);
        stage.setY(y);
    }
}
