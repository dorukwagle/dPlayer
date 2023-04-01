package com.doruk.dplayer.models;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PlayerModel {
    public String saveScreenShot(ImageView imageView) throws IOException {

        String home = System.getProperty("user.home");
        File file = Paths.get(home, "Pictures", "dPlayer-Screenshot-" + System.currentTimeMillis() + ".png").toFile();
        BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
        ImageIO.write(bImage, "png", file);
        return file.toString();
    }
}
