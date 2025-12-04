package com.comp2042.tetris.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ColorMapper {
    private static final Color TRANSPARENT = Color.TRANSPARENT;
    private static final Color I_BRICK_COLOR = Color.AQUA;
    private static final Color J_BRICK_COLOR = Color.BLUEVIOLET;
    private static final Color L_BRICK_COLOR = Color.DARKGREEN;
    private static final Color O_BRICK_COLOR = Color.YELLOW;
    private static final Color S_BRICK_COLOR = Color.RED;
    private static final Color T_BRICK_COLOR = Color.BEIGE;
    private static final Color Z_BRICK_COLOR = Color.BURLYWOOD;
    private static final Color DEFAULT_COLOR = Color.WHITE;

    private static final double GHOST_OPACITY = 0.2;
    private static final int GHOST_STROKE_R = 180;
    private static final int GHOST_STROKE_G = 180;
    private static final int GHOST_STROKE_B = 180;
    private static final double GHOST_STROKE_OPACITY = 0.6;
    private static final double GHOST_STROKE_WIDTH = 1.5;

    // Gets the fill color for a brick type
    public static Paint getBrickColor(int brickType) {
        return switch (brickType) {
            case 0 -> TRANSPARENT;
            case 1 -> I_BRICK_COLOR;
            case 2 -> J_BRICK_COLOR;
            case 3 -> L_BRICK_COLOR;
            case 4 -> O_BRICK_COLOR;
            case 5 -> S_BRICK_COLOR;
            case 6 -> T_BRICK_COLOR;
            case 7 -> Z_BRICK_COLOR;
            default -> DEFAULT_COLOR;
        };
    }

    // Gets the ghost color for a brick type
    public static Paint getGhostColor(int brickType) {
        if (brickType == 0) {
            return TRANSPARENT;
        }
        return Color.rgb(255, 255, 255, GHOST_OPACITY);
    }

    // Gets the ghost stroke color
    public static Color getGhostStrokeColor() {
        return Color.rgb(GHOST_STROKE_R, GHOST_STROKE_G, GHOST_STROKE_B, GHOST_STROKE_OPACITY);
    }

    // Gets the ghost stroke width
    public static double getGhostStrokeWidth() {
        return GHOST_STROKE_WIDTH;
    }
}
