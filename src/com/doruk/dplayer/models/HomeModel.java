package com.doruk.dplayer.models;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class HomeModel {
    private List<String> audioFilesFormat;
    private List<String> videoFilesFormat;

    public HomeModel(){
        audioFilesFormat = List.of("mp3", "ogg", "aac", "m4a", "flac", "wav", "wma", "alac");
        videoFilesFormat = List.of("mp4", "mov", "wmv", "avi", "flv", "mkv", "webm", "mpeg-2", "swf");
    }

    public File[] validateChosenFiles(List<File> files){
        if(files == null)
            return null;
        if (files.size() == 0)
            return null;
        var result =  files.stream().filter(file -> {
            if (!file.isFile())
                return false;
            var parts = file.getName().split("\\.");
            var ext = parts[parts.length - 1];
            return audioFilesFormat.contains(ext) || videoFilesFormat.contains(ext);
        }).toList();
        if(result.size() == 0)
            return null;
        return result.toArray(File[]::new);
    }

    public File[] readAudioDirectory(File path){
        if (path == null)
            return null;
        return path.listFiles((dir, fileName) -> {
            String[] fls = fileName.toLowerCase().split("\\.");
            String ext = fls[fls.length - 1];
            return audioFilesFormat.contains(ext);
        });
    }


    public File[] readVideoDirectory(File path){
        if(path == null)
            return null;
        return path.listFiles((dir, fileName) -> {
            String[] fls = fileName.toLowerCase().split("\\.");
            String ext = fls[fls.length - 1];
            return videoFilesFormat.contains(ext);
        });
    }
}
