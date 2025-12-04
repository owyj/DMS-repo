package com.comp2042.tetris.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game State Tests")
class GameStateTest {

    @Test
    @DisplayName("Playing state should allow movement")
    void testPlayingStateCanMove() {
        GameState state = new PlayingState();
        assertTrue(state.canMove());
    }

    @Test
    @DisplayName("Playing state should allow pause")
    void testPlayingStateCanPause() {
        GameState state = new PlayingState();
        assertTrue(state.canPause());
    }

    @Test
    @DisplayName("Playing state should return correct name")
    void testPlayingStateName() {
        GameState state = new PlayingState();
        assertEquals("Playing", state.getStateName());
    }

    @Test
    @DisplayName("Paused state should not allow movement")
    void testPausedStateCannotMove() {
        GameState state = new PausedState();
        assertFalse(state.canMove());
    }

    @Test
    @DisplayName("Paused state should allow unpause")
    void testPausedStateCanPause() {
        GameState state = new PausedState();
        assertTrue(state.canPause()); // Can unpause
    }

    @Test
    @DisplayName("Paused state should return correct name")
    void testPausedStateName() {
        GameState state = new PausedState();
        assertEquals("Paused", state.getStateName());
    }

    @Test
    @DisplayName("Game over state should not allow movement")
    void testGameOverStateCannotMove() {
        GameState state = new GameOverState();
        assertFalse(state.canMove());
    }

    @Test
    @DisplayName("Game over state should not allow pause")
    void testGameOverStateCannotPause() {
        GameState state = new GameOverState();
        assertFalse(state.canPause());
    }

    @Test
    @DisplayName("Game over state should return correct name")
    void testGameOverStateName() {
        GameState state = new GameOverState();
        assertEquals("Game Over", state.getStateName());
    }

    @Test
    @DisplayName("States should have different behaviors")
    void testStateDifferences() {
        GameState playing = new PlayingState();
        GameState paused = new PausedState();
        GameState gameOver = new GameOverState();

        // Playing can move and pause
        assertTrue(playing.canMove() && playing.canPause());

        // Paused cannot move but can unpause
        assertFalse(paused.canMove());
        assertTrue(paused.canPause());

        // Game over cannot do either
        assertFalse(gameOver.canMove() || gameOver.canPause());
    }
}