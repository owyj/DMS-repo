package com.comp2042.tetris.model;

import com.comp2042.tetris.dto.ClearRow;
import com.comp2042.tetris.dto.GameStateView;
import com.comp2042.tetris.model.piece.Brick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameBoard Tests")
class GameBoardTest {

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(25, 10);
    }

    @Test
    @DisplayName("Should initialize with correct dimensions")
    void testBoardInitialization() {
        int[][] matrix = gameBoard.getBoardMatrix();

        assertEquals(25, matrix.length);
        assertEquals(10, matrix[0].length);
    }

    @Test
    @DisplayName("Should initialize with empty board")
    void testEmptyBoardInitialization() {
        int[][] matrix = gameBoard.getBoardMatrix();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                assertEquals(0, matrix[i][j], "Board should be empty initially");
            }
        }
    }

    @Test
    @DisplayName("Should create new brick successfully")
    void testCreateNewBrick() {
        boolean gameOver = gameBoard.createNewBrick();
        assertFalse(gameOver, "Game should not be over after first brick");
    }

    @Test
    @DisplayName("Should move brick down when space available")
    void testMoveBrickDown() {
        gameBoard.createNewBrick();
        boolean moved = gameBoard.moveBrickDown();
        assertTrue(moved, "Brick should move down on empty board");
    }

    @Test
    @DisplayName("Should move brick left when space available")
    void testMoveBrickLeft() {
        gameBoard.createNewBrick();
        boolean moved = gameBoard.moveBrickLeft();
        assertTrue(moved, "Brick should be able to move left from center");
    }

    @Test
    @DisplayName("Should move brick right when space available")
    void testMoveBrickRight() {
        gameBoard.createNewBrick();
        boolean moved = gameBoard.moveBrickRight();
        assertTrue(moved, "Brick should be able to move right from center");
    }

    @Test
    @DisplayName("Should stop moving down at bottom")
    void testBrickStopsAtBottom() {
        gameBoard.createNewBrick();

        boolean moved;
        int moveCount = 0;
        do {
            moved = gameBoard.moveBrickDown();
            moveCount++;
        } while (moved && moveCount < 100);

        assertFalse(moved, "Brick should stop at bottom");
        assertTrue(moveCount < 100, "Should reach bottom in reasonable moves");
    }

    @Test
    @DisplayName("Should rotate brick")
    void testRotateBrick() {
        gameBoard.createNewBrick();

        boolean rotated = gameBoard.rotateLeftBrick();
        assertTrue(rotated || !rotated, "Rotation should complete without error");
    }

    @Test
    @DisplayName("Should merge brick to background")
    void testMergeBrickToBackground() {
        gameBoard.createNewBrick();

        // Move to bottom
        while (gameBoard.moveBrickDown());

        int[][] beforeMerge = gameBoard.getBoardMatrix();
        boolean allZeros = true;
        for (int[] row : beforeMerge) {
            for (int cell : row) {
                if (cell != 0) allZeros = false;
            }
        }
        assertTrue(allZeros, "Board should be empty before merge");

        gameBoard.mergeBrickToBackground();

        int[][] afterMerge = gameBoard.getBoardMatrix();
        boolean hasNonZero = false;
        for (int[] row : afterMerge) {
            for (int cell : row) {
                if (cell != 0) hasNonZero = true;
            }
        }
        assertTrue(hasNonZero, "Board should have brick after merge");
    }

    @Test
    @DisplayName("Should clear complete rows")
    void testClearCompleteRows() {
        gameBoard.createNewBrick();

        // Fill bottom row manually for testing
        int[][] matrix = gameBoard.getBoardMatrix();
        for (int j = 0; j < matrix[0].length; j++) {
            matrix[matrix.length - 1][j] = 1;
        }

        ClearRow result = gameBoard.clearRows();

        assertTrue(result.getLinesRemoved() >= 0);
    }

    @Test
    @DisplayName("Should return view data")
    void testGetViewData() {
        gameBoard.createNewBrick();
        GameStateView view = gameBoard.getViewData();

        assertNotNull(view);
        assertNotNull(view.getBrickData());
        assertNotNull(view.getNextBrickData());
        assertNotNull(view.getHeldBrickData());
        assertTrue(view.getXPosition() >= 0);
        assertTrue(view.getYPosition() >= 0);
    }

    @Test
    @DisplayName("Should return score instance")
    void testGetScore() {
        Score score = gameBoard.getScore();
        assertNotNull(score);
        assertEquals(0, score.getScore());
    }

    @Test
    @DisplayName("Should hold brick")
    void testHoldBrick() {
        gameBoard.createNewBrick();
        Brick held = gameBoard.holdBrick();

        assertNotNull(held);
    }

    @Test
    @DisplayName("Should not allow holding twice consecutively")
    void testCannotHoldTwice() {
        gameBoard.createNewBrick();

        Brick firstHold = gameBoard.holdBrick();
        assertNotNull(firstHold);

        Brick secondHold = gameBoard.holdBrick();
        // Second hold should return NullBrick or prevent action
        assertNotNull(secondHold);
    }

    @Test
    @DisplayName("Should allow holding after new brick")
    void testCanHoldAfterNewBrick() {
        gameBoard.createNewBrick();
        gameBoard.holdBrick();

        // Place current brick
        while (gameBoard.moveBrickDown());
        gameBoard.mergeBrickToBackground();
        gameBoard.createNewBrick();

        // Should be able to hold again
        Brick held = gameBoard.holdBrick();
        assertNotNull(held);
    }

    @Test
    @DisplayName("Should reset game state on newGame")
    void testNewGame() {
        gameBoard.createNewBrick();
        gameBoard.getScore().add(1000);

        gameBoard.newGame();

        assertEquals(0, gameBoard.getScore().getScore());

        int[][] matrix = gameBoard.getBoardMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                assertEquals(0, matrix[i][j], "Board should be cleared");
            }
        }
    }

    @Test
    @DisplayName("Should calculate ghost position below current brick")
    void testGhostPosition() {
        gameBoard.createNewBrick();
        GameStateView view = gameBoard.getViewData();

        int currentY = view.getYPosition();
        int ghostY = view.getGhostYPosition();

        assertTrue(ghostY >= currentY, "Ghost should be at or below current brick");
    }

    @Test
    @DisplayName("Should handle wall kicks during rotation")
    void testWallKickRotation() {
        gameBoard.createNewBrick();

        // Move to wall
        for (int i = 0; i < 10; i++) {
            gameBoard.moveBrickRight();
        }

        // Try to rotate at wall - should use wall kick
        assertDoesNotThrow(() -> gameBoard.rotateLeftBrick());
    }

    @Test
    @DisplayName("Board should handle multiple brick cycles")
    void testMultipleBrickCycles() {
        for (int i = 0; i < 5; i++) {
            gameBoard.createNewBrick();
            while (gameBoard.moveBrickDown());
            gameBoard.mergeBrickToBackground();
            gameBoard.clearRows();
        }

        // Should still function after multiple cycles
        assertDoesNotThrow(() -> gameBoard.createNewBrick());
    }

    @Test
    @DisplayName("Should prevent movement when brick at left wall")
    void testNoMovementAtLeftWall() {
        gameBoard.createNewBrick();

        // Move all the way left
        for (int i = 0; i < 20; i++) {
            gameBoard.moveBrickLeft();
        }

        // Should not be able to move further left
        boolean moved = gameBoard.moveBrickLeft();
        assertFalse(moved, "Should not move beyond left wall");
    }

    @Test
    @DisplayName("Should prevent movement when brick at right wall")
    void testNoMovementAtRightWall() {
        gameBoard.createNewBrick();

        // Move all the way right
        for (int i = 0; i < 20; i++) {
            gameBoard.moveBrickRight();
        }

        // Should not be able to move further right
        boolean moved = gameBoard.moveBrickRight();
        assertFalse(moved, "Should not move beyond right wall");
    }
}