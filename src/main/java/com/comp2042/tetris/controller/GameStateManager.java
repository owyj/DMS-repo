package com.comp2042.tetris.controller;

import com.comp2042.tetris.util.MusicManager;
import com.comp2042.tetris.view.component.GameOverPanel;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import com.comp2042.tetris.model.GameState;
import com.comp2042.tetris.model.GameOverState;
import com.comp2042.tetris.model.PausedState;
import com.comp2042.tetris.model.PlayingState;

/**
 * Manages the game state transitions and associated UI/audio behavior.
 * Implements the State pattern to handle Playing, Paused, and GameOver states.
 * Coordinates the game timeline, music playback, and game over panel display.
 */
public class GameStateManager {
    private final Timeline timeline;
    private final GameOverPanel gameOverPanel;
    private final Pane rootPane;
    private final Runnable onNewGameCallback;
    private GameState currentState;

    // Constructor: Initializes the game state manager with necessary dependencies.
    public GameStateManager(Timeline timeline, GameOverPanel gameOverPanel,
                            Pane rootPane, Runnable onNewGameCallback) {
        this.timeline = timeline;
        this.gameOverPanel = gameOverPanel;
        this.rootPane = rootPane;
        this.onNewGameCallback = onNewGameCallback;

        this.currentState = new PlayingState();
    }

    // Pause game
    public void pause() {
        if (currentState.canPause()) {
            timeline.pause();
            MusicManager.getInstance().pauseMusic();
            currentState = new PausedState();
        }
    }

    // Resume game
    public void resume() {
        if (currentState instanceof PausedState) {
            timeline.play();
            MusicManager.getInstance().playMusic();
            currentState = new PlayingState();
        }
    }

    // Toggle pause
    public void togglePause() {
        if (currentState instanceof PausedState) {
            resume();
        } else if (currentState instanceof PlayingState) {
            pause();
        }
    }

    // Game over
    public void triggerGameOver() {
        timeline.stop();
        MusicManager.getInstance().pauseMusic();

        currentState = new GameOverState();
        gameOverPanel.setVisible(true);

        // Position the game over panel
        if (gameOverPanel.getParent() != rootPane) {
            // Remove from previous parent if it exists
            if (gameOverPanel.getParent() != null) {
                ((Pane) gameOverPanel.getParent()).getChildren().remove(gameOverPanel);
            }
            rootPane.getChildren().add(gameOverPanel);
        }

        // Panel position and size
        gameOverPanel.setLayoutX(0);
        gameOverPanel.setLayoutY(0);
        gameOverPanel.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        gameOverPanel.toFront();
    }

    // New game
    public void startNewGame() {
        timeline.stop();
        gameOverPanel.setVisible(false);
        MusicManager.getInstance().resetMusic();

        onNewGameCallback.run();

        timeline.play();
        currentState = new PlayingState();
    }

    // Update game speed
    public void updateSpeed(int speedInMillis, Runnable moveDownAction) {
        timeline.stop();
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.millis(speedInMillis),
                        e -> moveDownAction.run()
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Resume timeline if in playing state
        if (currentState.canMove()) {
            timeline.play();
        }
    }

    // Checks if current state allows brick movement
    public boolean canMove() {
        return currentState.canMove();
    }

    // Checks if current state allows pausing
    public boolean canPause() {
        return currentState.canPause();
    }

    // Get current state name
    public String getCurrentStateName() {
        return currentState.getStateName();
    }

}
