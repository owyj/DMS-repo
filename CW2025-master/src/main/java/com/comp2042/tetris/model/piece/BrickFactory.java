package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.model.piece.types.*;

public class BrickFactory {

    public enum BrickType {
        I, J, L, O, S, T, Z
    }

    // create a brick of specific type
    public static Brick createBrick(BrickType type) {
        return switch (type) {
            case I -> new IBrick();
            case J -> new JBrick();
            case L -> new LBrick();
            case O -> new OBrick();
            case S -> new SBrick();
            case T -> new TBrick();
            case Z -> new ZBrick();
        };
    }

    // create all brick types as a list
    public static java.util.List createAllBricks() {
        return java.util.Arrays.stream(BrickType.values())
                .map(BrickFactory::createBrick)
                .collect(java.util.stream.Collectors.toList());
    }
}
