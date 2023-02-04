package com.doruk.dplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("JavaFX App");

        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);

        //stage.initModality(Modality.NONE);

//        stage.initStyle(StageStyle.DECORATED);

//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.initStyle(StageStyle.UNIFIED);
//        stage.initStyle(StageStyle.UTILITY);
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox);
//        scene.setCursor(Cursor.NONE);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);

        Rectangle2D bounds = Screen.getPrimary().getBounds();
//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());
        primaryStage.show();

//        primaryStage.setOnCloseRequest(event -> {
//            event.consume();
//            stage.show();
//            System.out.println("The app is being exit");
//            stage.setOnCloseRequest(event2 -> {
//                Platform.exit();
//            });
//        });
    }

    public static void main(String[] args) {
        Main.launch();
    }
}