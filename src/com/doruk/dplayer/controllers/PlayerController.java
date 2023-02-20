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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import javafx.scene.control.MenuItem;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.File;
import java.util.Arrays;
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

    public PlayerController(){
        ResourceProvider icons = new ResourceProvider();
        mediaPlayer = new DMediaPlayer();
        mediaView = mediaPlayer.getMediaView();
        menuBar = new MenuBar(icons);
        controlPanel = new VideoControlPanel(icons);
        playerView = new PlayerView(mediaView, menuBar, controlPanel);
        drawer = playerView.getDrawer();
        scene = new Scene(playerView);

//        try {
//            mediaPlayer.load("/home/doruk/Downloads/Video/test3.mkv");
////            mediaPlayer.load("/home/doruk/Downloads/Video/song.mp4");
////            mediaPlayer.load("/data/Movies/Gangster_(2022)_Hindi_Dubbed_720p.mp4");
//        } catch (FileNotFoundException e) {
//            System.out.println(e.toString());
//        }

        controlPanel.getDrawerBtn().setOnAction(e -> {
            var check = drawer.isHidden();
            if (!check) {
                drawer.hide();
            } else {
                drawer.show();
            }
        });

        // fit the media player height to the screen
        CompletableFuture.runAsync(()->{
            try {
                Thread.sleep(500);
                float height = (float) (playerView.getHeight() - menuBar.getHeight() - controlPanel.getHeight());
                mediaPlayer.setHeight(height);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public PlayerController(Parameters params) {
        this();
        HomeModel model = new HomeModel();
        if (params.getRaw().size() > 1)
            mediaList = model.readSystemArguments(params.getRaw());
        else
            mediaList = model.readMediaDirectory(new File(params.getRaw().get(0)).getParentFile());
        updatePlayList();
    }

    public PlayerController(File[] params){
        this();
        this.mediaList = params;
        updatePlayList();
    }

    private void updatePlayList(){

        CompletableFuture.runAsync(() -> {
            long totalDur = 0;
            MediaPlayerFactory factory = new MediaPlayerFactory();
            EmbeddedMediaPlayer player = factory.mediaPlayers().newEmbeddedMediaPlayer();
            for (int i = 0; i < mediaList.length; i++){
                long mediaDur = 0;
                player.media().startPaused(mediaList[i].getAbsolutePath());
                mediaDur = player.status().length();
                totalDur += mediaDur;
                drawer.addItem(
                        (i+1) + "> " +
                        mediaList[i].getName().substring(0, 50) + "..." +
                        " | " + millisToDuration(mediaDur)
                );
            }
            drawer.getTotalDuration().setText("Total Duration: " + totalDur);
        });
    }

    private String millisToDuration(long millis){
        long hours = millis /= (60 * 60 * 1000);  // convert to hours
        long minutes = millis /= (60 * 1000); // convert to minutes
        long seconds = millis /= 1000; // convert to seconds
        return hours + ":" + millis + ":" + seconds;
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
