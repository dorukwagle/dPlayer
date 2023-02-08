package com.doruk.dplayer.views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

public class SettingsView extends StackPane {

    private final VBox lay;
    private CheckBox resumeMedia, lightTheme;
    private ComboBox<Integer> resumeMediaLength;

    private Label shortBackJump, shortFrontJump, mediumBackJump, mediumFrontJump, longBackJump, longFrontJump,
                    volumeUp, volumeDown;

    private ComboBox<Integer> shortJump, mediumJump, longJump;
    private Button doneSettings;


    public SettingsView(){
        ScrollPane scroller = new ScrollPane();
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.pannableProperty().set(true);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        FlowPane baseLay = new FlowPane();
        baseLay.setOrientation(Orientation.VERTICAL);
        baseLay.setAlignment(Pos.CENTER);

        lay = new VBox();
        lay.setSpacing(40);
        baseLay.getChildren().add(lay);

//        Create general Preferences
        lay.getChildren().add(createHeading("General Preferences"));
        createGeneralPreferences();

//        Create HotKeys Preferences
        lay.getChildren().add(createHeading("HotKeys Preferences"));
        createHotKeysPreferences();

//        Create Seeking Preferences
        lay.getChildren().add(createHeading("Seeking Preferences"));
        createSeekingPreferences();

        doneSettings = new Button("Completed");
        doneSettings.setStyle("-fx-padding:5px;");
        HBox btnHolder = new HBox(doneSettings);
        btnHolder.setAlignment(Pos.CENTER);

        lay.getChildren().add(btnHolder);
        scroller.setContent(baseLay);
        getChildren().add(scroller);

//        scroll the scrollbar to bottom after some time
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            scroller.setVvalue(1.0);
            System.out.println(scroller.getVvalue());
        });
    }

    private void createGeneralPreferences(){
        resumeMediaLength = new ComboBox<>();
        resumeMediaLength.setPromptText("Minutes");
        resumeMediaLength.getItems().addAll(5, 10, 12, 15, 20, 25);
        lightTheme = new CheckBox("Enable Light Theme");
        resumeMedia = new CheckBox("Resume From Last Time Position");
        HBox resumeLen = new HBox(new Label("Resume Playback Longer Than "), resumeMediaLength, new Label("Minutes"));
        resumeLen.setSpacing(10);
        VBox general = new VBox(lightTheme, resumeMedia, resumeLen);
        general.setSpacing(20);
        lay.getChildren().add(general);
    }

    private void createHotKeysPreferences(){
        shortBackJump = new Label("Left Arrow");
        shortFrontJump = new Label("Right Arrow");
        mediumBackJump = new Label("Down Arrow");
        mediumFrontJump = new Label("Up Arrow");
        longBackJump = new Label("CTRL+Left Arrow");
        longFrontJump = new Label("CTRL+Right Arrow");
        volumeUp = new Label("CTRL+Up Arrow");
        volumeDown = new Label("CTRL+Down Arrow");

        Label sbj = new Label("Short Back Jump");
        Label sfj = new Label("Short Front Jump");
        Label mbj = new Label("Medium Back Jump");
        Label mfj = new Label("Medium Front Jump");
        Label lbj = new Label("Long Back Jump");
        Label lfj = new Label("Long Front Jump");
        Label vu = new Label("Volume Up");
        Label vd = new Label("Volume Down");

        VBox left = new VBox(sbj, sfj, mbj, mfj, lbj, lfj, vu, vd);
        left.setSpacing(20);

        VBox right = new VBox(shortBackJump, shortFrontJump, mediumBackJump, mediumFrontJump, longBackJump,
                longFrontJump, volumeUp, volumeDown);
        right.setSpacing(20);

//        Apply Styles the keys Label
        Arrays.stream(new Label[]{shortBackJump, shortFrontJump, mediumBackJump, mediumFrontJump, longBackJump,
                        longFrontJump, volumeUp, volumeDown})
                .forEach(label -> {
                    label.getStyleClass().remove("label");
                    label.getStyleClass().add("shortcut_keys_holder");
                });

        HBox main = new HBox(left, right);
        main.setSpacing(40);
        lay.getChildren().add(main);
    }

    private void createSeekingPreferences(){
        VBox left = new VBox(
                new Label("Short Jump Duration (Sec)"),
                new Label("Medium Jump Duration (Sec)"),
                new Label("Long Jump Duration (Sec)")
        );
        left.setSpacing(20);

        shortJump = new ComboBox<>(FXCollections.observableList(List.of(5, 10, 15)));
        shortJump.setValue(5);
        shortJump.setEditable(true);
        mediumJump = new ComboBox<>(FXCollections.observableList(List.of(10, 15, 20, 25, 30)));
        mediumJump.setValue(10);
        mediumJump.setEditable(true);
        longJump = new ComboBox<>(FXCollections.observableList(List.of(30, 40, 45, 60)));
        longJump.setValue(40);


        VBox right = new VBox(shortJump, mediumJump, longJump);
        right.setSpacing(20);

        HBox main = new HBox(left, right);
        main.setSpacing(40);
        lay.getChildren().add(main);
    }

    private VBox createHeading(String heading){
        Label general = new Label(heading);

        general.setTextAlignment(TextAlignment.CENTER);
        general.getStyleClass().remove("label");
        general.getStyleClass().add("heading");

        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        return new VBox(separator1, general, separator2);
    }
}
