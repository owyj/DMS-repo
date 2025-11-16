package com.comp2042.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;


public class GameOverPanel extends BorderPane {

    private Label gameOverLabel;
    private Label instructionLabel;

    public GameOverPanel() {

        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(200, 100);

        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        instructionLabel = new Label("Press 'N' for New Game");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        container.getChildren().addAll(gameOverLabel, instructionLabel);
        setCenter(container);
    }

}
