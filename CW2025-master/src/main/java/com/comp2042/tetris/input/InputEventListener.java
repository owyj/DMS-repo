package com.comp2042.tetris.input;

import com.comp2042.tetris.dto.DownData;
import com.comp2042.tetris.dto.MoveEvent;
import com.comp2042.tetris.dto.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    DownData onInstantDropEvent(MoveEvent event); //Instant drop

    ViewData onHoldEvent(MoveEvent event); //piece storage

    void createNewGame();
}
