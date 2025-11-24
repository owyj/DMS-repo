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

public class GameController implements InputEventListener {

    private Board board = new GameBoard(25, 10);

    private final GameViewController viewGameViewController;

    private final LevelManager levelManager = new LevelManager();

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

    @Override
    public MoveResultData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            System.out.println("DEBUG GameController: clearRow = " + clearRow);
            if (clearRow != null) {
                System.out.println("DEBUG GameController: clearRow.getLinesRemoved() = " + clearRow.getLinesRemoved());
                System.out.println("DEBUG GameController: clearRow.getScoreBonus() = " + clearRow.getScoreBonus());
            }

            if (clearRow != null && clearRow.getLinesRemoved() > 0) {
                System.out.println("DEBUG GameController: Clearing " + clearRow.getLinesRemoved() + " rows");
                board.getScore().add(clearRow.getScoreBonus());

                // Add cleared lines to level manager
                System.out.println("DEBUG GameController: About to call levelManager.addLinesCleared()");
                levelManager.addLinesCleared(clearRow.getLinesRemoved());
                System.out.println("DEBUG GameController: After calling levelManager.addLinesCleared()");
                System.out.println("DEBUG GameController: Current level = " + levelManager.getCurrentLevel());
                System.out.println("DEBUG GameController: Total lines = " + levelManager.getTotalLinesCleared());

                // Update game speed based on new level
                updateGameSpeed();
            }
            if (board.createNewBrick()) {
                board.getScore().saveIfHighScore();
                viewGameViewController.gameOver();
            }
            viewGameViewController.refreshGameBackground(board.getBoardMatrix());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new MoveResultData(clearRow, board.getViewData());
    }

    private void updateGameSpeed() {
        int newSpeed = levelManager.getCurrentSpeed();
        viewGameViewController.updateGameSpeed(newSpeed);
    }

    @Override
    public GameStateView onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public GameStateView onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public GameStateView onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public MoveResultData onInstantDropEvent(MoveEvent event) {
        //Keep moving down until it can't move anymore
        int dropDistance = 0;
        while (board.moveBrickDown()) {
            dropDistance++;
        }

        //Merge the brick and clear rows
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        System.out.println("DEBUG GameController instantDrop: clearRow = " + clearRow);
        if (clearRow != null) {
            System.out.println("DEBUG GameController instantDrop: clearRow.getLinesRemoved() = " + clearRow.getLinesRemoved());
        }

        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());

            // Add cleared lines to level manager
            System.out.println("DEBUG GameController instantDrop: About to call levelManager.addLinesCleared()");
            levelManager.addLinesCleared(clearRow.getLinesRemoved());

            // Update game speed based on new level
            updateGameSpeed();
        }

        //Add bonus points for hard drop (5 points per cell dropped)
        board.getScore().add(dropDistance * 5);

        //Create new brick
        if (board.createNewBrick()) {
            board.getScore().saveIfHighScore();
            viewGameViewController.gameOver();
        }

        viewGameViewController.refreshGameBackground(board.getBoardMatrix());

        return new MoveResultData(clearRow, board.getViewData());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        levelManager.reset(); // reset level on new game
        updateGameSpeed(); // set initial game speed
        viewGameViewController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public GameStateView onHoldEvent(MoveEvent event) {
        board.holdBrick();
        viewGameViewController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }
}