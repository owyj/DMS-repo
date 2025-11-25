package com.comp2042.tetris.view;

import com.comp2042.tetris.util.ColorMapper;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardRenderer {
    private static final int BRICK_SIZE = 20;
    private static final int ARC_SIZE = 9;
    private static final int SKIP_TOP_ROWS = 2;

    private final GridPane gamePanel;
    private Rectangle[][] displayMatrix;

    public BoardRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
    }

    // initialize the game board display
    public void initBoard(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = SKIP_TOP_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - SKIP_TOP_ROWS);
            }
        }
    }

    // set appearance of rectangle
    private void setRectangleAppearance(int colorCode, Rectangle rectangle) {
        rectangle.setFill(ColorMapper.getBrickColor(colorCode));
        rectangle.setArcHeight(ARC_SIZE);
        rectangle.setArcWidth(ARC_SIZE);
    }

    // refresh game board display
    public void refreshBoard(int[][] board) {
        for (int i = SKIP_TOP_ROWS; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleAppearance(board[i][j], displayMatrix[i][j]);
            }
        }
    }

}
