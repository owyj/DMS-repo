package com.comp2042.tetris.input;

import com.comp2042.tetris.dto.GameStateView;
import com.comp2042.tetris.dto.MoveEvent;

public interface GameInputCallback {
    void onMove(GameStateView gameStateView);
    void onMoveDown(MoveEvent event);
    void onInstantDrop(MoveEvent event);
    void onHold(MoveEvent event);
    void onNewGame();
    void onPause();
}
