package com.comp2042.tetris.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LevelManager {
    private final IntegerProperty currentLevel = new SimpleIntegerProperty(STARTING_LEVEL);
    private final IntegerProperty totalLinesCleared = new SimpleIntegerProperty(0);
    private final IntegerProperty linesToNextLevel = new SimpleIntegerProperty(LINES_PER_LEVEL);

    private static final int MAX_LEVEL = 10;
    private static final int LINES_PER_LEVEL = 4;
    private static final int STARTING_LEVEL = 1;

    // Game speeds in milliseconds (faster as level increases)
    private static final int[] LEVEL_SPEEDS = {
            500, 400, 300, 250, 200, 150, 100, 80, 60, 50
    };

    public LevelManager() {
        updateLinesToNextLevel();
    }

    public void addLinesCleared(int lines) {
        if (lines > 0) {
            totalLinesCleared.set(totalLinesCleared.get() + lines);
            checkLevelUp();
            updateLinesToNextLevel();
        }
    }

    private void checkLevelUp() {
        int currentTotal = totalLinesCleared.get();
        int currentLvl = currentLevel.get();
        int requiredForNextLevel = calculateRequiredLines(currentLvl + 1);

        while (currentTotal >= requiredForNextLevel && currentLvl < MAX_LEVEL) {
            currentLvl++;
            currentLevel.set(currentLvl);
            requiredForNextLevel = calculateRequiredLines(currentLvl + 1);
        }
    }

    private int calculateRequiredLines(int level) {
        if (level <= STARTING_LEVEL) {
            return 0;
        }
        return (level - STARTING_LEVEL) * LINES_PER_LEVEL;
    }

    private void updateLinesToNextLevel() {
        int currentLvl = currentLevel.get();
        int requiredForNextLevel = calculateRequiredLines(currentLvl + 1);
        int current = totalLinesCleared.get();
        int linesNeeded = requiredForNextLevel - current;

        if (currentLvl >= MAX_LEVEL || linesNeeded <= 0) {
            linesToNextLevel.set(0);
        } else {
            linesToNextLevel.set(linesNeeded);
        }
    }

    public int getCurrentSpeed() {
        int levelIndex = Math.min(currentLevel.get() - 1, LEVEL_SPEEDS.length - 1);
        return LEVEL_SPEEDS[levelIndex];
    }

    // Getters
    public IntegerProperty currentLevelProperty() {
        return currentLevel;
    }

    public IntegerProperty totalLinesClearedProperty() {
        return totalLinesCleared;
    }

    public IntegerProperty linesToNextLevelProperty() {
        return linesToNextLevel;
    }

    public int getCurrentLevel() {
        return currentLevel.get();
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared.get();
    }

    public int getLinesToNextLevel() {
        return linesToNextLevel.get();
    }

    public void reset() {
        currentLevel.set(STARTING_LEVEL);
        totalLinesCleared.set(0);
        updateLinesToNextLevel();
    }
}