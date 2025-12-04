package com.comp2042.tetris.view;

import com.comp2042.tetris.dto.GameStateView;
import com.comp2042.tetris.util.ColorMapper;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

// Handles rendering of tetris pieces on the game board
// Manages the visual display of the current brick, ghost brick, hold brick, and next brick preview
public class BrickRenderer {
    private static final int BRICK_SIZE = 20;
    private static final int BRICK_PANEL_Y_OFFSET_INIT = -42;
    private static final int BRICK_PANEL_Y_OFFSET_REFRESH = -45;
    private static final int GHOST_PANEL_X_OFFSET = -1;
    private static final int GHOST_PANEL_Y_OFFSET = -48;

    private static final int ARC_SIZE = 9;

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane holdPanel;
    private final GridPane nextBrickPanel;
    private GridPane ghostPanel;

    private Rectangle[][] rectangles;
    private Rectangle[][] holdRectangles;
    private Rectangle[][] ghostRectangles;

    // Set appearance of rectangle based on brick color type
    private void setRectangleAppearance(int colorCode, Rectangle rectangle) {
        rectangle.setFill(ColorMapper.getBrickColor(colorCode));
        rectangle.setArcHeight(ARC_SIZE);
        rectangle.setArcWidth(ARC_SIZE);
    }

    // Constructor: initializes panels for rendering bricks
    public BrickRenderer(GridPane gamePanel, GridPane brickPanel,
                         GridPane holdPanel, GridPane nextBrickPanel) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.holdPanel = holdPanel;
        this.nextBrickPanel = nextBrickPanel;
    }

    // Initialize current brick display
    public void initBrick(GameStateView brick) {
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorMapper.getBrickColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getXPosition() * brickPanel.getVgap() + brick.getXPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(BRICK_PANEL_Y_OFFSET_INIT + gamePanel.getLayoutY() + brick.getYPosition() * brickPanel.getHgap() + brick.getYPosition() * BRICK_SIZE);
    }

    // Refreshes current brick position and appearance
    public void refreshBrick(GameStateView brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getXPosition() * brickPanel.getVgap() + brick.getXPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(BRICK_PANEL_Y_OFFSET_REFRESH + gamePanel.getLayoutY() + brick.getYPosition() * brickPanel.getHgap() + brick.getYPosition() * BRICK_SIZE);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleAppearance(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }
    }

    // Initialize ghost piece display
    public void initGhostPanel(GameStateView brick) {
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(javafx.scene.paint.Color.TRANSPARENT);
                ghostRectangles[i][j] = rectangle;

                if (ghostPanel == null) {
                    ghostPanel = new GridPane();
                    ghostPanel.setVgap(1);
                    ghostPanel.setHgap(1);
                    ((Pane) gamePanel.getParent()).getChildren().add(0, ghostPanel);
                }
                ghostPanel.add(rectangle, j, i);
            }
        }
    }

    // Refresh ghost piece position and appearance
    // Updates ghost piece to show where the current brick will land
    public void refreshGhostPiece(GameStateView brick) {
        if (ghostPanel == null || brick == null) {
            return;
        }

        ghostPanel.setLayoutX(GHOST_PANEL_X_OFFSET + gamePanel.getLayoutX() +
                brick.getGhostXPosition() * ghostPanel.getVgap() + brick.getGhostXPosition() * BRICK_SIZE);
        ghostPanel.setLayoutY(GHOST_PANEL_Y_OFFSET + gamePanel.getLayoutY() +
                brick.getGhostYPosition() * ghostPanel.getHgap() + brick.getGhostYPosition() * BRICK_SIZE);

        int[][] brickData = brick.getBrickData();
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (i < ghostRectangles.length && j < ghostRectangles[i].length) {
                    ghostRectangles[i][j].setFill(ColorMapper.getGhostColor(brickData[i][j]));
                    ghostRectangles[i][j].setArcHeight(ARC_SIZE);
                    ghostRectangles[i][j].setArcWidth(ARC_SIZE);

                    if (brickData[i][j] != 0) {
                        ghostRectangles[i][j].setStroke(ColorMapper.getGhostStrokeColor());
                        ghostRectangles[i][j].setStrokeWidth(ColorMapper.getGhostStrokeWidth());
                    } else {
                        ghostRectangles[i][j].setStroke(null);
                    }
                }
            }
        }
    }

    // Initialize hold panel display
    // Creates a 4x4 grid for displaying the held brick
    public void initHoldPanel() {
        holdRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(javafx.scene.paint.Color.TRANSPARENT);
                holdRectangles[i][j] = rectangle;
                holdPanel.add(rectangle, j, i);
            }
        }
    }

    // Update hold panel with held brick
    public void refreshHoldPanel(int[][] heldBrick) {
        for (int i = 0; i < heldBrick.length; i++) {
            for (int j = 0; j < heldBrick[i].length; j++) {
                setRectangleAppearance(heldBrick[i][j], holdRectangles[i][j]);
            }
        }
    }

    // Update next brick preview display
    public void updateNextBrick(int[][] nextBrickData) {
        nextBrickPanel.getChildren().clear();

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(ColorMapper.getBrickColor(nextBrickData[i][j]));
                rectangle.setArcHeight(ARC_SIZE);
                rectangle.setArcWidth(ARC_SIZE);
                nextBrickPanel.add(rectangle, j, i);
            }
        }
    }
}
