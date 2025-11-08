package com.comp2042.ui;

import com.comp2042.game.logic.DownData;
import com.comp2042.game.logic.MoveEvent;
import com.comp2042.input.EventSource;
import com.comp2042.input.EventType;
import com.comp2042.input.InputEventListener;
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

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

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

    private Rectangle[][] holdRectangles;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private Rectangle[][] ghostRectangles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
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
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
                // Pause key (P)
                if (keyEvent.getCode() == KeyCode.P) {
                    pauseGame(null);
                    keyEvent.consume();
                }
            }
        });
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
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
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        initGhostPanel(brick);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private void initGhostPanel(ViewData brick) {
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
        return Color.rgb(200, 200, 200, 0.3);
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            refreshGhostPiece(brick);
        }
    }

    private void refreshGhostPiece(ViewData brick) {
        if (ghostPanel != null) {
            ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostXPosition() * ghostPanel.getVgap() + brick.getGhostXPosition() * BRICK_SIZE);
            ghostPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getGhostYPosition() * ghostPanel.getHgap() + brick.getGhostYPosition() * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    ghostRectangles[i][j].setFill(getGhostFillColor(brick.getBrickData()[i][j]));
                    ghostRectangles[i][j].setArcHeight(9);
                    ghostRectangles[i][j].setArcWidth(9);
                    // Add a stroke to make it more visible
                    if (brick.getBrickData()[i][j] != 0) {
                        ghostRectangles[i][j].setStroke(Color.rgb(255, 255, 255, 0.5));
                        ghostRectangles[i][j].setStrokeWidth(1);
                    } else {
                        ghostRectangles[i][j].setStroke(null);
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
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("Score: %d"));
        scoreLabel.setStyle("-fx-font-size: 20px;"); //font size
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (isGameOver.getValue() == Boolean.FALSE) {
            if (isPause.getValue() == Boolean.FALSE) {
                //Pause game
                timeLine.pause();
                isPause.setValue(Boolean.TRUE);
                showPauseOverlay();
            } else {
                //Resume game
                timeLine.play();
                isPause.setValue(Boolean.FALSE);
                hidePauseOverlay();
            }
        }
        gamePanel.requestFocus();
    }

    private void showPauseOverlay() {
        if (pauseOverlay == null) {
            pauseOverlay = new VBox();
            pauseOverlay.setAlignment(javafx.geometry.Pos.CENTER);
            pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            pauseOverlay.setPrefSize(232, 500);
            pauseOverlay.setLayoutX(0);
            pauseOverlay.setLayoutY(0);

            Label pauseLabel = new Label("PAUSED");
            pauseLabel.setStyle("-fx-font-family: 'Let\\'s go Digital'; -fx-font-size: 48px; -fx-text-fill: yellow;");

            Label resumeLabel = new Label("Press P to resume");
            resumeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

            pauseOverlay.getChildren().addAll(pauseLabel, resumeLabel);
            pauseOverlay.setSpacing(20);
        }

        // Add to the root pane
        ((Pane) gamePanel.getParent().getParent()).getChildren().add(pauseOverlay);
    }

    private void hidePauseOverlay() {
        if (pauseOverlay != null) {
            ((Pane) gamePanel.getParent().getParent()).getChildren().remove(pauseOverlay);
        }
    }

    //Instant drop implementation
    private void instantDrop(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onInstantDropEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
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
            ViewData viewData = eventListener.onHoldEvent(event);
            if (viewData != null) {
                refreshBrick(viewData);
                refreshHoldPanel(viewData.getHeldBrickData());
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
}