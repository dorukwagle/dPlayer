package com.doruk.dplayer.contracts;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public interface BasicMediaPlayerInterface {
    void load(String filePath); // loads the media file
//    void reload() throws FileNotFoundException; // reloads and resets player position to 0
    void play();
    void pause();
    void stop();
    boolean isPlaying();
    void seek(int seconds); // play from the given position in seconds
    long getCurrentTime(); // return current playing position in seconds
    void seekForward(int seconds); // fast-forward the video by given seconds
    void seekBackward(int second); // fast-backward ''
    void setVolume(int volume);
    int getVolume();
    void setTime(long time);
    void setOnComplete(MediaPlayCompleted mediaPlayCompleted);
    void setFullScreen(Stage stage);

    void setOriginalSize();

    ImageView getMediaView();

    long getDuration(); // returns the total playback duration of the media

}
