package com.comp2042.tetris.model.piece.types;

import com.comp2042.tetris.model.piece.Brick;
import com.comp2042.tetris.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

public final class NullBrick implements Brick {
    private static final NullBrick INSTANCE = new NullBrick();
    private final List<int[][]> brickMatrix = new ArrayList<>();

    private NullBrick() {
        // Add one empty shape (consistent with other bricks)
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    public static NullBrick getInstance() {
        return INSTANCE;
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}