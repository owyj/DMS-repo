package com.comp2042.tetris.util;

import javafx.scene.control.Alert;

public class DialogHelper {

    private static final String INSTRUCTIONS_TITLE = "Instructions";
    private static final String INSTRUCTIONS_HEADER = "How to Play Tetris";
    private static final String INSTRUCTIONS_CONTENT = """
            Controls:
            
            ← / A - Move Left
            → / D - Move Right
            ↑ / W - Rotate
            ↓ / S - Move Down
            
            Space - Instant Drop
            C / SHIFT - Hold Piece
            P - Pause Game
            N - New Game
            
            Scoring:
            
            Single Line: +100 points
            Double Line: +300 points
            Triple Line: +500 points
            Tetris (4 lines): +800 points
            
            Soft Drop: +1 point per cell
            Hard Drop: +5 points per cell""";

    // Displays the instructions dialog
    public static void showInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INSTRUCTIONS_TITLE);
        alert.setHeaderText(INSTRUCTIONS_HEADER);
        alert.setContentText(INSTRUCTIONS_CONTENT);
        alert.showAndWait();
    }

    // Displays error dialog with custom message
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
