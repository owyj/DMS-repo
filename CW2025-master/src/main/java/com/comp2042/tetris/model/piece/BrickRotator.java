package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.dto.NextBrickInfo;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        if (currentShape < 0) {
            throw new IllegalArgumentException("Shape index cannot be negative");
        }
        if (brick != null && currentShape >= brick.getShapeMatrix().size()) {
            throw new IllegalArgumentException("Shape index out of bounds: " + currentShape);
        }
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        if (brick == null) {
            throw new IllegalArgumentException("Brick cannot be null");
        }
        this.brick = brick;
        this.currentShape = 0;
    }

    public Brick getBrick() {
        return brick;
    }

    public NextBrickInfo peekNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextBrickInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }
}
