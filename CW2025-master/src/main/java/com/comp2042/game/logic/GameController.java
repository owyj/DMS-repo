package com.comp2042.game.logic;

import com.comp2042.input.EventSource;
import com.comp2042.input.InputEventListener;
import com.comp2042.logic.bricks.ClearRow;
import com.comp2042.ui.GuiController;
import com.comp2042.ui.SimpleBoard;
import com.comp2042.ui.ViewData;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    private final LevelManager levelManager = new LevelManager();

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());

        // Pass level properties to GUI
        viewGuiController.bindScore(
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
    public DownData onDownEvent(MoveEvent event) {
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
                viewGuiController.gameOver();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    private void updateGameSpeed() {
        int newSpeed = levelManager.getCurrentSpeed();
        viewGuiController.updateGameSpeed(newSpeed);
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public DownData onInstantDropEvent(MoveEvent event) {
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
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        levelManager.reset(); // reset level on new game
        updateGameSpeed(); // set initial game speed
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        board.holdBrick();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return board.getViewData();
    }
}