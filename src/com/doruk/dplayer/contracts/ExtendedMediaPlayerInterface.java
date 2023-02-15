package com.doruk.dplayer.contracts;

public interface ExtendedMediaPlayerInterface extends MediaPlayerInterface{
    void nextFrame(); // forward the video by one frame
    void previousFrame(); // back seek the video by one frame
    void playAudioOnly(); // only play the audio from the file
}
