package com.doruk.dplayer.views;

import com.doruk.dplayer.contracts.ListItemOnClick;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Drawer extends VBox {

    private boolean hidden = true;
    private StackPane parent;
    private float width = 0f;
    private float widthPercent;

    private VBox list;
    private Label totalDuration;
    private List<GridPane> playLists;
    private ListItemOnClick callback = null;
    private GridPane lastClickedItem;
    private String hoverColor;
    private String selectedColor;


    public Drawer(StackPane parent, float widthPercent){
        playLists = new ArrayList<>();
        hoverColor = "cyan";
        selectedColor = "blue";
        this.parent = parent;
        this.widthPercent = widthPercent;
        setOpacity(0.8);

        totalDuration = new Label("Total Duration: ");
        totalDuration.setId("total_duration");
        getChildren().add(totalDuration);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.pannableProperty().set(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        list = new VBox();
        scrollPane.setContent(list);
        list.setId("drawer_list");
        setId("drawer");
        getChildren().add(scrollPane);


        setOnMouseExited(mouseEvent -> this.hide());
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

    public void addItem(String[] item){
        Label index = new Label(item[0]);
        Label filename = new Label(item[1]);
        Label duration = new Label(item[2]);

        GridPane listItem = new GridPane();
        listItem.setMinWidth(widthPercent);
        var col1 = new ColumnConstraints();
        col1.setPercentWidth(8);
        col1.setHalignment(HPos.LEFT);
        var col2 = new ColumnConstraints();
        col2.setHalignment(HPos.LEFT);
        col2.setPercentWidth(75);
        var col3 = new ColumnConstraints();
        col3.setHalignment(HPos.RIGHT);
        col3.setPercentWidth(17);

        listItem.getColumnConstraints().addAll(col1, col2, col3);
        listItem.add(index, 0, 0);
        listItem.add(filename, 1, 0);
        listItem.add(duration, 2, 0);
        list.getChildren().add(listItem);
        listItem.setCursor(Cursor.HAND);
        playLists.add(listItem);
        addActionListener(listItem);
    }

    private void addActionListener(GridPane item){
        item.setOnMouseEntered(mouseEvent -> {
                    if(lastClickedItem == item)
                        return;
                    item.setBackground(Background.fill(Paint.valueOf(hoverColor)));
                }
        );
        item.setOnMouseExited(mouseEvent -> {
                    if(lastClickedItem == item)
                        return;
                    item.setBackground(Background.EMPTY);
                }
        );

        item.setOnMouseClicked(mouseEvent -> {
            int index = Integer.parseInt(((Label) item.getChildren().get(0)).getText()) - 1;
            String filename = ((Label) item.getChildren().get(1)).getText();
            selectItem(item);
            if(callback == null)
                return;
            callback.onClick(index, filename);
        });
    }

    public void setOnClick(ListItemOnClick callback){
        this.callback = callback;
    }


    public void selectItem(int index){
        selectItem(playLists.get(index));
    }

    private void selectItem(GridPane item){
        if(lastClickedItem != null)
            lastClickedItem.setBackground(Background.EMPTY);
        item.setBackground(Background.fill(Paint.valueOf(selectedColor)));
        lastClickedItem = item;
    }

    public Label getTotalDuration(){
        return totalDuration;
    }

    public void setHoverColor(String hoverColor) {
        this.hoverColor = hoverColor;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public String[] getSelectedItem(){
        return new String[] {
                ((Label) lastClickedItem.getChildren().get(0)).getText(),
                ((Label) lastClickedItem.getChildren().get(1)).getText(),
                ((Label) lastClickedItem.getChildren().get(2)).getText()
            };
    }
}
