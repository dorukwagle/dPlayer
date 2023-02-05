package com.doruk.dplayer.views;

import javafx.stage.Stage;

public class BaseContainer {
    private static BaseContainer window = null;
    private Stage stage = null;

    private BaseContainer(){}

    public static BaseContainer getInstance(){
        if (window == null)
            window = new BaseContainer();
        return window;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
    public Stage getStage(){
        return this.stage;
    }
}
