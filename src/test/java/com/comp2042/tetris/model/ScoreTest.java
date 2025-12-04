package com.comp2042.tetris.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Score Tests")
class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    @DisplayName("Should initialize with zero score")
    void testInitialScore() {
        assertEquals(0, score.getScore());
    }

    @Test
    @DisplayName("Should add points correctly")
    void testAddPoints() {
        score.add(100);
        assertEquals(100, score.getScore());

        score.add(50);
        assertEquals(150, score.getScore());
    }

    @Test
    @DisplayName("Should throw exception when adding negative points")
    void testAddNegativePoints() {
        assertThrows(IllegalArgumentException.class, () -> score.add(-10));
    }

    @Test
    @DisplayName("Should reset score to zero")
    void testResetScore() {
        score.add(500);
        score.reset();
        assertEquals(0, score.getScore());
    }

    @Test
    @DisplayName("Should update high score when current score exceeds it")
    void testHighScoreUpdate() {
        int initialHighScore = score.getHighScore();

        // Add enough points to definitely exceed initial high score
        score.add(50000);

        assertTrue(score.getScore() >= initialHighScore,
                "Current score should be at least equal to initial high score");
        assertEquals(score.getScore(), score.getHighScore(),
                "High score should equal current score when current exceeds previous high");
    }

    @Test
    @DisplayName("Should not decrease high score")
    void testHighScoreDoesNotDecrease() {
        score.add(1000);
        int highScore = score.getHighScore();

        score.reset();
        score.add(500);

        assertEquals(highScore, score.getHighScore());
    }

    @Test
    @DisplayName("Score property should be observable")
    void testScorePropertyObservable() {
        final int[] observedValue = {0};

        score.scoreProperty().addListener((obs, oldVal, newVal) -> {
            observedValue[0] = newVal.intValue();
        });

        score.add(250);
        assertEquals(250, observedValue[0]);
    }

    @Test
    @DisplayName("High score property should be observable")
    void testHighScorePropertyObservable() {
        final int[] observedValue = {score.getHighScore()};

        score.highScoreProperty().addListener((obs, oldVal, newVal) -> {
            observedValue[0] = newVal.intValue();
        });

        score.add(5000);
        assertEquals(score.getHighScore(), observedValue[0]);
    }

    @Test
    @DisplayName("Should add multiple scores cumulatively")
    void testMultipleAdditions() {
        score.add(100);
        score.add(200);
        score.add(300);
        score.add(400);

        assertEquals(1000, score.getScore());
    }

    @Test
    @DisplayName("Should handle zero points addition")
    void testAddZeroPoints() {
        score.add(0);
        assertEquals(0, score.getScore());
    }
}