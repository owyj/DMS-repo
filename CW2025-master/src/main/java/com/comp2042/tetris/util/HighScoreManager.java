package com.comp2042.tetris.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HighScoreManager {

    private static final String HIGH_SCORE_FILE = "tetris_highscore.dat";
    private static final String GAME_DIR = ".tetrisjfx";

    //Get the path to the high score file
    private static Path getHighScoreFilePath() {
        Path gameDir = Paths.get(GAME_DIR);
        try {
            //Create directory if it doesn't exist
            if (!Files.exists(gameDir)) {
                Files.createDirectories(gameDir);
            }
        } catch (IOException e) {
            System.err.println("Error creating game directory: " + e.getMessage());
        }
        return gameDir.resolve(HIGH_SCORE_FILE);
    }

    //Load high score from file
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
            System.err.println("Error reading high score: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid high score format: " + e.getMessage());
        }

        return 0;
    }

    //Save high score to file
    public static boolean saveHighScore(int score) {
        Path filePath = getHighScoreFilePath();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(String.valueOf(score));
            return true;
        } catch (IOException e) {
            System.err.println("Error saving high score: " + e.getMessage());
            return false;
        }
    }

    //Check and update high score if current score is higher
    public static boolean checkAndSaveHighScore(int currentScore, int currentHighScore) {
        if (currentScore > currentHighScore) {
            saveHighScore(currentScore);
            return true;
        }
        return false;
    }
}