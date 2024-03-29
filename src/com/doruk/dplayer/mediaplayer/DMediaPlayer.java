package com.doruk.dplayer.mediaplayer;

import com.doruk.dplayer.contracts.ExtendedMediaPlayerInterface;
import com.doruk.dplayer.contracts.MediaPlayCompleted;
import com.doruk.dplayer.contracts.OnPlaybackStart;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.fullscreen.JavaFXFullScreenStrategy;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.TrackDescription;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class DMediaPlayer implements ExtendedMediaPlayerInterface {

    private ImageView mediaView;
    private final MediaPlayerFactory mediaPlayerFactory;

    private EmbeddedMediaPlayer embeddedMediaPlayer;
    private MediaPlayCompleted onComplete;

    private List<OnPlaybackStart> onStartEvents;
    private Consumer<Long> timeChanged;

    private boolean isPlayingFinished = false;

    public DMediaPlayer() {
        onStartEvents = new ArrayList<>();
        this.mediaPlayerFactory = new MediaPlayerFactory("--no-video-title-show", "avcodec-hw+", "--disable-screensaver");
        this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                CompletableFuture.runAsync(() -> onStart());
                CompletableFuture.runAsync(() -> isPlayingFinished = false);
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void finished(MediaPlayer mediaPlayer){
                if(onComplete == null)
                    return;
                CompletableFuture.runAsync(onComplete::onComplete);
                CompletableFuture.runAsync(() -> isPlayingFinished = true);
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                if (timeChanged == null) return;
                CompletableFuture.runAsync(() -> timeChanged.accept(newTime));
            }
        });
        this.mediaView = new ImageView();
        this.mediaView.setPreserveRatio(true);

        embeddedMediaPlayer.videoSurface().set(new ImageViewVideoSurface(this.mediaView));
        embeddedMediaPlayer.video().setAdjustVideo(true);
    }

    @Override
    public void load(String filePath) {
        embeddedMediaPlayer.media().play(filePath);
        embeddedMediaPlayer.controls().pause();
        embeddedMediaPlayer.controls().setPosition(0f);
    }

    @Override
    public void scaleToScreen(Dimension dimension){
        var width = dimension.getWidth();
        var height = dimension.getHeight();
        var vdoDimension = embeddedMediaPlayer.video().videoDimension();
        var vWidth = vdoDimension.getWidth();
        var vHeight = vdoDimension.getHeight();

        double heightRatio = vHeight / height;
        double widthRatio = vWidth / width;
        mediaView.setPreserveRatio(true);
        if ( vWidth / heightRatio <= width) {
            mediaView.setFitHeight(height);
        }
        else if(vHeight / widthRatio <= height) {
            mediaView.setFitWidth(width);
            //here check again if while fitting the width, the height exceeds the bounds
        }
        else{
            mediaView.setFitWidth(width - 100);
        }
    }

    @Override
    public Dimension getVideoDimension(){
        return embeddedMediaPlayer.video().videoDimension();
    }

    @Override
    public void play(){
        embeddedMediaPlayer.controls().play();
    }

    @Override
    public void pause() {
        embeddedMediaPlayer.controls().pause();
    }

    @Override
    public void stop(){
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.release();
        mediaPlayerFactory.release();
    }

    @Override
    public void seek(int seconds) {
        embeddedMediaPlayer.controls().setPosition(0.1f);
    }

    @Override
    public void setOriginalSize(){
        Dimension videoDimension = embeddedMediaPlayer.video().videoDimension();
        mediaView.setPreserveRatio(false);
        mediaView.setFitHeight(videoDimension.getHeight());
        mediaView.setFitWidth(videoDimension.getWidth());
    }

    @Override
    public long getCurrentTime() {
        return embeddedMediaPlayer.status().time();
    }

    @Override
    public long getDuration(){
        return embeddedMediaPlayer.media().info().duration();
    }

    @Override
    public void seekForward(long seconds) {
        embeddedMediaPlayer.controls().skipTime(seconds * 1000L);
    }

    @Override
    public void seekBackward(long second) {
        embeddedMediaPlayer.controls().skipTime(-(second * 1000L));
    }

    @Override
    public void setTime(long millis){
        embeddedMediaPlayer.controls().setTime(millis);
    }

    @Override
    public void setVolume(int volume) {
        embeddedMediaPlayer.audio().setVolume(volume);
    }

    @Override
    public int getVolume(){
        return embeddedMediaPlayer.audio().volume();
    }

    @Override
    public boolean isPlaying(){
        return embeddedMediaPlayer.status().isPlaying();
    }


    @Override
    public void setOnComplete(MediaPlayCompleted function) {
        onComplete = function;
    }

    @Override
    public List<TrackDescription> getSubtitles() {
        return embeddedMediaPlayer.subpictures().trackDescriptions();
    }

    @Override
    public List<TrackDescription> getAudioTracks() {
        return embeddedMediaPlayer.audio().trackDescriptions();
    }

    @Override
    public void setSubtitle(int id) {
        embeddedMediaPlayer.subpictures().setTrack(id);
    }

    @Override
    public void setAudioTrack(int id) {
        embeddedMediaPlayer.audio().setTrack(id);
    }

    @Override
    public void setSubtitleFile(String filename){
        embeddedMediaPlayer.subpictures().setSubTitleFile(filename);
    }

    @Override
    public ImageView getMediaView(){
        return mediaView;
    }

    @Override
    public void nextFrame() {
        embeddedMediaPlayer.controls().nextFrame();
    }

    @Override
    public void previousFrame() {

    }

    @Override
    public void playAudioOnly() {

    }

    @Override
    public void setPlayBackSpeed(float speed){
        embeddedMediaPlayer.controls().setRate(speed);
    }

    @Override
    public void addOnStartEvents(OnPlaybackStart event) {
        onStartEvents.add(event);
    }

    @Override
    public void setOnTimeChanged(Consumer<Long> consumer){
        timeChanged = consumer;
    }

    private void onStart(){
        onStartEvents.forEach(event -> CompletableFuture.runAsync(event::onStart));
        onStartEvents.clear();
    }
}
