package com.comp2042.tetris.model;

import com.comp2042.tetris.model.piece.BrickRotator;
import com.comp2042.tetris.dto.ClearRow;
import com.comp2042.tetris.model.piece.types.NullBrick;
import com.comp2042.tetris.util.MatrixOperations;
import com.comp2042.tetris.dto.NextBrickInfo;
import com.comp2042.tetris.model.piece.Brick;
import com.comp2042.tetris.model.piece.BrickGenerator;
import com.comp2042.tetris.model.piece.RandomBrickGenerator;
import com.comp2042.tetris.dto.GameStateView;

import java.awt.*;
import java.util.List;

/**
 * Core game board implementation for Tetris.
 * Manages the playing field, brick movement, collision detection, rotation with wall kicks, the hold mechanic, and ghost piece calculation.
 * The board uses a matrix representation where 0 represents empty space and positive integers (1-7) represent different brick colors/types.
 */
public class GameBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick = NullBrick.getInstance();
    private boolean canHold = true;

    /**
     * Initializes a new game board with specified dimensions.
     *
     * @param width Number of rows in the board (typically 25, including hidden rows)
     * @param height Number of columns in the board (typically 10)
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Attempts to move the current brick down by one row.
     *
     * @return true if the brick moved successfully, false if blocked
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current brick left by one column.
     *
     * @return true if the brick moved successfully, false if blocked
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current brick right by one column.
     *
     * @return true if the brick moved successfully, false if blocked
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to rotate the current brick left (counter-clockwise) with wall kicks.
     *
     * @return true if the rotation was successful, false if blocked
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextBrickInfo nextShape = brickRotator.peekNextShape();

        // First try normal rotation
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(),
                (int) currentOffset.getX(), (int) currentOffset.getY());

        if (!conflict) {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        // More extensive wall kicks for I-brick
        Point[] wallKicks;

        if (isIBrick()) {
            // I-brick wall kick data (extended)
            wallKicks = new Point[]{
                    new Point(1, 0), new Point(-1, 0), new Point(2, 0), new Point(-2, 0),
                    new Point(0, -1), new Point(1, -1), new Point(-1, -1),
                    new Point(0, -2), new Point(1, -2), new Point(-1, -2),
                    new Point(0, 1), new Point(1, 1), new Point(-1, 1),
                    new Point(2, -1), new Point(-2, -1), new Point(2, 1), new Point(-2, 1)
            };
        } else {
            // Standard wall kick data for other bricks
            wallKicks = new Point[]{
                    new Point(1, 0), new Point(-1, 0), new Point(0, -1),
                    new Point(1, -1), new Point(-1, -1)
            };
        }

        // Try each wall kick
        for (Point kick : wallKicks) {
            Point testOffset = new Point(currentOffset);
            testOffset.translate(kick.x, kick.y);

            boolean kickConflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(),
                    (int) testOffset.getX(), (int) testOffset.getY());

            if (!kickConflict) {
                // Successful wall kick
                brickRotator.setCurrentShape(nextShape.getPosition());
                currentOffset = testOffset;
                return true;
            }
        }

        // All wall kicks failed
        return false;
    }

    /**
     * Checks if the current brick is an I-brick.
     * I-bricks need special wall kick handling due to its length.
     *
     * @return true if current brick is an I-brick, false otherwise
     */
    private boolean isIBrick() {
        int[][] shape = brickRotator.getCurrentShape();
        int nonZeroCount = 0;

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    nonZeroCount++;
                }
            }
        }

        // I-brick has exactly 4 blocks
        return nonZeroCount == 4;
    }

    /**
     * Creates and spawns a new brick at the top of the board.
     * Resets hold ability for the new piece.
     *
     * @return true if the new brick immediately collides (game over), false otherwise
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(3, 0); // Spawn point (top center)
        canHold = true; // Reset hold ability for new brick
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Gets the current game board matrix.
     *
     * @return 2D array representing the board state
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * Gathers all visual state information for the view layer.
     * Includes current brick, next brick preview, held brick, and ghost piece position.
     *
     * @return GameStateView containing complete rendering information
     */
    @Override
    public GameStateView getViewData() {
        // Get the first rotation (index 0) from each brick's shape matrix
        List<int[][]> heldShapes = heldBrick.getShapeMatrix();
        List<int[][]> nextShapes = brickGenerator.getNextBrick().getShapeMatrix();

        int[][] heldBrickData = heldShapes.get(0);
        int[][] nextBrickData = nextShapes.get(0);

        Point ghostPosition = calculateGhostPosition();
        return new GameStateView(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextBrickData,
                heldBrickData,
                (int) ghostPosition.getX(),
                (int) ghostPosition.getY()
        );
    }

    /**
     * Calculates where the current brick would land if dropped straight down.
     * Used to render the ghost piece preview showing landing position.
     *
     * @return Point representing the ghost piece position
     */
    private Point calculateGhostPosition() {
        if (brickRotator.getCurrentShape() == null) {
            return new Point(currentOffset);
        }

        Point ghostPos = new Point(currentOffset);
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        int[][] currentBrick = brickRotator.getCurrentShape();

        // Validate current position first
        if (MatrixOperations.intersect(currentMatrix, currentBrick,
                (int) ghostPos.getX(), (int) ghostPos.getY())) {
            return new Point(currentOffset); // Fallback to current position
        }

        // Move down until collision is detected
        while (true) {
            Point nextPos = new Point(ghostPos);
            nextPos.translate(0, 1);

            boolean willCollide = MatrixOperations.intersect(currentMatrix, currentBrick,
                    (int) nextPos.getX(), (int) nextPos.getY());

            if (willCollide) {
                break; // Found the landing position
            }

            ghostPos = nextPos; // Move to next position
        }

        return ghostPos;
    }

    // Permanently merges the current brick into the background matrix
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Checks for and removes any complete horizontal rows.
     *
     * @return ClearRow containing information about cleared lines and score bonus
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    // Returns the current game score
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Implements the hold mechanic: swaps current brick with held brick.
     * Can only be used once per brick (resets when new brick spawns).
     * If no brick is held, stores current and spawns a new one.
     *
     * @return The brick that was just stored in hold
     */
    @Override
    public Brick holdBrick() {
        if (!canHold) {
            return NullBrick.getInstance(); // Cannot hold again until new brick
        }
        Brick currentBrick = brickRotator.getBrick();
        Brick temp = heldBrick;
        heldBrick = currentBrick;
        canHold = false; // Disable further holds until new brick

        if (!(temp instanceof NullBrick)) {
            // Swap with previously held brick
            brickRotator.setBrick(temp);
        } else {
            // First time holding, create new brick
            Brick newBrick = brickGenerator.getBrick();
            brickRotator.setBrick(newBrick);
        }
        currentOffset = new Point(3, 0); // Reset position to spawn point
        return heldBrick;
    }

    // Resets game to initial state. Clears board,  resets score, clears held brick, and spawns new first brick.
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = NullBrick.getInstance();
        canHold = true;
        createNewBrick();
    }
}
