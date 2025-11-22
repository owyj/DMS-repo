package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class GameOverPanel extends BorderPane {

    private Label gameOverLabel;
    private Button newGameButton;
    private Button mainMenuButton;

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

    // Getter methods for buttons so GuiController can set actions
    public Button getNewGameButton() {
        return newGameButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }

    public void setNewGameAction(javafx.event.EventHandler<ActionEvent> action) {
        newGameButton.setOnAction(action);
    }

    public void setMainMenuAction(javafx.event.EventHandler<ActionEvent> action) {
        mainMenuButton.setOnAction(action);
    }
}