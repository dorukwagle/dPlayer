package com.doruk.dplayer.utilities;

import java.util.prefs.Preferences;

public class PreferencesManager {
    private Preferences pref;
    public PreferencesManager(){
        pref = Preferences.userRoot();
    }

    public void resetDefault(){
        pref.putBoolean("lightThemeChecked", false);
        pref.putBoolean("resumePlayback", true);
        pref.putLong("resumePlaybackLongerThan", 12); // minutes

        pref.put("shortBackJump", "LEFT");
        pref.put("shortFrontJump", "RIGHT");
        pref.put("mediumBackJump", "DOWN");
        pref.put("mediumFrontJump", "UP");
        pref.put("longBackJump", "CTRL+LEFT");
        pref.put("longFrontJump", "CTRL+RIGHT");
        pref.put("volumeUp", "CTRL+UP");
        pref.put("volumeDown", "CTRL+DOWN");

        pref.putLong("shortJumpDuration", 5); // seconds
        pref.putLong("mediumJumpDuration", 10);
        pref.putLong("longJumpDuration", 40);

        pref.putLong("volume", 25);
    }

    public boolean isLightThemeChecked(){
        return pref.getBoolean("lightThemeChecked", false);
    }

    public boolean isResumePlayback(){
        return pref.getBoolean("resumePlayback", true);
    }

    public long getResumePlaybackLength(){
        return pref.getLong("resumePlaybackLongerThan", 12); // resume if the playback is longer than given time
    }

    public long getShortJumpDuration(){
        return pref.getLong("shortJumpDuration", 5);
    }

    public long getMediumJumpDuration(){
        return pref.getLong("mediumJumpDuration", 10);
    }

    public long getLongJumpDuration(){
        return pref.getLong("longJumpDuration", 40);
    }

    public void setLightThemeChecked(boolean checked){
        pref.putBoolean("lightThemeChecked", checked);
    }

    public void setResumePlayback(boolean resume){
        pref.putBoolean("resumePlayback", resume);
    }

    public void setResumePlaybackLength(long length){
        pref.putLong("resumePlaybackLongerThan", length); // resume if the playback is longer than given time
    }

    public void setShortJumpDuration(long length){
        pref.putLong("shortJumpDuration", length);
    }

    public void setMediumJumpDuration(long length){
        pref.putLong("mediumJumpDuration", length);
    }

    public void setLongJumpDuration(long length){
        pref.putLong("longJumpDuration", length);
    }

    public void setResumePosition(String filename, long position){
        pref.putLong(String.valueOf(filename.hashCode()), position);
    }

    public long getResumePosition(String filename){
        return pref.getLong(String.valueOf(filename.hashCode()), 0);
    }

    public int getVolume(){
        return pref.getInt("volume", 25);
    }

    public void setVolume(int volume){
        pref.putInt("volume", volume);
    }
}
