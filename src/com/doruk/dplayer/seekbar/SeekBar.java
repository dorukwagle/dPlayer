package com.doruk.dplayer.seekbar;

import com.doruk.dplayer.contracts.SeekBarPopup;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;

public class SeekBar extends HBox {

    private ProgressBar progressBar;
    private Slider control;
    private long min = 0;
    private long max = 1;
    private EventHandler<MouseEvent> onClicked;
    private ChangeListener<Number> valueChanged;
    double popupOffset = 30;

    private Popup popup;
    private Label popupLabel;
    private SeekBarPopup showPopup;

    public SeekBar(){
        popup = new Popup();
        popupLabel = new Label();
        popupLabel.setBackground(Background.fill(Paint.valueOf("#2d333a")));
        popup.getContent().add(popupLabel);

        setFillHeight(false);

        StackPane barHolder = new StackPane();
        HBox.setHgrow(barHolder, Priority.ALWAYS);

        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);

        barHolder.getChildren().add(progressBar);

        control = new Slider();
        control.setMax(max);
        control.setMin(min);
        control.setShowTickMarks(true);
        control.setMajorTickUnit(5);
        control.setOpacity(0.01);
        HBox.setHgrow(control, Priority.ALWAYS);
        control.setCursor(Cursor.HAND);

        barHolder.getChildren().add(control);
        getChildren().add(barHolder);

        addListeners();
    }

    private void displayPopup(MouseEvent event){
        if(showPopup == null) return;;

        showPopup.show(event, control);

        control.setOnMouseEntered(e -> popup.show(control, e.getScreenX(), e.getScreenY() + popupOffset));
        control.setOnMouseExited(e -> popup.hide());
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

        control.setOnMouseMoved(e -> {
            if(showPopup == null) return;
            displayPopup(e);
        });
    }

    public void setValue(long value){
        progressBar.setProgress((double)value / max);
    }

    public void setDoubleValue(double value){
        double nextValue = (double) Math.round((value/max) * max) / max;
        progressBar.setProgress(value / max);
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

    public void setShowPopup(SeekBarPopup popup){
        showPopup = popup;
    }

    public void setPopupText(String text){
        popupLabel.setText(text);
    }

    public void setPopupAnchorX(double screenX){
        popup.setAnchorX(screenX);
    }

    public void setPopupAnchorY(double screenY){
        popup.setAnchorY(screenY - popupOffset);
    }
}
