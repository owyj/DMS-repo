package com.comp2042.tetris.view;

import com.comp2042.tetris.util.MusicManager;
import com.comp2042.tetris.view.component.GameOverPanel;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;

public class GameStateManager {
    private final Timeline timeline;
    private final GameOverPanel gameOverPanel;
    private final Pane rootPane;
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private final Runnable onNewGameCallback;

    public GameStateManager(Timeline timeline, GameOverPanel gameOverPanel,
                            Pane rootPane, Runnable onNewGameCallback) {
        this.timeline = timeline;
        this.gameOverPanel = gameOverPanel;
        this.rootPane = rootPane;
        this.onNewGameCallback = onNewGameCallback;
    }

    // pause game
    public void pause() {
        if (!isGameOver.get()) {
            timeline.pause();
            MusicManager.getInstance().pauseMusic();
            isPaused.set(true);
        }
    }

    // resume game
    public void resume() {
        if (!isGameOver.get()) {
            timeline.play();
            MusicManager.getInstance().playMusic();
            isPaused.set(false);
        }
    }

    // toggle pause
    public void togglePause() {
        if (isPaused.get()) {
            resume();
        } else {
            pause();
        }
    }

    // game over
    public void triggerGameOver() {
        timeline.stop();
        MusicManager.getInstance().pauseMusic();

        isGameOver.set(true);
        gameOverPanel.setVisible(true);

        // Position the game over panel
        if (gameOverPanel.getParent() != rootPane) {
            if (gameOverPanel.getParent() != null) {
                ((Pane) gameOverPanel.getParent()).getChildren().remove(gameOverPanel);
            }
            rootPane.getChildren().add(gameOverPanel);
        }

        gameOverPanel.setLayoutX(0);
        gameOverPanel.setLayoutY(0);
        gameOverPanel.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        gameOverPanel.toFront();
    }

    // new game
    public void startNewGame() {
        timeline.stop();
        gameOverPanel.setVisible(false);
        MusicManager.getInstance().resetMusic();

        onNewGameCallback.run();

        timeline.play();
        isPaused.set(false);
        isGameOver.set(false);
    }

    // update game speed
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

        if (!isPaused.get() && !isGameOver.get()) {
            timeline.play();
        }
    }

    // getters
    public boolean isPaused() {
        return isPaused.get();
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public BooleanProperty isPausedProperty() {
        return isPaused;
    }

    public BooleanProperty isGameOverProperty() {
        return isGameOver;
    }

}
