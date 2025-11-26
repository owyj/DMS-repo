package com.comp2042.tetris.model;

public class PlayingState implements GameState{

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public String getStateName() {
        return "Playing";
    }
}
