package com.comp2042.tetris.model;

public class GameOverState implements GameState{

    // Game over state does not allow any actions
    @Override
    public boolean canMove() {
        return false;
    }

    // Game over state does not allow rotation
    @Override
    public boolean canPause() {
        return false;
    }

    // Game over state does not allow pausing
    @Override
    public String getStateName() {
        return "Game Over";
    }
}
