package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.contracts.ExtendedMediaPlayerInterface;
import com.doruk.dplayer.mediaplayer.DMediaPlayer;
import com.doruk.dplayer.models.HomeModel;
import com.doruk.dplayer.utilities.PreferencesManager;
import com.doruk.dplayer.utilities.ResourceProvider;
import com.doruk.dplayer.views.Drawer;
import com.doruk.dplayer.views.MenuBar;
import com.doruk.dplayer.views.PlayerView;
import com.doruk.dplayer.views.VideoControlPanel;
import javafx.application.Application.Parameters;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.media.*;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.doruk.dplayer.utilities.DurUtils.*;

public class PlayerController implements Controllers {
    private ExtendedMediaPlayerInterface mediaPlayer;
    private ImageView mediaView;
    private Drawer drawer;
    private final PlayerView playerView;
    private final VideoControlPanel controlPanel;
    private final MenuBar menuBar;
    private final Scene scene;
    private File[] mediaList;
    private int currentPlaylistPosition = -1;
    private Dimension playerViewDimensions;
    private PreferencesManager preference;
    private ResourceProvider icons;

    public PlayerController(){
        icons = new ResourceProvider();
        preference = new PreferencesManager();

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
                currentPlaylistPosition = index;
            }
        );
        mediaPlayer.setOnComplete(this::playMedia);

        getPlayerViewDimensions();
        addControlPanelEventListeners();
        addMenuBarEventListeners();
        mediaPlayer.addOnStartEvents(this::monitorPlaybackAndSeekBar);
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

        // adjust video screen size and volume for each video
        mediaPlayer.addOnStartEvents(() -> {
            mediaPlayer.scaleToScreen(playerViewDimensions);
            mediaPlayer.setVolume((int) controlPanel.getVolumeSlider().getProgressValue());
        });
        // fetch the subtitles and audio tracks from video and update the menu-bar lists
        mediaPlayer.addOnStartEvents(this::fetchVideoSubtitles);
        mediaPlayer.addOnStartEvents(this::fetchAudioTracks);

        // track the video playback and resume if necessary
        mediaPlayer.addOnStartEvents(() -> {
            long resume = getResumeDuration(filename);
            if(resume > 0)
                mediaPlayer.addOnStartEvents(() -> mediaPlayer.setTime(resume));
        });
        mediaPlayer.addOnStartEvents(this::trackPlaybackProgress);
    }

    private void playMedia(){
        playMedia(getNextMedia());
    }

    private String getNextMedia(){
//        currentPlaylistPosition = (currentPlaylistPosition + 1) % mediaList.length;
        if(currentPlaylistPosition == mediaList.length - 1)
            return null;
        return getMediaFromList(++currentPlaylistPosition);
    }

    private String getPreviousMedia(){
        if(currentPlaylistPosition == 0)
            return null;
        return getMediaFromList(--currentPlaylistPosition);
    }

    private String getMediaFromList(int position){
        drawer.selectItem(position);
        return mediaList[position].getAbsolutePath();
    }

    private void addControlPanelEventListeners(){
        var playPause = controlPanel.getPlayPause();
        playPause.setOnAction(event -> {
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.play();
                playPause.setGraphic(icons.getIcon("pause_icon", 20, 20));
                return;
            }
            mediaPlayer.pause();
            playPause.setGraphic(icons.getIcon("play_icon", 25, 25));
        });

        var previous = controlPanel.getPreviousBtn();
        previous.setOnAction(event -> playMedia(getPreviousMedia()));

        var next = controlPanel.getNextBtn();
        next.setOnAction(event -> playMedia(getNextMedia()));

        var volumeSlider = controlPanel.getVolumeSlider();
        var volumeLabel = controlPanel.getVolumeLabel();
        // update slider position
        volumeSlider.setValue(preference.getVolume());
        volumeLabel.setText(preference.getVolume()+"%");
        volumeSlider.setOnValueChange((observableValue, oldValue, newValue) -> {
            volumeLabel.setText(newValue.intValue() + "%");
            mediaPlayer.setVolume(newValue.intValue());
            preference.setVolume(newValue.intValue());
        });

        var volumeBtn = controlPanel.getAudioBtn();
//        if(preference.getVolume)
        volumeBtn.setOnAction(event -> {
            if(mediaPlayer.getVolume() > 0) {
                mediaPlayer.setVolume(0);
                volumeBtn.setGraphic(icons.getIcon("volume_mute_icon", 20, 20));
                return;
            }
            mediaPlayer.setVolume((int)volumeSlider.getProgressValue());
            volumeBtn.setGraphic(icons.getIcon("volume_max_icon", 20, 20));
        });
    }

    private void addMenuBarEventListeners(){

    }

    private void monitorPlaybackAndSeekBar(){
        var currentPosition = controlPanel.getCurrentPosition();
        var remainingPosition = controlPanel.getTotalRemainingPosition();

        var mediaSlider = controlPanel.getSeekBar();

        mediaSlider.setOnClick(mouseEvent -> {
            var totalTime = durationToMillis(drawer.getSelectedItem()[2]);
            var ratio = totalTime / mediaSlider.getMax();
            var curTime = (long) (mediaSlider.getControlValue() * ratio);

            mediaPlayer.setTime(curTime / 1000);
        });

        mediaSlider.setOnValueChange((observableValue, oldValue, newValue) -> {
            var totalTime = durationToMillis(drawer.getSelectedItem()[2]);
            var ratio = totalTime / mediaSlider.getMax();
            var curTime = (long) (mediaSlider.getProgressValue() * ratio);

            currentPosition.setText(millisToDuration(curTime));
            var remainingText = (remainingPosition.getText().charAt(0) == '-' ?
                    "-" + millisToDuration(totalTime - curTime): drawer.getSelectedItem()[2]);
            remainingPosition.setText(remainingText);
        });

        remainingPosition.setOnMouseClicked(mouseEvent -> {
            if(remainingPosition.getText().charAt(0) == '-') {
                remainingPosition.setText(drawer.getSelectedItem()[2]);
                return;
            }
            var curTime = durationToMillis(currentPosition.getText());
            var totalTime = durationToMillis(remainingPosition.getText());
            remainingPosition.setText("-" + millisToDuration(totalTime - curTime));
        });

        // show time popups while playing hovering on the slider
        mediaSlider.setShowPopup((e, slider) -> {
            NumberAxis axis = (NumberAxis) slider.lookup(".axis");
            Point2D locationInAxis = axis.sceneToLocal(e.getSceneX(), e.getSceneY());
            double mouseX = locationInAxis.getX() ;
            double value = axis.getValueForDisplay(mouseX).doubleValue() ;
            if (value >= slider.getMin() && value <= slider.getMax()) {
                mediaSlider.setPopupText(String.format("Value: %.1f", value));
            } else {
                mediaSlider.setPopupText("Value: ---");
            }
            mediaSlider.setPopupAnchorX(e.getScreenX());
            mediaSlider.setPopupAnchorY(e.getScreenY());
        });
    }

    public void trackPlaybackProgress(){
        var slider = controlPanel.getSeekBar();

        while(mediaPlayer.isPlaying()){
            try {
                var curTime = mediaPlayer.getCurrentTime();
                var ratio = mediaPlayer.getDuration() / slider.getMax();
                var sliderPos = curTime / ratio;
                Platform.runLater(()->slider.setValue(sliderPos));

                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // continue tracking after pause or next media
        mediaPlayer.addOnStartEvents(this::trackPlaybackProgress);
    }

    private void fetchVideoSubtitles(){
        var a = mediaPlayer.getSubtitles();
        System.out.println(a.toString());
    }

    private void fetchAudioTracks(){
        var a = mediaPlayer.getAudioTracks();
        System.out.println(a.toString());
    }


    private void updatePlayList(){
        CompletableFuture.runAsync(() -> {
            final long[] totalDur = {0};
            final int[] count = {0};
            List<String[]> playList = new ArrayList<>();
            for (int i = 0; i < mediaList.length; ++i) {
                EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();

                player.mediaPlayer().media().prepare(mediaList[i].getAbsolutePath());
                final boolean[] parsed = {false};
                count[0]++;
                player.mediaPlayer().media().events().addMediaEventListener(new MediaEventAdapter() {
                    @Override
                    public void mediaParsedChanged(Media media, MediaParsedStatus status) {
                        long mediaDur = media.info().duration();
                        totalDur[0] += mediaDur;
                        String fileName = media.meta().get(Meta.TITLE);
                        String duration = millisToDuration(mediaDur);

                        playList.add(new String[]{String.valueOf(count[0]) , fileName, duration});

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
                int ii = i;
                Platform.runLater(() -> drawer.addItem(playList.get(ii)));
//                drawer.addItem(playList[0]);
            }

            String totalDuration = millisToDuration(totalDur[0]);
            Platform.runLater(() -> drawer.getTotalDuration().setText("Total Duration: " + totalDuration));
        });
    }

    private long getResumeDuration(String filename){
        if(preference.isResumePlayback()){
            var time = drawer.getSelectedItem()[2];
            var duration = durationToMillis(drawer.getSelectedItem()[2]) / 1000;
            if(duration >= preference.getResumePlaybackLength())
                return preference.getResumePosition(filename);
        }
        return 0;
    }

    @Override
    public Scene getScene(){
        return scene;
    }

    public void onStop(){
        mediaPlayer.stop();
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
