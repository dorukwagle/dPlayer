package com.doruk.dplayer.seekbar;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.sql.Time;
import java.util.concurrent.CompletableFuture;

public class SeekBar extends HBox {

    private ProgressBar progressBar;
    private Slider control;
    private long min = 0;
    private long max = 1;
    private EventHandler<MouseEvent> onClicked;
    private ChangeListener<Number> valueChanged;

    public SeekBar(){

        setFillHeight(false);

        StackPane barHolder = new StackPane();
        HBox.setHgrow(barHolder, Priority.ALWAYS);

        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);

        barHolder.getChildren().add(progressBar);

        control = new Slider();
        control.setMax(max);
        control.setMin(min);
        HBox.setHgrow(control, Priority.ALWAYS);
        control.setCursor(Cursor.HAND);

        barHolder.getChildren().add(control);
        getChildren().add(barHolder);

        addListeners();
    }

    private void addListeners(){
        control.valueProperty().addListener((observableValue, oldValue, newValue) ->
                progressBar.setProgress(newValue.doubleValue() / max));

        control.setOnMouseClicked(mouseEvent -> {
            if(onClicked != null) onClicked.handle(mouseEvent);
        });

        progressBar.progressProperty().addListener((observableValue, oldValue, newValue) -> {
            if(valueChanged != null) valueChanged.changed(observableValue, oldValue.doubleValue() * max,
                    newValue.doubleValue() * max);
        });
    }

    public void setValue(long value){
        progressBar.setProgress((double)value / max);
    }

    public double getProgressValue(){
        return progressBar.getProgress() * max;
    }

    public double getControlValue(){
        return control.getValue();
    }

    public void setMax(long value){
        max = value;
        control.setMax(max);
    }

    public long getMax(){
        return max;
    }

    public void setMin(long value){
        min = value;
        control.setMin(min);
    }

    public long getMin(){
        return min;
    }

    public void setOnClick(EventHandler<MouseEvent> event){
        onClicked = event;
    }

    public void setOnValueChange(ChangeListener<Number> listener){
        valueChanged = listener;
    }
}
