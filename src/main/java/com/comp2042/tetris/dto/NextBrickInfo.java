package com.comp2042.tetris.dto;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Data Transfer Object (DTO) that encapsulates information about the next brick to appear in the game.
 * Contains the shape matrix of the next brick and its initial horizontal position.
 * This class is immutable (final) to ensure data integrity when passed between components.
 */
public final class NextBrickInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructor: Initializes all fields
     *
     * @param shape The matrix representation of the next brick's shape
     * @param position The initial horizontal position of the next brick
     */
    public NextBrickInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    // Getters: gets a copy of the next brick's shape matrix
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    // Getters: gets the initial horizontal position of the next brick
    public int getPosition() {
        return position;
    }
}
