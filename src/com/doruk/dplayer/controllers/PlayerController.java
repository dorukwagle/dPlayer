package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.contracts.MediaPlayerInterface;
import com.doruk.dplayer.mediaplayer.DMediaPlayer;
import com.doruk.dplayer.models.HomeModel;
import com.doruk.dplayer.utilities.ResourceProvider;
import com.doruk.dplayer.views.Drawer;
import com.doruk.dplayer.views.MenuBar;
import com.doruk.dplayer.views.PlayerView;
import com.doruk.dplayer.views.VideoControlPanel;
import javafx.application.Application.Parameters;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.media.*;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import java.awt.*;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class PlayerController implements Controllers {
    private MediaPlayerInterface mediaPlayer;
    private ImageView mediaView;
    private Drawer drawer;
    private final PlayerView playerView;
    private final VideoControlPanel controlPanel;
    private final MenuBar menuBar;
    private final Scene scene;
    private File[] mediaList;
    private int currentPlaylistPosition = 0;
    private Dimension playerViewDimensions;

    public PlayerController(){
        ResourceProvider icons = new ResourceProvider();
        mediaPlayer = new DMediaPlayer();
        mediaView = mediaPlayer.getMediaView();
        menuBar = new MenuBar(icons);
        controlPanel = new VideoControlPanel(icons);
        playerView = new PlayerView(mediaView, menuBar, controlPanel);
        drawer = playerView.getDrawer();
        scene = new Scene(playerView);

        controlPanel.getDrawerBtn().setOnAction(e -> {
            var check = drawer.isHidden();
            if (!check) {
                drawer.hide();
            } else {
                drawer.show();
            }
        });


        drawer.setOnClick((index, listItem) -> {
                playMedia(mediaList[index].getAbsolutePath());
                currentPlaylistPosition = ++index;
            }
        );
        mediaPlayer.setOnComplete(this::playMedia);

        getPlayerViewDimensions();
    }

    public PlayerController(Parameters params) {
        this();
        HomeModel model = new HomeModel();
        if (params.getRaw().size() > 1)
            mediaList = model.readSystemArguments(params.getRaw());
        else
            mediaList = model.readMediaDirectory(new File(params.getRaw().get(0)).getParentFile());

        updatePlayList();
        startPlaying();
    }

    public PlayerController(File[] params){
        this();
        this.mediaList = params;

        updatePlayList();
        startPlaying();
    }

    private void startPlaying(){
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(600);
                playMedia();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void getPlayerViewDimensions(){
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500);
                // fit the media player height to the screen
                float height = (float) (playerView.getHeight() - menuBar.getHeight() - controlPanel.getHeight());
                playerViewDimensions = new Dimension();
                playerViewDimensions.setSize(playerView.getWidth(), height);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void playMedia(String filename){
        if(filename == null)
            return;
        mediaPlayer.load(filename);
        mediaPlayer.play();
        mediaPlayer.setFitToScreen(playerViewDimensions);
    }

    private void playMedia(){
        playMedia(getNextMedia());
    }

    private String getNextMedia(){
//        currentPlaylistPosition = (currentPlaylistPosition + 1) % mediaList.length;
        if(currentPlaylistPosition == mediaList.length)
            return null;
        return getMediaFromList(currentPlaylistPosition++);
    }

    private String getPreviousMedia(){
        if(currentPlaylistPosition < 0)
            return null;
        return getMediaFromList(currentPlaylistPosition--);
    }

    private String getMediaFromList(int position){
        drawer.selectItem(position);
        return mediaList[position].getAbsolutePath();
    }



    private void updatePlayList(){
        CompletableFuture.runAsync(() -> {
            final long[] totalDur = {0};
            final int[] count = {0};
            String[][] playList = new String[1][1];
            for (File file : mediaList) {
                EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();

                player.mediaPlayer().media().prepare(file.getAbsolutePath());
                final boolean[] parsed = {false};
                count[0]++;
                player.mediaPlayer().media().events().addMediaEventListener(new MediaEventAdapter() {
                    @Override
                    public void mediaParsedChanged(Media media, MediaParsedStatus status) {
                        long mediaDur = media.info().duration();
                        totalDur[0] += mediaDur;
                        String fileName = media.meta().get(Meta.TITLE);
                        String duration = millisToDuration(mediaDur);

                        playList[0] = new String[]{String.valueOf(count[0]) , fileName, duration};

                        parsed[0] = status.toString().equals("DONE");
                        player.release();
                    }
                });
                var a = player.mediaPlayer().media().parsing().parse(ParseFlag.PARSE_LOCAL);
                while (!parsed[0]) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // add the filename to the drawer
                Platform.runLater(() -> drawer.addItem(playList[0]));
            }

            String totalDuration = millisToDuration(totalDur[0]);
            Platform.runLater(() -> drawer.getTotalDuration().setText("Total Duration: " + totalDuration));

        });
    }

    private String millisToDuration(long dur){
        long millis = dur;
        long hours = millis / (60 * 60 * 1000);  // convert to hours
        millis %= (60 * 60 * 1000);
        long minutes = millis / (60 * 1000); // convert to minutes
        millis %= (60 * 1000);
        long seconds = millis / 1000; // convert to seconds
        return (hours/10 > 1? hours: "0"+hours) + ":" +
                (minutes/10 > 0? minutes: "0"+minutes) + ":" +
                (seconds/10 > 0? seconds: "0"+seconds);
    }

    @Override
    public Scene getScene(){
        return scene;
    }

    public Button getNext(){
        return null;
    }

    public MenuItem getPreferenceButton(){
        return menuBar.getPreference();
    }

    public Button getStopButton(){
        return controlPanel.getStopBtn();
    }
}
