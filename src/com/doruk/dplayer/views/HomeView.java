package com.doruk.dplayer.views;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class HomeView extends StackPane {
    private final Button btnPreferences;
    private final Button btnChooseFile;
    private final Button btnChooseAudioDir, btnChooseVideoDir, btnChooseMediaDir;

    public HomeView() {
        // add the toolbar
        setAlignment(Pos.TOP_CENTER);
        VBox bar = new VBox();
        ToolBar toolBar = new ToolBar();
        Region space = new Region();
        HBox.setHgrow(space, Priority.SOMETIMES);
        // add button to the toolbar
        btnPreferences = new Button("Preferences");
        Image img = new Image(this.getClass().getResourceAsStream("/res/settings_icon_white.png"));
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        btnPreferences.setGraphic(imageView);
        toolBar.getItems().addAll(space, btnPreferences);
        bar.getChildren().add(toolBar);
        bar.setMaxHeight(toolBar.getHeight());

        // add file choosers
        FlowPane itemHolder = new FlowPane();
        itemHolder.setOrientation(Orientation.VERTICAL);
        itemHolder.setAlignment(Pos.CENTER);
        itemHolder.setVgap(20);

        btnChooseFile = createIconBtn("/res/video_icon.png", 100, 100);
        Text lbl = new Text("Choose the Media file Or the Folder");
        Font font = Font.font("Verdana", FontWeight.EXTRA_BOLD, 25);
        lbl.setFont(font);
        lbl.setFill(Color.LIGHTGRAY);
        VBox mLay = new VBox(lbl, btnChooseFile);
        mLay.setAlignment(Pos.CENTER);
        mLay.setSpacing(20);

        btnChooseAudioDir = createIconBtn("/res/music_folder.png", 100, 100);
        btnChooseMediaDir = createIconBtn("/res/media_folder.png", 100, 100);
        btnChooseVideoDir = createIconBtn("/res/video_folder.png", 100, 100);

        HBox dirHolder = new HBox(btnChooseAudioDir, btnChooseVideoDir, btnChooseMediaDir);
        dirHolder.setAlignment(Pos.CENTER);
        dirHolder.setSpacing(10);
        itemHolder.getChildren().add(mLay);
        itemHolder.getChildren().add(dirHolder);


        getChildren().add(itemHolder);
        getChildren().add(bar);

    }

    private Button createIconBtn(String icon, int width, int height){
        ImageView img = new ImageView(
                new Image(getClass().getResourceAsStream(icon))
        );
        img.setPreserveRatio(true);
        img.setFitHeight(height);
        img.setFitWidth(width);
        Button btn = new Button();
        btn.setGraphic(img);
        btn.setFocusTraversable(false);
        return btn;
    }

    public Button getBtnPreferences() {
        return btnPreferences;
    }

    public Button getBtnChooseFile() {
        return btnChooseFile;
    }

    public Button getBtnChooseAudioDir() {
        return btnChooseAudioDir;
    }

    public Button getBtnChooseVideoDir() {
        return btnChooseVideoDir;
    }

    public Button getBtnChooseMediaDir() {
        return btnChooseMediaDir;
    }
}
