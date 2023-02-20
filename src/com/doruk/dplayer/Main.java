package com.doruk.dplayer;

import com.doruk.dplayer.controllers.NavigationController;
import com.doruk.dplayer.views.BaseContainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        BaseContainer container = BaseContainer.getInstance();
        container.setStage(stage);
        Stage primaryStage = container.getStage();
        primaryStage.setTitle("dPlayer");
        new NavigationController(getParameters());
        primaryStage.show();
    }
}
