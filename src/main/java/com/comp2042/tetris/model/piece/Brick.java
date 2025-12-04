package com.comp2042.tetris.model.piece;

import java.util.List;

public interface Brick {

    // Return all rotation states of the brick as a list of matrices
    List<int[][]> getShapeMatrix();
}
