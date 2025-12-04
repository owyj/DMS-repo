package com.comp2042.tetris.model;

public interface GameState {
    boolean canMove();
    boolean canPause();
    String getStateName();
}
