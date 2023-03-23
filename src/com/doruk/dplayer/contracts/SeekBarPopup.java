package com.doruk.dplayer.contracts;

import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public interface SeekBarPopup {
    public void show(MouseEvent e, Slider slider);
}
