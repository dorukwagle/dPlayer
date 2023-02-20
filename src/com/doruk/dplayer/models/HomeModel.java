package com.doruk.dplayer.models;

import java.io.File;
import java.util.ArrayList;
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
        return readDirectory(path, audioFilesFormat);
    }


    public File[] readVideoDirectory(File path){
        return readDirectory(path, videoFilesFormat);
    }

    public File[] readSystemArguments(List<String> files){
        List<String> formats = getMediaFormats();
        return files.stream().map(File::new).filter(file -> file.isFile()).filter(file -> {
            String[] tmp = file.getName().toLowerCase().split("\\.");
            var ext = tmp[tmp.length - 1];
            return formats.contains(ext);
        }).toArray(File[]::new);
    }

    public File[] readMediaDirectory(File path){
        return readDirectory(path, getMediaFormats());
    }

    private File[] readDirectory(File path, List<String> match){
        if(path == null)
            return null;
        return path.listFiles((dir, fileName) -> {
            String[] fls = fileName.toLowerCase().split("\\.");
            String ext = fls[fls.length - 1];
            return match.contains(ext);
        });
    }

    private List<String> getMediaFormats() {
        List<String> newList = new ArrayList<>();
        newList.addAll(audioFilesFormat);
        newList.addAll(videoFilesFormat);
        return newList;
    }
}
