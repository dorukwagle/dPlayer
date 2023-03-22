package com.doruk.dplayer.seekbar;

import javafx.scene.Cursor;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.sql.Time;
import java.util.concurrent.CompletableFuture;

public class SeekBar extends HBox {

    private ProgressBar progressBar;
    private Slider control;

    public SeekBar(){

        // 1560bd, 3a7ca5

        setFillHeight(false);

        StackPane barHolder = new StackPane();
        HBox.setHgrow(barHolder, Priority.ALWAYS);

        progressBar = new ProgressBar();
        progressBar.prefWidthProperty().bind(barHolder.widthProperty());
        progressBar.prefHeightProperty().bind(barHolder.heightProperty());

        progressBar.setProgress(0.5);
        barHolder.getChildren().add(progressBar);

        control = new Slider();
        control.setMax(1);
        control.setMin(0);
        HBox.setHgrow(control, Priority.ALWAYS);
        control.setCursor(Cursor.HAND);

        control.valueProperty().addListener((observableValue, oldValue, newValue) ->
                progressBar.setProgress(newValue.doubleValue()));

        barHolder.getChildren().add(control);

        getChildren().add(barHolder);
    }

    public void setValue(long value){

    }

    public long getValue(){
        return 1;
    }

    public void setMax(long value){

    }

    public long getMax(){
        return 1;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Slider getControl() {
        return control;
    }
}
