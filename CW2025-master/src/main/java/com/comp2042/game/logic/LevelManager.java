package com.comp2042.game.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LevelManager {
    private final IntegerProperty currentLevel = new SimpleIntegerProperty(1);
    private final IntegerProperty totalLinesCleared = new SimpleIntegerProperty(0);
    private final IntegerProperty linesToNextLevel = new SimpleIntegerProperty(4);

    // Game speeds in milliseconds (faster as level increases)
    private static final int[] LEVEL_SPEEDS = {
            500, 400, 300, 250, 200, 150, 100, 80, 60, 50
    };

    public LevelManager() {
        updateLinesToNextLevel();
    }

    public void addLinesCleared(int lines) {
        System.out.println("DEBUG: addLinesCleared called with lines = " + lines);
        if (lines > 0) {
            int oldTotal = totalLinesCleared.get();
            int newTotal = oldTotal + lines;
            totalLinesCleared.set(newTotal);
            System.out.println("DEBUG: Lines updated from " + oldTotal + " to " + newTotal);
            System.out.println("DEBUG: Property value after set: " + totalLinesCleared.get());
            checkLevelUp();
            updateLinesToNextLevel();
        } else {
            System.out.println("DEBUG: No lines to add (lines <= 0)");
        }
    }

    private void checkLevelUp() {
        int currentTotal = totalLinesCleared.get();
        int currentLvl = currentLevel.get();

        // Check if we've reached the next level
        int requiredForNextLevel = calculateRequiredLines(currentLvl + 1);

        while (currentTotal >= requiredForNextLevel && currentLvl < 10) {
            currentLvl++;
            currentLevel.set(currentLvl);
            System.out.println("DEBUG: LEVEL UP! Now Level: " + currentLevel.get() + ", Total Lines: " + currentTotal);
            requiredForNextLevel = calculateRequiredLines(currentLvl + 1);
        }
    }

    private int calculateRequiredLines(int level) {
        // Level 1: 0 lines, Level 2: 4 lines, Level 3: 8 lines, Level 4: 12 lines, etc.
        // Simple progression: 4 lines per level
        if (level <= 1) return 0;
        return (level - 1) * 4;
    }

    private void updateLinesToNextLevel() {
        int currentLvl = currentLevel.get();
        int requiredForNextLevel = calculateRequiredLines(currentLvl + 1);
        int current = totalLinesCleared.get();
        int linesNeeded = requiredForNextLevel - current;

        // If we're at max level or have cleared all lines needed
        if (currentLvl >= 10 || linesNeeded <= 0) {
            linesToNextLevel.set(0);
        } else {
            linesToNextLevel.set(linesNeeded);
        }

        System.out.println("DEBUG: Level " + currentLvl + " - Current lines: " + current + ", Need " + linesNeeded + " more lines for level " + (currentLvl + 1));
    }

    public int getCurrentSpeed() {
        int levelIndex = Math.min(currentLevel.get() - 1, LEVEL_SPEEDS.length - 1);
        int speed = LEVEL_SPEEDS[levelIndex];
        System.out.println("DEBUG: Current speed: " + speed + "ms for level " + currentLevel.get());
        return speed;
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
        currentLevel.set(1);
        totalLinesCleared.set(0);
        updateLinesToNextLevel();
        System.out.println("DEBUG: LevelManager reset");
    }
}