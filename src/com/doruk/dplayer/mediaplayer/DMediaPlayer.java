package com.doruk.dplayer.mediaplayer;

import com.doruk.dplayer.contracts.MediaPlayCompleted;
import com.doruk.dplayer.contracts.MediaPlayerInterface;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.fullscreen.JavaFXFullScreenStrategy;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class DMediaPlayer implements MediaPlayerInterface {

    private ImageView mediaView;
    private final MediaPlayerFactory mediaPlayerFactory;

    private EmbeddedMediaPlayer embeddedMediaPlayer;
    private MediaPlayCompleted onComplete;

    private Dimension fitScreen;


    public DMediaPlayer() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                if(fitScreen != null)
                    scaleToScreen(fitScreen);
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
                Platform.runLater(onComplete::onComplete);
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
            }

        });
        this.mediaView = new ImageView();
        this.mediaView.setPreserveRatio(true);

        embeddedMediaPlayer.videoSurface().set(new ImageViewVideoSurface(this.mediaView));

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

        mediaView.setPreserveRatio(true);
        if ( vWidth / heightRatio <= width)
            mediaView.setFitHeight(height);
        else
            mediaView.setFitWidth(width);
    }

    @Override
    public void setFitToScreen(Dimension dimension){
        fitScreen = dimension;
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
    public void setFullScreen(Stage stage){
        embeddedMediaPlayer.fullScreen().strategy(new JavaFXFullScreenStrategy(stage));
        embeddedMediaPlayer.fullScreen().set(true);
        embeddedMediaPlayer.fullScreen().toggle();
    }

    @Override
    public void setOriginalSize(){
        Dimension videoDimension = embeddedMediaPlayer.video().videoDimension();
        mediaView.setPreserveRatio(false);
        mediaView.setFitHeight(videoDimension.getHeight());
        mediaView.setFitWidth(videoDimension.getWidth());
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekForward(int seconds) {

    }

    @Override
    public void seekBackward(int second) {

    }

    @Override
    public void setVolume(int volume) {

    }

    @Override
    public int getVolume() {
        return 0;
    }

    @Override
    public void setOnComplete(MediaPlayCompleted function) {
        onComplete = function;
    }

    @Override
    public List<Object> getSubtitles() {
        return null;
    }

    @Override
    public List<Object> getAudioTracks() {
        return null;
    }

    @Override
    public void setMediaType(String media) {

    }

    @Override
    public boolean isVideo() {
        return false;
    }

    @Override
    public void setSubtitle() {

    }

    @Override
    public void loadSubtitleFile() {

    }

    @Override
    public void setAudioTrack() {

    }

    @Override
    public ImageView getMediaView(){
        return mediaView;
    }
}
