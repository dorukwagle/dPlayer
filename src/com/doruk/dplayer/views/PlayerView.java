package com.doruk.dplayer.views;

import com.doruk.dplayer.utilities.IconsProvider;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class PlayerView extends BorderPane {

    private MenuBar menuBar;
    private VideoControlPanel controlPanel;

    private IconsProvider icons;

    public PlayerView(){
        icons = new IconsProvider();
        menuBar = new MenuBar(icons);
        setTop(menuBar);




        controlPanel = new VideoControlPanel(icons);
        setBottom(controlPanel);
    }

}
