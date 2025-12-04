package com.comp2042.tetris.input;

import com.comp2042.tetris.dto.MoveResultData;
import com.comp2042.tetris.dto.MoveEvent;
import com.comp2042.tetris.dto.GameStateView;

public interface InputEventListener {

    MoveResultData onDownEvent(MoveEvent event);

    GameStateView onLeftEvent(MoveEvent event);

    GameStateView onRightEvent(MoveEvent event);

    GameStateView onRotateEvent(MoveEvent event);

    MoveResultData onInstantDropEvent(MoveEvent event); //Instant drop

    GameStateView onHoldEvent(MoveEvent event); //piece storage

    void createNewGame();
}
