package com.comp2042.tetris.util;

import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    private static final String START_MENU_FXML = "startMenu.fxml";
    private static final String MAIN_MENU_TITLE = "TetrisJFX - Main Menu";
    private static final int MENU_WIDTH = 350;
    private static final int MENU_HEIGHT = 600;

    // Navigates to main menu, stopping the game and music
    public static void navigateToMainMenu(Stage currentStage, Timeline gameTimeline) {
        // Stop the game timeline if provided
        if (gameTimeline != null) {
            gameTimeline.stop();
        }

        // Stop music
        MusicManager.getInstance().stopMusic();

        try {
            // Load the start menu
            FXMLLoader fxmlLoader = new FXMLLoader(
                    SceneNavigator.class.getClassLoader().getResource(START_MENU_FXML)
            );
            Parent root = fxmlLoader.load();

            // Create and set the main menu scene
            Scene menuScene = new Scene(root, MENU_WIDTH, MENU_HEIGHT);
            currentStage.setScene(menuScene);
            currentStage.setTitle(MAIN_MENU_TITLE);

        } catch (IOException e) {
            // If navigation fails, show error
            DialogHelper.showError("Failed to load main menu");
        }
    }
}
