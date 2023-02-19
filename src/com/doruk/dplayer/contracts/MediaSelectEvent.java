package com.doruk.dplayer.contracts;

import java.io.File;
import java.util.List;

public interface MediaSelectEvent {
    void onSelectionComplete(File[] medias);
}
