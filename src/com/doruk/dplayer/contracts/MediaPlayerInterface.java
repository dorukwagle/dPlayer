package com.doruk.dplayer.contracts;

import javafx.scene.control.Slider;
import uk.co.caprica.vlcj.player.base.TrackDescription;

import java.awt.Dimension;
import java.util.List;

public interface MediaPlayerInterface extends BasicMediaPlayerInterface{
    List<TrackDescription> getSubtitles();
    List<TrackDescription> getAudioTracks();
    void setSubtitle(); // set one of the loaded subtitle either from the video or file
    void setAudioTrack(); // set one of the available audio track
    void scaleToScreen(Dimension dimension); //
}
