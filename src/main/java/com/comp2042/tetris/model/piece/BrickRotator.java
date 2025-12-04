package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.dto.NextBrickInfo;

/**
 * Manages brick rotation state for the current falling piece.
 * Each brick type has multiple rotation states (shapes) stored in a list.
 * This class tracks which rotation is currently active and provides methods to cycle through rotations.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    // Gets the matrix representation of the current rotation state
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    // Sets the current rotation state by a specific index
    public void setCurrentShape(int currentShape) {
        if (currentShape < 0) {
            throw new IllegalArgumentException("Shape index cannot be negative");
        }
        if (brick != null && currentShape >= brick.getShapeMatrix().size()) {
            throw new IllegalArgumentException("Shape index out of bounds: " + currentShape);
        }
        this.currentShape = currentShape;
    }

    // Sets a new brick to manage and resets rotation state
    public void setBrick(Brick brick) {
        if (brick == null) {
            throw new IllegalArgumentException("Brick cannot be null");
        }
        this.brick = brick;
        this.currentShape = 0;
    }

    // Gets the current brick being managed
    public Brick getBrick() {
        return brick;
    }

    // Previews the next rotation state without changing the current state
    public NextBrickInfo peekNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextBrickInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }
}
