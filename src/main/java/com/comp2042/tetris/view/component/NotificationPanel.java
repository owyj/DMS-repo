package com.comp2042.tetris.view.component;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

// UI component that displays temporary notifications messages with animations
public class NotificationPanel extends BorderPane {

    // Constructor: to create a notification panel with specified text
    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);
        final Label score = new Label(text);
        score.getStyleClass().add("bonusStyle");
        final Effect glow = new Glow(0.6);
        score.setEffect(glow);
        score.setTextFill(Color.WHITE);
        setCenter(score);

    }

    // Display the notification with fade-out and upward movement animations
    public void showScore(ObservableList<Node> list) {
        // Fade-out animation
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        // Upward movement animation
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        // Run both animations simultaneously
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // remove this panel from scene after animation completes
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }
}
