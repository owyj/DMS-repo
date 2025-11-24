package com.comp2042.tetris.model.piece.types;

import com.comp2042.tetris.model.piece.Brick;
import com.comp2042.tetris.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

public final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
