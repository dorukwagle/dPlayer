package com.doruk.dplayer.utilities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconsProvider {
    public ImageView createGraphic(String path, int height, int width){
        ImageView img = new ImageView(
                new Image(this.getClass().getResourceAsStream(path))
        );
        img.setPreserveRatio(true);
        img.setFitHeight(height);
        img.setFitWidth(width);
        return img;
    }
}
