package com.comp2042.tetris.view;

import com.comp2042.tetris.dto.MoveResultData;
import com.comp2042.tetris.dto.MoveEvent;
import com.comp2042.tetris.dto.GameStateView;
import com.comp2042.tetris.input.EventSource;
import com.comp2042.tetris.input.EventType;
import com.comp2042.tetris.input.InputEventListener;
import com.comp2042.tetris.util.MusicManager;
import com.comp2042.tetris.view.component.GameOverPanel;
import com.comp2042.tetris.view.component.NotificationPanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    private static final int BRICK_SIZE = 20;

    private static final int BRICK_PANEL_Y_OFFSET_INIT = -42;
    private static final int BRICK_PANEL_Y_OFFSET_REFRESH = -45;
    private static final int GHOST_PANEL_X_OFFSET = -1;
    private static final int GHOST_PANEL_Y_OFFSET = -48;
    private static final int NEXT_BRICK_LABEL_Y_OFFSET = 40;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane holdPanel;

    @FXML
    private Button pauseButton;

    @FXML
    private VBox pauseOverlay;

    @FXML
    private GridPane ghostPanel;

    @FXML
    private Label highScoreLabel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label linesLabel;

    private Rectangle[][] holdRectangles;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private Rectangle[][] ghostRectangles;

    private IntegerProperty currentScoreProperty;

    private IntegerProperty currentHighScoreProperty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResourceAsStream("digital.ttf"), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        setupGameOverPanel();

        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                // Always allow 'P' for pause and 'N' for new game (except when game is over)
                if (keyEvent.getCode() == KeyCode.N && isPause.getValue() == Boolean.FALSE) {
                    newGame(null);
                    keyEvent.consume();
                    return;
                }
                if (keyEvent.getCode() == KeyCode.P && isGameOver.getValue() == Boolean.FALSE) {
                    pauseGame(null);
                    keyEvent.consume();
                    return;
                }

                // Only process game controls if not paused and not game over
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        instantDrop(new MoveEvent(EventType.INSTANT_DROP, EventSource.USER));
                        keyEvent.consume();
                    }
                    //Brick hold key (C or Shift)
                    if (keyEvent.getCode() == KeyCode.C || keyEvent.getCode() == KeyCode.SHIFT) {
                        hold(new MoveEvent(EventType.HOLD, EventSource.USER));
                        keyEvent.consume();
                    }
                }
            }
        });
        gameOverPanel.setVisible(false);

        Label nextBrickLabel = new Label("NEXT BRICK");
        nextBrickLabel.getStyleClass().add("nextBrickLabel");

        Pane parentPane = (Pane) nextBrickPanel.getParent();
        if (parentPane != null) {
            parentPane.getChildren().add(nextBrickLabel);

            nextBrickLabel.layoutXProperty().bind(nextBrickPanel.layoutXProperty());
            nextBrickLabel.layoutYProperty().bind(nextBrickPanel.layoutYProperty().subtract(NEXT_BRICK_LABEL_Y_OFFSET));
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, GameStateView brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        initHoldPanel();

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getXPosition() * brickPanel.getVgap() + brick.getXPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(BRICK_PANEL_Y_OFFSET_INIT + gamePanel.getLayoutY() + brick.getYPosition() * brickPanel.getHgap() + brick.getYPosition() * BRICK_SIZE);

        initGhostPanel(brick);

        int initialSpeed = 500; // Default speed
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(initialSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        updateNextBrick(brick.getNextBrickData());
    }

    private void initGhostPanel(GameStateView brick) {
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                ghostRectangles[i][j] = rectangle;
                if (ghostPanel == null) {
                    ghostPanel = new GridPane();
                    ghostPanel.setVgap(1);
                    ghostPanel.setHgap(1);
                    ((Pane) gamePanel.getParent()).getChildren().add(0, ghostPanel);
                }
                ghostPanel.add(rectangle, j, i);
            }
        }
        refreshGhostPiece(brick);
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    private Paint getGhostFillColor(int i) {
        if (i == 0) {
            return Color.TRANSPARENT;
        }
        // Return a semi-transparent white/gray for ghost piece
        return Color.rgb(255, 255, 255, 0.2);
    }

    private void refreshBrick(GameStateView brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getXPosition() * brickPanel.getVgap() + brick.getXPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(BRICK_PANEL_Y_OFFSET_REFRESH + gamePanel.getLayoutY() + brick.getYPosition() * brickPanel.getHgap() + brick.getYPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            refreshGhostPiece(brick);

            // Update next brick display whenever the brick refreshes
            updateNextBrick(brick.getNextBrickData());
        }
    }

    private void refreshGhostPiece(GameStateView brick) {
        if (ghostPanel != null && brick != null) {
            ghostPanel.setLayoutX(GHOST_PANEL_X_OFFSET + gamePanel.getLayoutX() + brick.getGhostXPosition() * ghostPanel.getVgap() + brick.getGhostXPosition() * BRICK_SIZE);
            ghostPanel.setLayoutY(GHOST_PANEL_Y_OFFSET + gamePanel.getLayoutY() + brick.getGhostYPosition() * ghostPanel.getHgap() + brick.getGhostYPosition() * BRICK_SIZE);

            int[][] brickData = brick.getBrickData();
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    if (i < ghostRectangles.length && j < ghostRectangles[i].length) {
                        ghostRectangles[i][j].setFill(getGhostFillColor(brickData[i][j]));
                        ghostRectangles[i][j].setArcHeight(9);
                        ghostRectangles[i][j].setArcWidth(9);

                        // Enhanced border for better visibility
                        if (brickData[i][j] != 0) {
                            ghostRectangles[i][j].setStroke(Color.rgb(180, 180, 180, 0.6));
                            ghostRectangles[i][j].setStrokeWidth(1.5);
                        } else {
                            ghostRectangles[i][j].setStroke(null);
                        }
                    }
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            MoveResultData moveResultData = eventListener.onDownEvent(event);
            if (moveResultData.getClearRow() != null && moveResultData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + moveResultData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(moveResultData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty, IntegerProperty highScoreProperty,
                          IntegerProperty levelProperty, IntegerProperty linesClearedProperty,
                          IntegerProperty linesToNextLevelProperty) {
        this.currentScoreProperty = scoreProperty;
        this.currentHighScoreProperty = highScoreProperty;

        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
        highScoreLabel.textProperty().bind(highScoreProperty.asString("High Score: %d"));

        // display current level
        if (levelLabel != null) {
            levelLabel.textProperty().bind(levelProperty.asString("Level: %d"));
        }

        if (linesLabel != null) {
            // show only total lines cleared
            linesLabel.textProperty().bind(linesClearedProperty.asString("Lines: %d"));
        }
    }

    public void gameOver() {
        timeLine.stop();
        //pause music when game over
        MusicManager.getInstance().pauseMusic();

        // Check if it's a new high score
        boolean isNewHighScore = currentScoreProperty.getValue() > currentHighScoreProperty.getValue();

        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        gameOverPanel.toFront();

        // Position the game over panel to cover the game area
        Pane rootPane = (Pane) gamePanel.getParent().getParent();
        if (gameOverPanel.getParent() != rootPane) {
            // Remove from current parent and add to root pane for proper positioning
            if (gameOverPanel.getParent() != null) {
                ((Pane) gameOverPanel.getParent()).getChildren().remove(gameOverPanel);
            }
            rootPane.getChildren().add(gameOverPanel);
        }

        // Set position and size to cover the game area
        gameOverPanel.setLayoutX(0);
        gameOverPanel.setLayoutY(0);
        gameOverPanel.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        //reset music when starting new game
        MusicManager.getInstance().resetMusic();
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (isGameOver.getValue() == Boolean.FALSE) {
            if (isPause.getValue() == Boolean.FALSE) {
                //Pause game and music
                timeLine.pause();
                MusicManager.getInstance().pauseMusic();
                isPause.setValue(Boolean.TRUE);
                showPauseOverlay();
            } else {
                //Resume game
                timeLine.play();
                MusicManager.getInstance().playMusic();
                isPause.setValue(Boolean.FALSE);
                hidePauseOverlay();
            }
        }
        gamePanel.requestFocus();
    }

    private void showPauseOverlay() {
        if (pauseOverlay == null) {
            pauseOverlay = new VBox(20);
            pauseOverlay.setAlignment(javafx.geometry.Pos.CENTER);
            pauseOverlay.getStyleClass().add("pause-overlay");

            // Set the overlay to cover the entire game area
            Pane rootPane = (Pane) gamePanel.getParent().getParent();
            pauseOverlay.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
            pauseOverlay.setLayoutX(0);
            pauseOverlay.setLayoutY(0);

            Label pauseLabel = new Label("PAUSED");
            pauseLabel.getStyleClass().add("pause-title");

            Label resumeLabel = new Label("Press 'P' to resume");
            resumeLabel.getStyleClass().add("pause-subtitle");

            // Create menu buttons with consistent sizing
            Button resumeButton = new Button("RESUME");
            Button instructionsButton = new Button("INSTRUCTIONS");
            Button exitButton = new Button("MAIN MENU");

            // Apply CSS classes for consistent styling
            resumeButton.getStyleClass().add("pause-button");
            instructionsButton.getStyleClass().add("pause-button");
            exitButton.getStyleClass().add("pause-button");

            // Button actions
            resumeButton.setOnAction(e -> pauseGame(null));
            instructionsButton.setOnAction(e -> showInstructions());
            exitButton.setOnAction(e -> exitToMainMenu());

            pauseOverlay.getChildren().addAll(pauseLabel, resumeLabel, resumeButton, instructionsButton, exitButton);
        } else {
            // Update size in case window was resized
            Pane rootPane = (Pane) gamePanel.getParent().getParent();
            pauseOverlay.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        }

        // Add to the root pane
        Pane rootPane = (Pane) gamePanel.getParent().getParent();
        if (!rootPane.getChildren().contains(pauseOverlay)) {
            rootPane.getChildren().add(pauseOverlay);
        }
        pauseOverlay.toFront();
    }

    private void hidePauseOverlay() {
        if (pauseOverlay != null) {
            Pane rootPane = (Pane) gamePanel.getParent().getParent();
            rootPane.getChildren().remove(pauseOverlay);
        }
    }

    private void showInstructions() {
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

    private void exitToMainMenu() {
        // Stop the game timeline
        if (timeLine != null) {
            timeLine.stop();
        }

        // Stop music
        MusicManager.getInstance().stopMusic();

        try {
            // Load the start menu
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("startMenu.fxml"));
            Parent root = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) gamePanel.getScene().getWindow();

            // Create and set the main menu scene
            Scene menuScene = new Scene(root, 350, 600);
            stage.setScene(menuScene);
            stage.setTitle("TetrisJFX - Main Menu");

        } catch (IOException e) {
            e.printStackTrace();
            // Fallback: just close the pause overlay
            hidePauseOverlay();
            isPause.setValue(Boolean.FALSE);
        }
    }

    //Instant drop implementation
    private void instantDrop(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            MoveResultData moveResultData = eventListener.onInstantDropEvent(event);
            if (moveResultData.getClearRow() != null && moveResultData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + moveResultData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(moveResultData.getViewData());
        }
        gamePanel.requestFocus();
    }

    //Brick hold implementation
    public void initHoldPanel() {
        holdRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                holdRectangles[i][j] = rectangle;
                holdPanel.add(rectangle, j, i);
            }
        }
    }

    private void hold(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            GameStateView gameStateView = eventListener.onHoldEvent(event);
            if (gameStateView != null) {
                refreshBrick(gameStateView);
                refreshHoldPanel(gameStateView.getHeldBrickData());
            }
        }
        gamePanel.requestFocus();
    }

    private void refreshHoldPanel(int[][] heldBrick) {
        for (int i = 0; i < heldBrick.length; i++) {
            for (int j = 0; j < heldBrick[i].length; j++) {
                setRectangleData(heldBrick[i][j], holdRectangles[i][j]);
            }
        }
    }

    private void updateNextBrick(int[][] nextBrickData) {
        nextBrickPanel.getChildren().clear(); // Clear current next brick display

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(nextBrickData[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                nextBrickPanel.add(rectangle, j, i);
            }
        }
    }

    public void updateGameSpeed(int speedInMillis) {
        if (timeLine != null) {
            timeLine.stop();
            timeLine = new Timeline(new KeyFrame(
                    Duration.millis(speedInMillis),
                    ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));
            timeLine.setCycleCount(Timeline.INDEFINITE);

            // Only play if game is not paused and not over
            if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                timeLine.play();
            }
        }
    }

    private void setupGameOverPanel() {
        if (gameOverPanel != null) {
            gameOverPanel.setNewGameAction(e -> newGame(null));
            gameOverPanel.setMainMenuAction(e -> exitToMainMenu());
        }
    }
}