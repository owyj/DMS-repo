package com.comp2042.tetris.model;

public class PausedState implements GameState{

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canPause() {
        return true; // Can unpause
    }

    @Override
    public String getStateName() {
        return "Paused";
    }
}
