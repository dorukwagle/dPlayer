package com.doruk.dplayer.controllers;

import com.doruk.dplayer.contracts.Controllers;
import com.doruk.dplayer.contracts.ExtendedMediaPlayerInterface;
import com.doruk.dplayer.mediaplayer.DMediaPlayer;
import com.doruk.dplayer.models.HomeModel;
import com.doruk.dplayer.seekbar.SeekBar;
import com.doruk.dplayer.utilities.PreferencesManager;
import com.doruk.dplayer.utilities.ResourceProvider;
import com.doruk.dplayer.views.*;
import com.doruk.dplayer.views.MenuBar;
import javafx.application.Application.Parameters;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import uk.co.caprica.vlcj.media.*;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.doruk.dplayer.utilities.DurUtils.durationToMillis;
import static com.doruk.dplayer.utilities.DurUtils.millisToDuration;

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

    private Label currentPosition, remainingPosition;
    private SeekBar mediaSlider, volumeSlider;

    private String currentMedia = null;
    private long currentPlaybackPosition = 0;
    private boolean toggleOriginalSize = false;
    private boolean toggleFullScreen = false;

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
        addKeysListeners();


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
                mediaPlayer.setTime(resume);
        });
        mediaPlayer.setOnTimeChanged(this::trackPlaybackProgress);
    }

    private void playMedia(){
        //reset the playback position for tracking resume positions
        currentPlaybackPosition = 0;
        var media = getNextMedia();
        currentMedia = media;
        playMedia(media);
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

        volumeSlider = controlPanel.getVolumeSlider();
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
        //show popup volume label while hovering
        volumeSlider.setShowPopup(this::showVolumePopup);
    }

    private void addMenuBarEventListeners(){
        menuBar.getExit().setOnAction(event -> {
            mediaPlayer.stop();
            Platform.exit();
        });

        menuBar.getPlayBackSpeedGroup().selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            mediaPlayer.setPlayBackSpeed(Float.parseFloat(t1.getUserData().toString()));
        });

        menuBar.getAddSubtitleMenu().setOnAction(event -> {
            String str = mediaList[0].getParent();
            File file = menuBar.chooseSubtitleFile(new File(str));
            menuBar.addSubtitleItem(file.getName().substring(0, 20)).setOnAction(event1 ->
                    mediaPlayer.setSubtitleFile(file.getAbsolutePath()));
        });
    }

    private void monitorPlaybackAndSeekBar(){
        currentPosition = controlPanel.getCurrentPosition();
        remainingPosition = controlPanel.getTotalRemainingPosition();

        mediaSlider = controlPanel.getSeekBar();

        mediaSlider.setOnClick(mouseEvent -> {
            var totalTime = durationToMillis(drawer.getSelectedItem()[2]);
            var ratio = totalTime / mediaSlider.getMax();
            var curTime = (long) (mediaSlider.getControlValue() * ratio);

            mediaPlayer.setTime(curTime);
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
        mediaSlider.setShowPopup(this::showPlayerPopup);
    }

    public void showPlayerPopup(MouseEvent e, Slider slider){
        NumberAxis axis = (NumberAxis) slider.lookup(".axis");
        Point2D locationInAxis = axis.sceneToLocal(e.getSceneX(), e.getSceneY());
        double mouseX = locationInAxis.getX() ;
        double value = axis.getValueForDisplay(mouseX).doubleValue() ;
        if (value >= slider.getMin() && value <= slider.getMax()) {
            var totalTime = durationToMillis(drawer.getSelectedItem()[2]);
            var ratio = totalTime / slider.getMax();
            var curTime = (long) (value * ratio);
            mediaSlider.setPopupText(millisToDuration(curTime));
        } else {
            mediaSlider.setPopupText("__:__:__");
        }
        mediaSlider.setPopupAnchorX(e.getScreenX());
        mediaSlider.setPopupAnchorY(e.getScreenY());
    }

    public void showVolumePopup(MouseEvent e, Slider slider){
        NumberAxis axis = (NumberAxis) slider.lookup(".axis");
        Point2D locationInAxis = axis.sceneToLocal(e.getSceneX(), e.getSceneY());
        double mouseX = locationInAxis.getX() ;
        double value = axis.getValueForDisplay(mouseX).doubleValue();
        if (value >= slider.getMin() && value <= slider.getMax()) {
            volumeSlider.setPopupText("Vol: " + (int)value + "%");
        } else {
            volumeSlider.setPopupText("Volume: __");
        }
        volumeSlider.setPopupAnchorX(e.getScreenX());
        volumeSlider.setPopupAnchorY(e.getScreenY());
    }

    public void trackPlaybackProgress(long newTime){
        //update slider calculations
        var slider = controlPanel.getSeekBar();
        var ratio = mediaPlayer.getDuration() / slider.getMax();
        var sliderPos = newTime / ratio;

        //update the remaining time and current time (calculations)
        long totalTime = mediaPlayer.getDuration();
        String remainingText = (remainingPosition.getText().charAt(0) == '-' ?
                "-" + millisToDuration(totalTime - newTime): drawer.getSelectedItem()[2]);

        //update current time and remaining time
        Platform.runLater(() -> {
            currentPosition.setText(millisToDuration(newTime));
            remainingPosition.setText(remainingText);
        });
        //update the slider
        Platform.runLater(()->slider.setValue(sliderPos));

        //finally save the current playback position for resuming the video next time
        CompletableFuture.runAsync(() -> savePlayBackPosition(newTime));
    }

    private void fetchVideoSubtitles(){
        var a = mediaPlayer.getSubtitles();
        for (var track: a){
            var item = menuBar.addSubtitleItem(track.description());
            item.setOnAction(event -> mediaPlayer.setSubtitle(track.id()));
        }
    }

    private void fetchAudioTracks(){
        var a = mediaPlayer.getAudioTracks();
        for (var track: a) {
            var item = menuBar.addAudioTrackItem(track.description());
            item.setOnAction(event -> mediaPlayer.setAudioTrack(track.id()));
        }
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
            var duration = durationToMillis(time) / 1000 / 60;
            if(duration >= preference.getResumePlaybackLength())
                return preference.getResumePosition(filename);
        }
        return 0;
    }

    private void addKeysListeners(){
        KeyCombination ctrlUp = new KeyCodeCombination(KeyCode.UP, KeyCombination.CONTROL_DOWN);
        KeyCombination ctrlDown = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_DOWN);
        KeyCombination ctrlRight = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN);
        KeyCombination ctrlLeft = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN);

        playerView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            if(ctrlUp.match(event)){
                volumeUp();
                event.consume();
                return;
            }
            if(ctrlDown.match(event)){
                volumeDown();
                event.consume();
                return;
            }
            if(ctrlRight.match(event)){
                longJumpForward();
                event.consume();
                return;
            }
            if(ctrlLeft.match(event)){
                longJumpBackward();
                event.consume();
                return;
            }
            if(code == KeyCode.SPACE) {
                controlPanel.getPlayPause().fire();
                event.consume();
                return;
            }
            if(code == KeyCode.F){
                toggleFullScreen();
                event.consume();
                return;
            }
            if(code == KeyCode.O){
                toggleOriginalVideoSize();
                event.consume();
                return;
            }
            if(code == KeyCode.E){
                displayNextFrame();
                event.consume();
                return;
            }
            if(code == KeyCode.M){
                controlPanel.getAudioBtn().fire();
                event.consume();
                return;
            }
            if(code == KeyCode.LEFT){
                shortJumpBackward();
                event.consume();
                return;
            }
            if(code == KeyCode.RIGHT){
                shortJumpForward();
                event.consume();
                return;
            }
            if(code == KeyCode.UP){
                mediumJumpForward();
                event.consume();
                return;
            }
            if(code == KeyCode.DOWN){
                mediumJumpBackward();
                event.consume();
            }
        });
    }

    private void toggleFullScreen(){

    }

    private void toggleOriginalVideoSize(){

    }

    private void displayNextFrame(){
        mediaPlayer.nextFrame();
    }

    private void volumeUp(){
        var curVolume = volumeSlider.getProgressValue();
        double nextVolume = (curVolume > 145? 150: curVolume + 5);
        volumeSlider.setDoubleValue(nextVolume);
    }

    private void volumeDown(){
        var curVolume = volumeSlider.getProgressValue();
        double nextVolume = (curVolume < 5? 0: curVolume - 5);
        volumeSlider.setDoubleValue(nextVolume);
    }

    private void shortJumpForward(){
        mediaPlayer.seekForward(preference.getShortJumpDuration());
    }

    private void shortJumpBackward(){
        mediaPlayer.seekBackward(preference.getShortJumpDuration());
    }

    private void mediumJumpForward(){
        mediaPlayer.seekForward(preference.getMediumJumpDuration());
    }

    private void mediumJumpBackward(){
        mediaPlayer.seekBackward(preference.getMediumJumpDuration());
    }

    private void longJumpForward(){
        mediaPlayer.seekForward(preference.getLongJumpDuration());
    }

    private void longJumpBackward(){
        mediaPlayer.seekBackward(preference.getLongJumpDuration());
    }


    private void savePlayBackPosition(long position){
        long actualPosition = position;
        if(actualPosition <= mediaPlayer.getDuration() - 4000)
            actualPosition = 0;
        if(Math.abs(actualPosition - currentPlaybackPosition) < 4000) return;
        preference.setResumePosition(currentMedia, actualPosition);
        currentPlaybackPosition = actualPosition;
    }

    private void scaleOnScreenResize(){

    }

    @Override
    public Scene getScene(){
        return scene;
    }

    public void onStop(){
        mediaPlayer.stop();
    }

    public MenuItem getPreferenceButton(){
        return menuBar.getPreference();
    }

    public Button getStopButton(){
        return controlPanel.getStopBtn();
    }
    
}
