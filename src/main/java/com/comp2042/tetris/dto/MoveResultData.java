package com.comp2042.tetris.dto;

/**
 * Data Transfer Object (DTO) that encapsulates the result of a move action in the game.
 * Contains information about any cleared rows and the updated game state view after the move.
 * This class is immutable (final) to ensure data integrity when passed between components.
 */
public final class MoveResultData {
    private final ClearRow clearRow;
    private final GameStateView gameStateView;

    /**
     * Constructor: Initializes all fields
     *
     * @param clearRow Information about any rows cleared as a result of the move
     * @param gameStateView The updated visual state of the game after the move
     */
    public MoveResultData(ClearRow clearRow, GameStateView gameStateView) {
        this.clearRow = clearRow;
        this.gameStateView = gameStateView;
    }

    // Getters: gets information about cleared rows
    public ClearRow getClearRow() {
        return clearRow;
    }

    // Getters: gets the updated game state view
    public GameStateView getViewData() {
        return gameStateView;
    }
}
