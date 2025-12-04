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

// Controller class for the main game view
// Manages game board display, user input handling, game state updates, and coordination between various game components (renderers, managers, UI panels)
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

    // Initialize the game view controller when FXML is loaded
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameOverPanel.setVisible(false);

        setupNextBrickLabel();
        setupGameOverPanel();
    }

    // Initialize the game view with the board and initial brick
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

    /**
     * Set the event listener for game events and configure input handling.
     * Establishes callbacks for various game actions (move, drop, hold, pause, etc.).
     *
     * @param eventListener the listener to handle game events
     */
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
                        gameStateManager.canMove(),
                        gameStateManager.canPause())
        );
    }

    /**
     * Bind game statistics to UI labels.
     * Creates property bindings for score, high score, level, and lines cleared displays.
     *
     * @param scoreProperty the property containing the current score
     * @param highScoreProperty the property containing the high score
     * @param levelProperty the property containing the current level
     * @param linesClearedProperty the property containing total lines cleared
     * @param linesToNextLevelProperty the property containing lines needed for next level
     */
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

    /**
     * Refresh the game board background display.
     * Updates the visual representation of the game board.
     *
     * @param board the current game board matrix to render
     */
    public void refreshGameBackground(int[][] board) {
        boardRenderer.refreshBoard(board);
    }

    // Trigger game over state through the game state manager
    public void gameOver() {
        gameStateManager.triggerGameOver();
    }

    /**
     * Start a new game.
     * Resets game state and restarts the game through the state manager.
     *
     * @param actionEvent the action event that triggered this method (can be null)
     */
    public void newGame(ActionEvent actionEvent) {
        gameStateManager.startNewGame();
        gamePanel.requestFocus();
    }

    /**
     * Toggle pause state of the game.
     * Shows or hides the pause overlay based on the new pause state.
     *
     * @param actionEvent the action event that triggered this method (can be null)
     */
    public void pauseGame(ActionEvent actionEvent) {
        if (gameStateManager.canPause()) {
            gameStateManager.togglePause();

            if (!gameStateManager.canMove()) {
                pauseOverlayManager.show();
            } else {
                pauseOverlayManager.hide();
            }
        }
        gamePanel.requestFocus();
    }

    /**
     * Update the game speed (brick drop rate).
     * Adjusts the timeline speed to match the new difficulty level.
     *
     * @param speedInMillis the new speed in milliseconds between automatic drops
     */
    public void updateGameSpeed(int speedInMillis) {
        gameStateManager.updateSpeed(speedInMillis,
                () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        );
    }

    /**
     * Move the current brick down by one row.
     * Handles the result of the move including line clears and display updates.
     *
     * @param event the move event triggering this action
     */
    private void moveDown(MoveEvent event) {
        if (gameStateManager.canMove()) {
            MoveResultData result = eventListener.onDownEvent(event);
            handleMoveResult(result);
        }
        gamePanel.requestFocus();
    }

    /**
     * Instantly drop the current brick to its landing position.
     * Performs a hard drop and handles the result.
     *
     * @param event the move event triggering this action
     */
    private void instantDrop(MoveEvent event) {
        if (gameStateManager.canMove()) {
            MoveResultData result = eventListener.onInstantDropEvent(event);
            handleMoveResult(result);
        }
        gamePanel.requestFocus();
    }

    /**
     * Hold the current brick and swap it with the held brick.
     * Updates the hold panel display with the newly held brick.
     *
     * @param event the move event triggering this action
     */
    private void hold(MoveEvent event) {
        if (gameStateManager.canMove()) {
            GameStateView gameStateView = eventListener.onHoldEvent(event);
            if (gameStateView != null) {
                brickRenderer.refreshBrick(gameStateView);
                brickRenderer.refreshGhostPiece(gameStateView);
                brickRenderer.refreshHoldPanel(gameStateView.getHeldBrickData());
            }
        }
        gamePanel.requestFocus();
    }

    /**
     * Handle the result of a brick movement.
     * Shows score notifications for line clears and updates brick displays.
     *
     * @param result the result data from the movement including cleared lines and updated state
     */
    private void handleMoveResult(MoveResultData result) {
        if (result.getClearRow() != null && result.getClearRow().getLinesRemoved() > 0) {
            // Show score bonus notification for cleared lines
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

    // Exit to main menu
    private void exitToMainMenu() {
        Stage stage = (Stage) gamePanel.getScene().getWindow();
        SceneNavigator.navigateToMainMenu(stage, timeline);
    }

    // Set up the "NEXT BRICK" label above the next brick preview panel
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

    // Set up game over panel with action handlers
    // Configures new game and main menu buttons actions
    private void setupGameOverPanel() {
        if (gameOverPanel != null) {
            gameOverPanel.setNewGameAction(e -> newGame(null));
            gameOverPanel.setMainMenuAction(e -> exitToMainMenu());
        }
    }
}