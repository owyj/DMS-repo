package com.comp2042.tetris.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Level Manager Tests")
class LevelManagerTest {

    private LevelManager levelManager;

    @BeforeEach
    void setUp() {
        levelManager = new LevelManager();
    }

    @Test
    @DisplayName("Should start at level 1")
    void testInitialLevel() {
        assertEquals(1, levelManager.currentLevelProperty().get());
    }

    @Test
    @DisplayName("Should start with 0 total lines cleared")
    void testInitialLinesCleared() {
        assertEquals(0, levelManager.totalLinesClearedProperty().get());
    }

    @Test
    @DisplayName("Should start with 4 lines to next level")
    void testInitialLinesToNextLevel() {
        assertEquals(4, levelManager.linesToNextLevelProperty().get());
    }

    @Test
    @DisplayName("Should increment total lines cleared")
    void testAddLinesCleared() {
        levelManager.addLinesCleared(2);
        assertEquals(2, levelManager.totalLinesClearedProperty().get());

        levelManager.addLinesCleared(1);
        assertEquals(3, levelManager.totalLinesClearedProperty().get());
    }

    @Test
    @DisplayName("Should level up after clearing 4 lines")
    void testLevelUpAfterFourLines() {
        levelManager.addLinesCleared(4);

        assertEquals(2, levelManager.currentLevelProperty().get());
        assertEquals(4, levelManager.totalLinesClearedProperty().get());
    }

    @Test
    @DisplayName("Should update lines to next level correctly")
    void testLinesToNextLevelUpdate() {
        levelManager.addLinesCleared(2);
        assertEquals(2, levelManager.linesToNextLevelProperty().get());

        levelManager.addLinesCleared(1);
        assertEquals(1, levelManager.linesToNextLevelProperty().get());
    }

    @Test
    @DisplayName("Should level up to level 3 after 8 lines")
    void testLevelUpToLevel3() {
        levelManager.addLinesCleared(8);

        assertEquals(3, levelManager.currentLevelProperty().get());
        assertEquals(8, levelManager.totalLinesClearedProperty().get());
    }

    @Test
    @DisplayName("Should handle multiple level ups in single call")
    void testMultipleLevelUpsAtOnce() {
        levelManager.addLinesCleared(12);

        assertEquals(4, levelManager.currentLevelProperty().get());
        assertEquals(12, levelManager.totalLinesClearedProperty().get());
    }

    @Test
    @DisplayName("Should not exceed max level 10")
    void testMaxLevel() {
        levelManager.addLinesCleared(100);

        assertEquals(10, levelManager.currentLevelProperty().get());
    }

    @Test
    @DisplayName("Should have 0 lines to next level at max level")
    void testLinesToNextLevelAtMaxLevel() {
        levelManager.addLinesCleared(100);

        assertEquals(0, levelManager.linesToNextLevelProperty().get());
    }

    @Test
    @DisplayName("Should decrease speed as level increases")
    void testSpeedDecreasesWithLevel() {
        int speedLevel1 = levelManager.getCurrentSpeed();

        levelManager.addLinesCleared(4);
        int speedLevel2 = levelManager.getCurrentSpeed();

        assertTrue(speedLevel2 < speedLevel1, "Speed should decrease (time in ms) as level increases");
    }

    @Test
    @DisplayName("Should return correct speed for level 1")
    void testSpeedLevel1() {
        assertEquals(500, levelManager.getCurrentSpeed());
    }

    @Test
    @DisplayName("Should return correct speed for level 5")
    void testSpeedLevel5() {
        levelManager.addLinesCleared(16); // Level 5 requires 16 lines (4*4)
        assertEquals(200, levelManager.getCurrentSpeed());
    }

    @Test
    @DisplayName("Should return correct speed for max level 10")
    void testSpeedMaxLevel() {
        levelManager.addLinesCleared(100);
        assertEquals(50, levelManager.getCurrentSpeed());
    }

    @Test
    @DisplayName("Should reset to initial state")
    void testReset() {
        levelManager.addLinesCleared(10);

        levelManager.reset();

        assertEquals(1, levelManager.currentLevelProperty().get());
        assertEquals(0, levelManager.totalLinesClearedProperty().get());
        assertEquals(4, levelManager.linesToNextLevelProperty().get());
    }

    @Test
    @DisplayName("Should not add lines when zero lines provided")
    void testAddZeroLines() {
        levelManager.addLinesCleared(0);

        assertEquals(0, levelManager.totalLinesClearedProperty().get());
        assertEquals(1, levelManager.currentLevelProperty().get());
    }

    @Test
    @DisplayName("Should handle gradual progression through levels")
    void testGradualProgression() {
        // Level 1 -> 2
        levelManager.addLinesCleared(1);
        assertEquals(1, levelManager.currentLevelProperty().get());

        levelManager.addLinesCleared(3);
        assertEquals(2, levelManager.currentLevelProperty().get());
        assertEquals(4, levelManager.linesToNextLevelProperty().get());

        // Level 2 -> 3
        levelManager.addLinesCleared(4);
        assertEquals(3, levelManager.currentLevelProperty().get());
    }

    @Test
    @DisplayName("Properties should be observable")
    void testPropertiesAreObservable() {
        final int[] levelObserved = {1};
        final int[] linesObserved = {0};

        levelManager.currentLevelProperty().addListener((obs, old, newVal) -> {
            levelObserved[0] = newVal.intValue();
        });

        levelManager.totalLinesClearedProperty().addListener((obs, old, newVal) -> {
            linesObserved[0] = newVal.intValue();
        });

        levelManager.addLinesCleared(4);

        assertEquals(2, levelObserved[0]);
        assertEquals(4, linesObserved[0]);
    }
}