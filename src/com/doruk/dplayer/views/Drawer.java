package com.doruk.dplayer.views;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;

public class Drawer extends VBox {

    private boolean hidden = true;
    private StackPane parent;
    private float width = 0f;
    private float widthPercent;

    private ListView<String> list;

    public Drawer(StackPane parent, float widthPercent){
        this.parent = parent;
        this.widthPercent = widthPercent;

        setOpacity(0.8);
        list = new ListView<>();
        list.getItems().add("hello world");
        list.getItems().add("bad idea");
        list.getItems().add("hey man");
        getChildren().add(list);

        setSize();
        setVisible(false);
    }

    private void setSize(){
        CompletableFuture.runAsync(()-> {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            width = (float)parent.getWidth()*widthPercent;
            setMaxWidth(width);
            setTranslateX(-width);
            setVisible(true);
        });
    }

    public void show(){
        animate(false);
    }

    public void hide(){
        animate(true);
    }

    private void animate(boolean hidden){
        float pixels = (hidden? -width:0);
        KeyValue keyValue = new KeyValue(this.translateXProperty(), pixels);

        // over the course of 5 seconds
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValue);
        Timeline timeline = new Timeline(keyFrame);

        timeline.play();
        CompletableFuture.runAsync(
                () -> {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    this.hidden = hidden;
                }
        );
    }

    public boolean isHidden(){
        return hidden;
    }

}
