package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.LevelManager;
import com.comp2042.tetris.dto.MoveResultData;
import com.comp2042.tetris.dto.MoveEvent;
import com.comp2042.tetris.input.EventSource;
import com.comp2042.tetris.input.InputEventListener;
import com.comp2042.tetris.dto.ClearRow;
import com.comp2042.tetris.model.Board;
import com.comp2042.tetris.view.GameViewController;
import com.comp2042.tetris.model.GameBoard;
import com.comp2042.tetris.dto.GameStateView;

/**
 * Main controller for the Tetris game.
 * Handles game logic, user input, scoring, and level progression.
 * Acts as intermediary between the game model and view components.
 */
public class GameController implements InputEventListener {

    // Points awarded for soft and hard drops
    private static final int SOFT_DROP_POINTS = 1;
    private static final int HARD_DROP_POINTS = 5;

    // Game board managing pieces and grid state
    private Board board = new GameBoard(25, 10);

    // View controller for updating the GUI
    private final GameViewController viewGameViewController;

    // Level manager for handling level progression and speed
    private final LevelManager levelManager = new LevelManager();

    /**
     * Initializes the game controller.
     * Sets up the game board, connects to the view, and binds score/level properties.
     */
    public GameController(GameViewController c) {
        viewGameViewController = c;
        board.createNewBrick();
        viewGameViewController.setEventListener(this);
        viewGameViewController.initGameView(board.getBoardMatrix(), board.getViewData());

        // Pass level properties to GUI
        viewGameViewController.bindScore(
                board.getScore().scoreProperty(),
                board.getScore().highScoreProperty(),
                levelManager.currentLevelProperty(),
                levelManager.totalLinesClearedProperty(),
                levelManager.linesToNextLevelProperty()
        );

        // Set initial game speed
        updateGameSpeed();
    }

    /**
     * Handles downward movement of the current brick.
     * If brick can't move down, it merges with the board and clears completed rows.
     * Awards soft drop points if the move was user-initiated.
     */
    @Override
    public MoveResultData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            // Merge the brick into the board
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            // Award points for cleared rows
            if (clearRow != null && clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());

                // Add cleared lines to level manager
                levelManager.addLinesCleared(clearRow.getLinesRemoved());

                // Update game speed based on new level
                updateGameSpeed();
            }
            // Create a new brick; if unable, the game is over
            if (board.createNewBrick()) {
                board.getScore().saveIfHighScore();
                viewGameViewController.gameOver();
            }
            viewGameViewController.refreshGameBackground(board.getBoardMatrix());
        } else {
            // Award soft drop points for user-initiated down moves
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(SOFT_DROP_POINTS);
            }
        }
        return new MoveResultData(clearRow, board.getViewData());
    }

    // Updates the game speed in the view based on the current level
    private void updateGameSpeed() {
        int newSpeed = levelManager.getCurrentSpeed();
        viewGameViewController.updateGameSpeed(newSpeed);
    }

    // Move brick left
    @Override
    public GameStateView onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    // Move brick right
    @Override
    public GameStateView onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    // Rotate brick clockwise
    @Override
    public GameStateView onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    // Instant drop the brick to the bottom
    @Override
    public MoveResultData onInstantDropEvent(MoveEvent event) {
        //Keep moving down until it can't move anymore
        int dropDistance = 0;
        while (board.moveBrickDown()) {
            dropDistance++;
        }

        // Merge the brick and clear rows
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());

            // Add cleared lines to level manager
            levelManager.addLinesCleared(clearRow.getLinesRemoved());

            // Update game speed based on new level
            updateGameSpeed();
        }

        // Add bonus points for hard drop (5 points per cell dropped)
        board.getScore().add(dropDistance * HARD_DROP_POINTS);

        // Create new brick
        if (board.createNewBrick()) {
            board.getScore().saveIfHighScore();
            viewGameViewController.gameOver();
        }

        viewGameViewController.refreshGameBackground(board.getBoardMatrix());

        return new MoveResultData(clearRow, board.getViewData());
    }

    // Start a new game
    @Override
    public void createNewGame() {
        board.newGame();
        board.getScore().reloadHighScoreFromFile(); // force reload after new game
        levelManager.reset(); // reset level on new game
        updateGameSpeed(); // set initial game speed
        viewGameViewController.refreshGameBackground(board.getBoardMatrix());
    }

    // Hold the current brick
    @Override
    public GameStateView onHoldEvent(MoveEvent event) {
        board.holdBrick();
        viewGameViewController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }
}