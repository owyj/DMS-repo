package com.comp2042.tetris.model.piece.types;

import com.comp2042.tetris.model.piece.Brick;
import com.comp2042.tetris.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a null/empty brick using the Null Object pattern.
 * Used as a placeholder when no brick is held in the hold mechanic, avoiding the need for null checks throughout the codebase.
 * Implements the Singleton pattern to ensure only one instance exists, as all null bricks are functionally identical.
 * The brick contains an empty 4x4 matrix (all zeros) to maintain consistency with other brick implementations.
 */
public final class NullBrick implements Brick {
    // Singleton instance
    private static final NullBrick INSTANCE = new NullBrick();

    // Empty brick matrix
    private final List<int[][]> brickMatrix = new ArrayList<>();

    // Private constructor to enforce singleton pattern
    private NullBrick() {
        // Add one empty shape (consistent with other bricks)
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    // Public method to access the singleton instance
    public static NullBrick getInstance() {
        return INSTANCE;
    }

    // Returns a deep copy of the brick's shape matrix
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}