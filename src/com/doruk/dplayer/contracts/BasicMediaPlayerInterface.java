package com.doruk.dplayer.contracts;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

public interface BasicMediaPlayerInterface {
    void load(String filePath); // loads the media file
//    void reload() throws FileNotFoundException; // reloads and resets player position to 0
    void play();
    void pause();
    void stop();
    boolean isPaused();
    void seek(int seconds); // play from the given position in seconds
    int getCurrentTime(); // return current playing position in seconds
    void seekForward(int seconds); // fast-forward the video by given seconds
    void seekBackward(int second); // fast-backward ''
    void setVolume(int volume);
    int getVolume();
    void setTime(long time);
    void setOnComplete(MediaPlayCompleted mediaPlayCompleted);
    void setFullScreen(Stage stage);
    void setFitToScreen(Dimension dimension); // whether to resize the video during start up

    void setOriginalSize();

    ImageView getMediaView();

}
