package com.comp2042;

import com.comp2042.game.logic.GameController;
import com.comp2042.ui.GuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenuController {

    @FXML
    private void startGame(ActionEvent event) {
        try {
            //load game layout
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = fxmlLoader.load();
            GuiController guiController = fxmlLoader.getController();

            //current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //game scene
            Scene gameScene = new Scene(root, 350, 500);
            stage.setScene(gameScene);
            stage.setTitle("TetrisJFX - Game");

            //Initialize the game controller
            new GameController(guiController);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load game");
        }
    }

    @FXML
    private void showInstructions(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("How to Play Tetris");
        alert.setContentText(
                "Controls:\n\n" +
                        "← / A - Move Left\n" +
                        "→ / D - Move Right\n" +
                        "↑ / W - Rotate\n" +
                        "↓ / S - Move Down\n\n" +

                        "Space - Instant Drop\n" +
                        "C / SHIFT - Hold Piece\n" +
                        "P - Pause Game\n" +
                        "N - New Game\n\n" +

                        "Scoring:\n\n" +
                        "Single Line: +100 points\n" +
                        "Double Line: +300 points\n" +
                        "Triple Line: +500 points\n" +
                        "Tetris (4 lines): +800 points\n\n" +

                        "Soft Drop: +1 point per cell\n" +
                        "Hard Drop: +5 points per cell"
        );
        alert.showAndWait();
    }

    @FXML
    private void exitGame(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}