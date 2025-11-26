package com.comp2042.tetris.model;

public class GameOverState implements GameState{

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public String getStateName() {
        return "Game Over";
    }
}
