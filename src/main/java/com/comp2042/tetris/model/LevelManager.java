package com.comp2042.tetris.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages level progression and difficulty scaling in Tetris.
 * Tracks lines cleared, determines when to level up, and provides corresponding game speeds for each level.
 * Uses JavaFX properties to enable automatic UI updates when values change.
 * Level progression: Every 4 lines cleared advances one level (up to level 10).
 * Game speed increases (piece falls faster) as levels increase.
 */
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

    // Initializes the LevelManager and sets initial lines to next level
    public LevelManager() {
        updateLinesToNextLevel();
    }

    /**
     * Adds cleared lines to the total and checks for level advancement.
     * Automatically updates level and speed if thresholds are met.
     *
     * @param lines Number of lines cleared (1-4 for single/double/triple/tetris)
     */
    public void addLinesCleared(int lines) {
        if (lines > 0) {
            totalLinesCleared.set(totalLinesCleared.get() + lines);
            checkLevelUp();
            updateLinesToNextLevel();
        }
    }

    // Checks if the player has cleared enough lines to level up
    private void checkLevelUp() {
        int currentTotal = totalLinesCleared.get();
        int currentLvl = currentLevel.get();
        int requiredForNextLevel = calculateRequiredLines(currentLvl + 1);

        // Loop handles multiple level-ups (e.g. clearing 8 lines at level 1)
        while (currentTotal >= requiredForNextLevel && currentLvl < MAX_LEVEL) {
            currentLvl++;
            currentLevel.set(currentLvl);
            requiredForNextLevel = calculateRequiredLines(currentLvl + 1);
        }
    }

    /**
     * Calculates total lines required to reach a specific level.
     * Formula: (level - 1) * 4 lines per level
     *
     * @param level The target level
     * @return Total lines needed from start to reach that level
     */
    private int calculateRequiredLines(int level) {
        if (level <= STARTING_LEVEL) {
            return 0;
        }
        return (level - STARTING_LEVEL) * LINES_PER_LEVEL;
    }

    // Updates the lines needed to reach the next level
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

    /**
     * Gets the game speed (drop interval) for the current level.
     * Speed increases (interval decreases) with each level.
     *
     * @return Milliseconds between automatic brick drops (500ms at level 1, 50ms at level 10)
     */
    public int getCurrentSpeed() {
        int levelIndex = Math.min(currentLevel.get() - 1, LEVEL_SPEEDS.length - 1);
        return LEVEL_SPEEDS[levelIndex];
    }

    // Resets level manager to initial state
    public void reset() {
        currentLevel.set(STARTING_LEVEL);
        totalLinesCleared.set(0);
        updateLinesToNextLevel();
    }

    // Getter: current level property for UI binding
    public IntegerProperty currentLevelProperty() {
        return currentLevel;
    }

    // Getter: total lines cleared property for UI binding
    public IntegerProperty totalLinesClearedProperty() {
        return totalLinesCleared;
    }

    // Getter: lines to next level property for UI binding
    public IntegerProperty linesToNextLevelProperty() {
        return linesToNextLevel;
    }


}