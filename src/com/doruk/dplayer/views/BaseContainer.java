package com.doruk.dplayer.views;

import javafx.stage.Stage;

public class BaseContainer extends Stage {
    private static BaseContainer window = null;

    private BaseContainer(){
    }

    public static BaseContainer getInstance(){
        if (window == null)
            window = new BaseContainer();
        return window;
    }
}
