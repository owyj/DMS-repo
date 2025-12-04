package com.comp2042.tetris.model;

public class PausedState implements GameState{

    // In paused state, the game does not respond to move commands but can be unpaused
    @Override
    public boolean canMove() {
        return false;
    }

    // In paused state, the game can be unpaused
    @Override
    public boolean canPause() {
        return true; // Can unpause
    }

    // In paused state, the game cannot be over
    @Override
    public String getStateName() {
        return "Paused";
    }
}
