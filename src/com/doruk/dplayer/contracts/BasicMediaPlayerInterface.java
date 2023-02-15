package com.doruk.dplayer.contracts;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

public interface BasicMediaPlayerInterface {
    void load(String filePath) throws FileNotFoundException; // loads the media file
//    void reload() throws FileNotFoundException; // reloads and resets player position to 0
    void play();
    void pause();
    void stop();
    void seek(int seconds); // play from the given position in seconds
    int getCurrentPosition(); // return current playing position in seconds
    void seekForward(int seconds); // fast-forward the video by given seconds
    void seekBackward(int second); // fast-backward ''
    void setVolume(int volume);
    int getVolume();
    void setOnComplete(Consumer<Object> function);
    void setHeight(float height);
    void setWidth(float width);
    void setFullScreen(Stage stage);

    void setOriginalSize();

    ImageView getMediaView();

}
