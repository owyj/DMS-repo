package com.comp2042.tetris.dto;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Data Transfer Object (DTO) that encapsulates the complete visual state of the game.
 * Contains all information needed by the view layer to render the current game state, including the active brick, preview brick, held brick, and ghost piece positions.
 * This class is immutable (final) to ensure thread-safe data transfer between the game model and view components.
 */
public final class GameStateView {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int[][] heldBrickData;
    private final int ghostXPosition;
    private final int ghostYPosition;

    /**
     * Constructor: A complete game state view with all visual information.
     *
     * @param brickData Matrix of the current active brick
     * @param xPosition X coordinate of the current brick
     * @param yPosition Y coordinate of the current brick
     * @param nextBrickData Matrix of the next brick to appear
     * @param heldBrickData Matrix of the brick currently held in storage
     * @param ghostXPosition X coordinate of the ghost piece
     * @param ghostYPosition Y coordinate of the ghost piece
     */
    public GameStateView(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] heldBrickData, int ghostXPosition, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.heldBrickData = heldBrickData;
        this.ghostXPosition = ghostXPosition;
        this.ghostYPosition = ghostYPosition;
    }

    // Getters: gets a copy of the current brick matrix
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    // Getters: gets the X position of the current brick
    public int getXPosition() {
        return xPosition;
    }

    // Getters: gets the Y position of the current brick
    public int getYPosition() {
        return yPosition;
    }

    // Getters: gets a copy of the next brick matrix
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    // Getters: gets a copy of the held brick matrix
    public int[][] getHeldBrickData() {
        return MatrixOperations.copy(heldBrickData);
    }

    // Getters: gets the X position of the ghost piece
    public int getGhostXPosition() {
        return ghostXPosition;
    }

    // Getters: gets the Y position of the ghost piece
    public int getGhostYPosition() {
        return ghostYPosition;
    }
}
