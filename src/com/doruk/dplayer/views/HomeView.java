package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.ResourceProvider;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;


public class HomeView extends StackPane {
    private final Button btnPreferences;
    private final Button btnChooseFile;
    private final Button btnChooseAudioDir, btnChooseVideoDir;

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

        btnChooseFile = createIconBtn("video_icon", 100, 100);
        Text lbl = new Text("Choose the Media file Or the Folder");
        Font font = Font.font("Verdana", FontWeight.EXTRA_BOLD, 25);
        lbl.setFont(font);
        lbl.setFill(Color.LIGHTGRAY);
        VBox mLay = new VBox(lbl, btnChooseFile);
        mLay.setAlignment(Pos.CENTER);
        mLay.setSpacing(20);

        btnChooseAudioDir = createIconBtn("music_folder", 100, 100);
//        btnChooseMediaDir = createIconBtn("media_folder", 100, 100);
        btnChooseVideoDir = createIconBtn("video_folder", 100, 100);

        HBox dirHolder = new HBox(btnChooseAudioDir, btnChooseVideoDir);
        dirHolder.setAlignment(Pos.CENTER);
        dirHolder.setSpacing(10);
        itemHolder.getChildren().add(mLay);
        itemHolder.getChildren().add(dirHolder);


        getChildren().add(itemHolder);
        getChildren().add(bar);

    }

    public File chooseDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        return chooser.showDialog(BaseContainer.getInstance().getStage());
    }

    public List<File> chooseFiles(){
        FileChooser chooser = new FileChooser();
        return chooser.showOpenMultipleDialog(BaseContainer.getInstance().getStage());
    }


    private Button createIconBtn(String icon, int width, int height){
        ResourceProvider icons = new ResourceProvider();
        var img = icons.getIcon(icon, width, height);
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

//    public Button getBtnChooseMediaDir() {
//        return btnChooseMediaDir;
//    }
}
