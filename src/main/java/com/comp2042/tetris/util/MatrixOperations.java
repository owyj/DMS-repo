package com.comp2042.tetris.util;

import com.comp2042.tetris.dto.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

// Utility class for matrix operations
// Provide methods for collision detection, matrix manipulation, and row clearing
public class MatrixOperations {

    /**
     * Check if a brick intersects with the game matrix or is out of bounds.
     *
     * @param matrix the game board matrix
     * @param brick the tetromino brick to check
     * @param x the x-coordinate (column) position to check
     * @param y the y-coordinate (row) position to check
     * @return true if there is an intersection or out of bounds, false otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                if (brick[i][j] != 0) {
                    int targetX = x + j;
                    int targetY = y + i;
                    if (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if coordinates are out of bounds for the given matrix.
     *
     * @param matrix the matrix to check bounds against
     * @param targetX the x-coordinate to check
     * @param targetY the y-coordinate to check
     * @return true if coordinates are out of bounds, false otherwise
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        // More comprehensive bounds checking
        return targetX < 0 ||
                targetY < 0 ||
                targetY >= matrix.length ||
                targetX >= matrix[targetY].length;
    }

    /**
     * Create a deep copy of a 2D integer array.
     *
     * @param original the original matrix to copy
     * @return a new matrix with copied values
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merge a brick into the filled fields matrix at the specified position.
     * Creates a copy of the matrix with the brick's cells merged in.
     *
     * @param filledFields the current game board state
     * @param brick the tetromino brick to merge
     * @param x the x-coordinate (column) position for merging
     * @param y the y-coordinate (row) position for merging
     * @return a new matrix with the brick merged in
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                if (brick[i][j] != 0) {  // Consistent with intersect method
                    int targetX = x + j;
                    int targetY = y + i;
                    if (!checkOutOfBound(copy, targetX, targetY)) {
                        copy[targetY][targetX] = brick[i][j];
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Check for completed rows and remove them from the matrix.
     * Calculates score bonus based on the number of lines cleared.
     *
     * @param matrix the game board matrix to check
     * @return ClearRow object containing the number of cleared rows, updated matrix, and score bonus
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        // Identify complete rows and separate them from incomplete ones
        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }

        // Rebuild the matrix with remaining rows at the bottom
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }

        //new scoring logic
        int linesCleared = clearedRows.size();
        int scoreBonus = 0;

        switch (linesCleared) {
            case 1 -> scoreBonus = 100; //single
            case 2 -> scoreBonus = 300; //double
            case 3 -> scoreBonus = 500; //triple
            case 4 -> scoreBonus = 800; //tetris
            default -> scoreBonus = 0; //no lines cleared
        }
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    /**
     * Create a deep copy of a list of 2D integer arrays.
     *
     * @param list the list of matrices to copy
     * @return a new list containing deep copies of all matrices
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
