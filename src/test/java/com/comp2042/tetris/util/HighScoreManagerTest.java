package com.comp2042.tetris.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("High Score Manager Tests")
class HighScoreManagerTest {

    private Path realGameDir;
    private Path realHighScoreFile;
    private Path backupFile;

    @BeforeEach
    void setUp() throws IOException {
        // Back up existing high score file if it exists
        realGameDir = Paths.get(".tetrisjfx");
        realHighScoreFile = realGameDir.resolve("tetris_highscore.dat");
        backupFile = realGameDir.resolve("tetris_highscore.dat.backup");

        // Backup existing file
        if (Files.exists(realHighScoreFile)) {
            Files.copy(realHighScoreFile, backupFile,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            Files.delete(realHighScoreFile);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restore backup
        if (Files.exists(backupFile)) {
            Files.move(backupFile, realHighScoreFile,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Test
    @DisplayName("Should return 0 when no high score file exists")
    void testLoadHighScoreNoFile() {
        // Ensure file doesn't exist
        if (Files.exists(realHighScoreFile)) {
            try {
                Files.delete(realHighScoreFile);
            } catch (IOException e) {
                fail("Could not delete test file");
            }
        }

        int highScore = HighScoreManager.loadHighScore();
        assertEquals(0, highScore);
    }

    @Test
    @DisplayName("Should save high score successfully")
    void testSaveHighScore() {
        boolean saved = HighScoreManager.saveHighScore(5000);
        assertTrue(saved);
    }

    @Test
    @DisplayName("Should load saved high score")
    void testLoadSavedHighScore() {
        int scoreToSave = 7500;
        HighScoreManager.saveHighScore(scoreToSave);

        int loadedScore = HighScoreManager.loadHighScore();
        assertEquals(scoreToSave, loadedScore);
    }

    @Test
    @DisplayName("Should update high score when current score is higher")
    void testCheckAndSaveHigherScore() {
        HighScoreManager.saveHighScore(1000);

        boolean updated = HighScoreManager.checkAndSaveHighScore(2000, 1000);
        assertTrue(updated);

        assertEquals(2000, HighScoreManager.loadHighScore());
    }

    @Test
    @DisplayName("Should not update high score when current score is lower")
    void testCheckAndSaveNoUpdateForLowerScore() {
        HighScoreManager.saveHighScore(5000);

        boolean updated = HighScoreManager.checkAndSaveHighScore(3000, 5000);
        assertFalse(updated);

        assertEquals(5000, HighScoreManager.loadHighScore());
    }

    @Test
    @DisplayName("Should handle zero as high score")
    void testSaveZeroScore() {
        HighScoreManager.saveHighScore(0);
        assertEquals(0, HighScoreManager.loadHighScore());
    }

    @Test
    @DisplayName("Should handle very large scores")
    void testSaveVeryLargeScore() {
        int largeScore = Integer.MAX_VALUE - 1000;
        HighScoreManager.saveHighScore(largeScore);

        assertEquals(largeScore, HighScoreManager.loadHighScore());
    }

    @Test
    @DisplayName("Should overwrite existing high score")
    void testOverwriteHighScore() {
        HighScoreManager.saveHighScore(1000);
        HighScoreManager.saveHighScore(2000);

        assertEquals(2000, HighScoreManager.loadHighScore());
    }

    @Test
    @DisplayName("Should persist high score across multiple loads")
    void testPersistenceAcrossMultipleLoads() {
        HighScoreManager.saveHighScore(3500);

        int load1 = HighScoreManager.loadHighScore();
        int load2 = HighScoreManager.loadHighScore();
        int load3 = HighScoreManager.loadHighScore();

        assertEquals(3500, load1);
        assertEquals(3500, load2);
        assertEquals(3500, load3);
    }

    @Test
    @DisplayName("Should update when current equals high score")
    void testCheckAndSaveEqualScore() {
        HighScoreManager.saveHighScore(1000);

        boolean updated = HighScoreManager.checkAndSaveHighScore(1000, 1000);
        assertFalse(updated, "Should not update when scores are equal");
    }

    @Test
    @DisplayName("Should handle multiple save operations")
    void testMultipleSaveOperations() {
        assertTrue(HighScoreManager.saveHighScore(100));
        assertTrue(HighScoreManager.saveHighScore(200));
        assertTrue(HighScoreManager.saveHighScore(300));

        assertEquals(300, HighScoreManager.loadHighScore());
    }

    @Test
    @DisplayName("Should return false when not updating high score")
    void testCheckAndSaveReturnsFalseWhenNotUpdating() {
        HighScoreManager.saveHighScore(9999);

        boolean result = HighScoreManager.checkAndSaveHighScore(1000, 9999);
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when updating high score")
    void testCheckAndSaveReturnsTrueWhenUpdating() {
        boolean result = HighScoreManager.checkAndSaveHighScore(5000, 0);
        assertTrue(result);
    }
}