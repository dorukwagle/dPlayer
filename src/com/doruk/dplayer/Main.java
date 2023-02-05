package com.doruk.dplayer;

import com.doruk.dplayer.controllers.NavigationController;
import com.doruk.dplayer.views.BaseContainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

//        Stage stage = new Stage();
////        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initModality(Modality.WINDOW_MODAL);
//        stage.initOwner(primaryStage);

        //stage.initModality(Modality.NONE);

//        stage.initStyle(StageStyle.DECORATED);

//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.initStyle(StageStyle.UNIFIED);
//        stage.initStyle(StageStyle.UTILITY);

//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());

//        primaryStage.setOnCloseRequest(event -> {
//            event.consume();
//            stage.show();
//            System.out.println("The app is being exit");
//            stage.setOnCloseRequest(event2 -> {
//                Platform.exit();
//            });
//        });

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BaseContainer container = BaseContainer.getInstance();
        container.setStage(stage);

        Stage primaryStage = container.getStage();
        primaryStage.setTitle("dPlayer");
        primaryStage.show();
        new NavigationController(getParameters());
    }
}