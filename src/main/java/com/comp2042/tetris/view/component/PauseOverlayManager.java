package com.comp2042.tetris.view.component;

import com.comp2042.tetris.util.DialogHelper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


// Manages the pause overlay UI component
// Displays pause menu with options to resume, view instructions, or return to main menu
public class PauseOverlayManager {
    private static final int OVERLAY_SPACING = 20;

    private VBox pauseOverlay;
    private final Pane rootPane;
    private final Runnable onResume;
    private final Runnable onMainMenu;

    // Constructor: Pause overlay manager
    public PauseOverlayManager(Pane rootPane, Runnable onResume, Runnable onMainMenu) {
        this.rootPane = rootPane;
        this.onResume = onResume;
        this.onMainMenu = onMainMenu;
    }

    // Show pause overlay
    public void show() {
        if (pauseOverlay == null) {
            createPauseOverlay();
        } else {
            updateSize();
        }

        if (!rootPane.getChildren().contains(pauseOverlay)) {
            rootPane.getChildren().add(pauseOverlay);
        }
        pauseOverlay.toFront();
    }

    // Hide pause overlay
    public void hide() {
        if (pauseOverlay != null) {
            rootPane.getChildren().remove(pauseOverlay);
        }
    }

    // Create pause overlay UI
    private void createPauseOverlay() {
        pauseOverlay = new VBox(OVERLAY_SPACING);
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.getStyleClass().add("pause-overlay");

        updateSize();
        pauseOverlay.setLayoutX(0);
        pauseOverlay.setLayoutY(0);

        Label pauseLabel = new Label("PAUSED");
        pauseLabel.getStyleClass().add("pause-title");

        Label resumeLabel = new Label("Press 'P' to resume");
        resumeLabel.getStyleClass().add("pause-subtitle");

        Button resumeButton = createButton("RESUME", e -> onResume.run());
        Button instructionsButton = createButton("INSTRUCTIONS", e -> DialogHelper.showInstructions());
        Button exitButton = createButton("MAIN MENU", e -> onMainMenu.run());

        pauseOverlay.getChildren().addAll(
                pauseLabel, resumeLabel, resumeButton, instructionsButton, exitButton
        );
    }

    // Create styled button
    private Button createButton(String text, javafx.event.EventHandler action) {
        Button button = new Button(text);
        button.getStyleClass().add("pause-button");
        button.setOnAction(action);
        return button;
    }

    // Update overlay size to match root pane
    private void updateSize() {
        if (pauseOverlay != null) {
            pauseOverlay.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        }
    }
}
