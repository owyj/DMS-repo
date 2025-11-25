package com.comp2042.tetris.model;

import com.comp2042.tetris.util.HighScoreManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);

    public Score() {
        //Load high score from file when Score object is created
        int loadedHighScore = HighScoreManager.loadHighScore();
        highScore.setValue(loadedHighScore);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public int getScore() {
        return score.getValue();
    }

    public int getHighScore() {
        return highScore.getValue();
    }

    public void add(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative: " + points);
        }
        score.setValue(score.getValue() + points);
        // Update high score if current score exceeds it
        if (score.getValue() > highScore.getValue()) {
            highScore.setValue(score.getValue());
        }
    }

    public void reset() {
        score.setValue(0);
    }

    // Save high score to file if current score is higher than previous high score
    public boolean saveIfHighScore() {
        return HighScoreManager.checkAndSaveHighScore(score.getValue(), HighScoreManager.loadHighScore());
    }

    // Check if current score is a new high score
    public boolean isNewHighScore() {
        int previousHighScore = HighScoreManager.loadHighScore();
        return score.getValue() > previousHighScore;
    }

}
