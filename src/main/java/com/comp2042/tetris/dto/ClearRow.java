package com.comp2042.tetris.dto;

import com.comp2042.tetris.util.MatrixOperations;

/**
 * Data Transfer Object (DTO) that encapsulates the result of a row-clearing operation.
 * Contains information about how many lines were cleared, the updated game board matrix, and the score bonus awarded for the clear.
 * This class is immutable (final) to ensure data integrity when passed between components.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructor: Initializes all fields
     *
     * @param linesRemoved Number of complete rows that were cleared
     * @param newMatrix The updated game board after row removal
     * @param scoreBonus Points awarded for this clear (based on line count)
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    // Getters: gets the number of lines removed
    public int getLinesRemoved() {
        return linesRemoved;
    }

    // Getters: gets a copy of the new matrix after rows have been cleared
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    // Getters: gets the score bonus awarded for the row clear
    public int getScoreBonus() {
        return scoreBonus;
    }
}
