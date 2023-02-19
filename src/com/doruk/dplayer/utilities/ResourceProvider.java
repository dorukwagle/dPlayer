package com.doruk.dplayer.utilities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class ResourceProvider {
    private HashMap<String, String> iconsMapDark;
    private HashMap<String, String> iconsMapLight;
    private HashMap<String, String> commonMaps;
    private PreferencesManager preferences;

    public ResourceProvider(){
        iconsMapDark = new HashMap<>();
        iconsMapLight = new HashMap<>();
        commonMaps = new HashMap<>();
        preferences = new PreferencesManager();

        commonMaps.putAll(Map.of(
                "media_folder", "/res/media_folder.png",
                "music_folder", "/res/music_folder.png",
                "video_folder", "/res/video_folder.png",
                "video_icon", "/res/video_icon.png",
                "music_icon", "/res/music_icon.png"
        ));


        iconsMapDark.putAll(Map.of(
                "navigation_drawer_icon", "/res/navigation_drawer_black.png",
                "next_icon", "/res/next_icon_black.png",
                "pause_icon", "/res/pause_icon_black.png",
                "play_icon", "/res/play_icon_black.png",
                "previous_icon", "/res/previous_icon_black.png",
                "settings_icon", "/res/settings_icon_black.png",
                "stop_icon", "/res/stop_icon_black.png",
                "subtitles_icon", "/res/subtitles_icon_black.png",
                "volume_max_icon", "/res/volume_max_icon_black.png",
                "volume_mute_icon", "/res/volume_mute_icon_black.png"
        ));

        iconsMapLight.putAll(Map.of(
                "navigation_drawer_icon", "/res/navigation_drawer_white.png",
                "next_icon", "/res/next_icon_white.png",
                "pause_icon", "/res/pause_icon_white.png",
                "play_icon", "/res/play_icon_white.png",
                "previous_icon", "/res/previous_icon_white.png",
                "settings_icon", "/res/settings_icon_white.png",
                "stop_icon", "/res/stop_icon_white.png",
                "subtitles_icon", "/res/subtitles_icon_white.png",
                "volume_max_icon", "/res/volume_max_icon_white.png",
                "volume_mute_icon", "/res/volume_mute_icon_white.png"
        ));
    }

    private ImageView createGraphic(String path, int height, int width){
        ImageView img = new ImageView(
                new Image(this.getClass().getResourceAsStream(path))
        );
        img.setPreserveRatio(true);
        img.setFitHeight(height);
        img.setFitWidth(width);
        return img;
    }

    public ImageView getIcon(String iconName, int height, int width){
        // check if the iconName exists in the hashMap
        if(!keyExists(iconName)){
            System.out.println(iconName + ": icon not found");
            System.exit(-1);
        }
        if(commonMaps.containsKey(iconName))
            return createGraphic(commonMaps.get(iconName), height, width);

        // here first read the theme and load accordingly
        boolean lightTheme = preferences.isLightThemeChecked();

        String path = (lightTheme? iconsMapDark.get(iconName): iconsMapLight.get(iconName));
        return createGraphic(path, height, width);
    }

    public String getCssFile(){
        String path = "";
        if(preferences.isLightThemeChecked())
            path = "";
        else
            path = "/assets/dark_theme_adv.css";
        return this.getClass().getResource(path).toString();
    }

    private boolean keyExists(String key){
        return (iconsMapLight.containsKey(key) || iconsMapDark.containsKey(key)
                || commonMaps.containsKey(key));
    }

}
