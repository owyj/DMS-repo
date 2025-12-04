package com.comp2042.tetris.view.component;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

// UI component that displays the game over screen
// Shows game over message and buttons for new game and main menu
public class GameOverPanel extends BorderPane {

    private Label gameOverLabel;
    private Button newGameButton;
    private Button mainMenuButton;

    // Constructor: to initialize the game over panel
    //Creates a vertical layout with game over label and action buttons
    public GameOverPanel() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(300, 400);
        container.getStyleClass().add("game-over-overlay");

        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        // Create buttons
        newGameButton = new Button("NEW GAME");
        mainMenuButton = new Button("MAIN MENU");

        // Apply CSS classes
        newGameButton.getStyleClass().add("game-over-button");
        mainMenuButton.getStyleClass().add("game-over-button");

        container.getChildren().addAll(gameOverLabel, newGameButton, mainMenuButton);
        setCenter(container);
    }

    // set action handler for new game button
    public void setNewGameAction(javafx.event.EventHandler<ActionEvent> action) {
        newGameButton.setOnAction(action);
    }

    // set action handler for main menu button
    public void setMainMenuAction(javafx.event.EventHandler<ActionEvent> action) {
        mainMenuButton.setOnAction(action);
    }
}