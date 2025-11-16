package com.comp2042.ui;

import com.comp2042.logic.bricks.BrickRotator;
import com.comp2042.logic.bricks.ClearRow;
import com.comp2042.game.logic.Board;
import com.comp2042.game.logic.MatrixOperations;
import com.comp2042.game.logic.NextShapeInfo;
import com.comp2042.game.logic.Score;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick = null;
    private boolean canHold = true;

    public SimpleBoard(int width, int height) {
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
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
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
    public ViewData getViewData() {
        int[][] heldBrickData = heldBrick != null ? heldBrick.getShapeMatrix().get(0) : new int[4][4];
        Point ghostPosition = calculateGhostPosition();
        return new ViewData(
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
    public Brick getHeldBrick() {
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
