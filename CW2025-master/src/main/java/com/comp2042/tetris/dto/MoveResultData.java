package com.comp2042.tetris.dto;

public final class MoveResultData {
    private final ClearRow clearRow;
    private final GameStateView gameStateView;

    public MoveResultData(ClearRow clearRow, GameStateView gameStateView) {
        this.clearRow = clearRow;
        this.gameStateView = gameStateView;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public GameStateView getViewData() {
        return gameStateView;
    }
}
