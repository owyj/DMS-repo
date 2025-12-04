package com.comp2042.tetris.model.piece;

public interface BrickGenerator {

    // Return the current brick
    Brick getBrick();

    // Return the next brick
    Brick getNextBrick();
}
