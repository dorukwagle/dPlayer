package com.doruk.dplayer.contracts;

import java.util.List;

public interface MediaPlayerInterface extends BasicMediaPlayerInterface{
    List<Object> getSubtitles();
    List<Object> getAudioTracks();
    void setMediaType(String media); // video or audio
    boolean isVideo(); // returns if the current media is video (if not video its is audio)
    void setSubtitle(); // set one of the loaded subtitle either from the video or file
    void loadSubtitleFile(); // set the subtitles from external file
    void setAudioTrack(); // set one of the available audio track
}
