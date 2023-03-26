package com.doruk.dplayer.contracts;

import javafx.scene.control.Slider;
import uk.co.caprica.vlcj.player.base.TrackDescription;

import java.awt.Dimension;
import java.util.List;
import java.util.function.Consumer;

public interface MediaPlayerInterface extends BasicMediaPlayerInterface{
    List<TrackDescription> getSubtitles();
    List<TrackDescription> getAudioTracks();
    void setSubtitle(int id); // set one of the loaded subtitle either from the video or file
    void setAudioTrack(int id); // set one of the available audio track
    void setSubtitleFile(String filename);
    void setPlayBackSpeed(float speed);
    void scaleToScreen(Dimension dimension); //

    void setOnTimeChanged(Consumer<Long> con);
}
