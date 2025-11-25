package com.comp2042.tetris.model;

import com.comp2042.tetris.model.piece.BrickRotator;
import com.comp2042.tetris.dto.ClearRow;
import com.comp2042.tetris.util.MatrixOperations;
import com.comp2042.tetris.dto.NextBrickInfo;
import com.comp2042.tetris.model.piece.Brick;
import com.comp2042.tetris.model.piece.BrickGenerator;
import com.comp2042.tetris.model.piece.RandomBrickGenerator;
import com.comp2042.tetris.dto.GameStateView;

import java.awt.*;

public class GameBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick = null;
    private boolean canHold = true;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

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
            wallKicks = new Point[]{
                    new Point(1, 0), new Point(-1, 0), new Point(2, 0), new Point(-2, 0),
                    new Point(0, -1), new Point(1, -1), new Point(-1, -1),
                    new Point(0, -2), new Point(1, -2), new Point(-1, -2),
                    new Point(0, 1), new Point(1, 1), new Point(-1, 1),
                    new Point(2, -1), new Point(-2, -1), new Point(2, 1), new Point(-2, 1)
            };
        } else {
            wallKicks = new Point[]{
                    new Point(1, 0), new Point(-1, 0), new Point(0, -1),
                    new Point(1, -1), new Point(-1, -1)
            };
        }

        for (Point kick : wallKicks) {
            Point testOffset = new Point(currentOffset);
            testOffset.translate(kick.x, kick.y);

            boolean kickConflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(),
                    (int) testOffset.getX(), (int) testOffset.getY());

            if (!kickConflict) {
                brickRotator.setCurrentShape(nextShape.getPosition());
                currentOffset = testOffset;
                return true;
            }
        }

        return false;
    }

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

        return nonZeroCount == 4; // I-brick has exactly 4 blocks
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0);
        canHold = true; //reset hold ability for new brick
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public GameStateView getViewData() {
        int[][] heldBrickData = heldBrick != null ? heldBrick.getShapeMatrix().get(0) : new int[4][4];
        Point ghostPosition = calculateGhostPosition();
        return new GameStateView(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                heldBrickData,
                (int) ghostPosition.getX(),
                (int) ghostPosition.getY()
        );
    }

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

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public Brick holdBrick() {
        if (!canHold) {
            return null; //cannot hold again until new brick
        }
        Brick currentBrick = brickRotator.getBrick();
        Brick temp = heldBrick;
        heldBrick = currentBrick;
        canHold = false; //disable further holds until new brick
        if (temp != null) {
            //swap with held brick
            brickRotator.setBrick(temp);
        } else {
            //first time holding, create new brick
            Brick newBrick = brickGenerator.getBrick();
            brickRotator.setBrick(newBrick);
        }
        currentOffset = new Point(4, 0);
        return heldBrick;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null; //clear held brick
        canHold = true;
        createNewBrick();
    }
}
