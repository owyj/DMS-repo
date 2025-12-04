package com.comp2042.tetris.model;

public class PlayingState implements GameState{

    // Pieces can move freely when the game is playing
    @Override
    public boolean canMove() {
        return true;
    }

    // Game can be paused when the game is playing
    @Override
    public boolean canPause() {
        return true;
    }

    // Game cannot be resumed when it is already playing (Debugging purpose)
    @Override
    public String getStateName() {
        return "Playing";
    }
}
