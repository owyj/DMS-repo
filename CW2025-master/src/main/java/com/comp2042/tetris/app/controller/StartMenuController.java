package com.comp2042.tetris.app.controller;

import com.comp2042.tetris.controller.GameController;
import com.comp2042.tetris.util.MusicManager;
import com.comp2042.tetris.view.GuiController;
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
            // Load and start background music
            MusicManager musicManager = MusicManager.getInstance();
            musicManager.loadMusic();
            musicManager.playMusic();

            //load game layout
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = fxmlLoader.load();
            GuiController guiController = fxmlLoader.getController();

            //current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //game scene
            Scene gameScene = new Scene(root, 350, 600);
            stage.setScene(gameScene);
            stage.setTitle("TetrisJFX - Game");

            //Initialize the game controller
            new GameController(guiController);

        } catch (IOException e) {
            e.printStackTrace();
            showError();
        }
    }

    @FXML
    private void showInstructions(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("How to Play Tetris");
        alert.setContentText(
                """
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
                        Hard Drop: +5 points per cell"""
        );
        alert.showAndWait();
    }

    @FXML
    private void exitGame(ActionEvent event) {
        //stop music when exiting
        MusicManager.getInstance().stopMusic();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Failed to load game");
        alert.showAndWait();
    }
}