package com.comp2042.tetris.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Manages the persistence of high scores for the Tetris game
// Handles loading and saving high scores to a file
public class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "tetris_highscore.dat";
    private static final String GAME_DIR = ".tetrisjfx";

    /**
     * Get the path to the high score file.
     * Creates the game directory if it doesn't exist.
     *
     * @return Path object pointing to the high score file
     */
    private static Path getHighScoreFilePath() {
        Path gameDir = Paths.get(GAME_DIR);
        try {
            // Create directory if it doesn't exist
            if (!Files.exists(gameDir)) {
                Files.createDirectories(gameDir);
            }
        } catch (IOException e) {
            // Directory creation failed; but continue with existing path
        }
        return gameDir.resolve(HIGH_SCORE_FILE);
    }

    /**
     * Load high score from file.
     * Returns 0 if file doesn't exist or cannot be read.
     *
     * @return the saved high score, or 0 if unavailable
     */
    public static int loadHighScore() {
        Path filePath = getHighScoreFilePath();

        if (!Files.exists(filePath)) {
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            // File read error; return default score
        } catch (NumberFormatException e) {
            // Invalid number format; return default score
        }

        return 0;
    }

    /**
     * Save high score to file.
     *
     * @param score the score to save
     * @return true if save was successful, false otherwise
     */
    public static boolean saveHighScore(int score) {
        Path filePath = getHighScoreFilePath();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(String.valueOf(score));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Check and update high score if current score is higher.
     *
     * @param currentScore the score achieved in the current game
     * @param currentHighScore the existing high score to compare against
     * @return true if a new high score was set and saved, false otherwise
     */
    public static boolean checkAndSaveHighScore(int currentScore, int currentHighScore) {
        if (currentScore > currentHighScore) {
            saveHighScore(currentScore);
            return true;
        }
        return false;
    }
}