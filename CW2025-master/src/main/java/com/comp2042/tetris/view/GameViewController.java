package com.comp2042.tetris.view;

import com.comp2042.tetris.controller.GameStateManager;
import com.comp2042.tetris.dto.*;
import com.comp2042.tetris.input.*;
import com.comp2042.tetris.util.SceneNavigator;
import com.comp2042.tetris.view.component.*;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

import java.net.URL;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    private static final int NEXT_BRICK_LABEL_Y_OFFSET = 40;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel;
    @FXML private GridPane holdPanel;
    @FXML private GridPane nextBrickPanel;
    @FXML private Label levelLabel;
    @FXML private Label linesLabel;
    @FXML private Label highScoreLabel;

    private InputEventListener eventListener;
    private GameInputHandler inputHandler;
    private BrickRenderer brickRenderer;
    private BoardRenderer boardRenderer;
    private PauseOverlayManager pauseOverlayManager;
    private GameStateManager gameStateManager;
    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameOverPanel.setVisible(false);

        setupNextBrickLabel();
        setupGameOverPanel();
    }

    public void initGameView(int[][] boardMatrix, GameStateView brick) {
        // Initialize renderers
        boardRenderer = new BoardRenderer(gamePanel);
        boardRenderer.initBoard(boardMatrix);

        brickRenderer = new BrickRenderer(gamePanel, brickPanel, holdPanel, nextBrickPanel);
        brickRenderer.initHoldPanel();
        brickRenderer.initBrick(brick);
        brickRenderer.initGhostPanel(brick);
        brickRenderer.updateNextBrick(brick.getNextBrickData());

        // Initialize timeline
        timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Initialize game state manager
        Pane rootPane = (Pane) gamePanel.getParent().getParent();
        gameStateManager = new GameStateManager(
                timeline,
                gameOverPanel,
                rootPane,
                () -> eventListener.createNewGame()
        );

        // Initialize pause overlay
        pauseOverlayManager = new PauseOverlayManager(
                rootPane,
                () -> pauseGame(null),
                this::exitToMainMenu
        );

        timeline.play();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;

        inputHandler = new GameInputHandler(eventListener, new GameInputCallback() {
            @Override
            public void onMove(GameStateView gameStateView) {
                brickRenderer.refreshBrick(gameStateView);
                brickRenderer.refreshGhostPiece(gameStateView);
                brickRenderer.updateNextBrick(gameStateView.getNextBrickData());
            }

            @Override
            public void onMoveDown(MoveEvent event) {
                moveDown(event);
            }

            @Override
            public void onInstantDrop(MoveEvent event) {
                instantDrop(event);
            }

            @Override
            public void onHold(MoveEvent event) {
                hold(event);
            }

            @Override
            public void onNewGame() {
                newGame(null);
            }

            @Override
            public void onPause() {
                pauseGame(null);
            }
        });

        gamePanel.setOnKeyPressed(keyEvent ->
                inputHandler.handleKeyPress(keyEvent,
                        gameStateManager.isPaused(),
                        gameStateManager.isGameOver())
        );
    }

    public void bindScore(IntegerProperty scoreProperty, IntegerProperty highScoreProperty,
                          IntegerProperty levelProperty, IntegerProperty linesClearedProperty,
                          IntegerProperty linesToNextLevelProperty) {
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
        highScoreLabel.textProperty().bind(highScoreProperty.asString("High Score: %d"));

        if (levelLabel != null) {
            levelLabel.textProperty().bind(levelProperty.asString("Level: %d"));
        }

        if (linesLabel != null) {
            linesLabel.textProperty().bind(linesClearedProperty.asString("Lines: %d"));
        }
    }

    public void refreshGameBackground(int[][] board) {
        boardRenderer.refreshBoard(board);
    }

    public void gameOver() {
        gameStateManager.triggerGameOver();
    }

    public void newGame(ActionEvent actionEvent) {
        gameStateManager.startNewGame();
        gamePanel.requestFocus();
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (!gameStateManager.isGameOver()) {
            gameStateManager.togglePause();

            if (gameStateManager.isPaused()) {
                pauseOverlayManager.show();
            } else {
                pauseOverlayManager.hide();
            }
        }
        gamePanel.requestFocus();
    }

    public void updateGameSpeed(int speedInMillis) {
        gameStateManager.updateSpeed(speedInMillis,
                () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        );
    }

    private void moveDown(MoveEvent event) {
        if (!gameStateManager.isPaused()) {
            MoveResultData result = eventListener.onDownEvent(event);
            handleMoveResult(result);
        }
        gamePanel.requestFocus();
    }

    private void instantDrop(MoveEvent event) {
        if (!gameStateManager.isPaused()) {
            MoveResultData result = eventListener.onInstantDropEvent(event);
            handleMoveResult(result);
        }
        gamePanel.requestFocus();
    }

    private void hold(MoveEvent event) {
        if (!gameStateManager.isPaused()) {
            GameStateView gameStateView = eventListener.onHoldEvent(event);
            if (gameStateView != null) {
                brickRenderer.refreshBrick(gameStateView);
                brickRenderer.refreshGhostPiece(gameStateView);
                brickRenderer.refreshHoldPanel(gameStateView.getHeldBrickData());
            }
        }
        gamePanel.requestFocus();
    }

    private void handleMoveResult(MoveResultData result) {
        if (result.getClearRow() != null && result.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notification = new NotificationPanel(
                    "+" + result.getClearRow().getScoreBonus()
            );
            groupNotification.getChildren().add(notification);
            notification.showScore(groupNotification.getChildren());
        }
        brickRenderer.refreshBrick(result.getViewData());
        brickRenderer.refreshGhostPiece(result.getViewData());
        brickRenderer.updateNextBrick(result.getViewData().getNextBrickData());
    }

    private void exitToMainMenu() {
        Stage stage = (Stage) gamePanel.getScene().getWindow();
        SceneNavigator.navigateToMainMenu(stage, timeline);
    }

    private void setupNextBrickLabel() {
        Label nextBrickLabel = new Label("NEXT BRICK");
        nextBrickLabel.getStyleClass().add("nextBrickLabel");

        Pane parentPane = (Pane) nextBrickPanel.getParent();
        if (parentPane != null) {
            parentPane.getChildren().add(nextBrickLabel);
            nextBrickLabel.layoutXProperty().bind(nextBrickPanel.layoutXProperty());
            nextBrickLabel.layoutYProperty().bind(
                    nextBrickPanel.layoutYProperty().subtract(NEXT_BRICK_LABEL_Y_OFFSET)
            );
        }
    }

    private void setupGameOverPanel() {
        if (gameOverPanel != null) {
            gameOverPanel.setNewGameAction(e -> newGame(null));
            gameOverPanel.setMainMenuAction(e -> exitToMainMenu());
        }
    }
}