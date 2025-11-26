package com.comp2042.tetris.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Color Mapper Tests")
class ColorMapperTest {

    @Test
    @DisplayName("Should return transparent for brick type 0")
    void testBrickType0() {
        Paint color = ColorMapper.getBrickColor(0);
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    @DisplayName("Should return aqua for I-brick (type 1)")
    void testIBrickColor() {
        Paint color = ColorMapper.getBrickColor(1);
        assertEquals(Color.AQUA, color);
    }

    @Test
    @DisplayName("Should return blueviolet for J-brick (type 2)")
    void testJBrickColor() {
        Paint color = ColorMapper.getBrickColor(2);
        assertEquals(Color.BLUEVIOLET, color);
    }

    @Test
    @DisplayName("Should return darkgreen for L-brick (type 3)")
    void testLBrickColor() {
        Paint color = ColorMapper.getBrickColor(3);
        assertEquals(Color.DARKGREEN, color);
    }

    @Test
    @DisplayName("Should return yellow for O-brick (type 4)")
    void testOBrickColor() {
        Paint color = ColorMapper.getBrickColor(4);
        assertEquals(Color.YELLOW, color);
    }

    @Test
    @DisplayName("Should return red for S-brick (type 5)")
    void testSBrickColor() {
        Paint color = ColorMapper.getBrickColor(5);
        assertEquals(Color.RED, color);
    }

    @Test
    @DisplayName("Should return beige for T-brick (type 6)")
    void testTBrickColor() {
        Paint color = ColorMapper.getBrickColor(6);
        assertEquals(Color.BEIGE, color);
    }

    @Test
    @DisplayName("Should return burlywood for Z-brick (type 7)")
    void testZBrickColor() {
        Paint color = ColorMapper.getBrickColor(7);
        assertEquals(Color.BURLYWOOD, color);
    }

    @Test
    @DisplayName("Should return white for unknown brick type")
    void testUnknownBrickType() {
        Paint color = ColorMapper.getBrickColor(99);
        assertEquals(Color.WHITE, color);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    @DisplayName("Should return non-null colors for all brick types")
    void testAllBrickTypesReturnNonNull(int brickType) {
        Paint color = ColorMapper.getBrickColor(brickType);
        assertNotNull(color);
    }

    @Test
    @DisplayName("Should return transparent ghost color for empty cell")
    void testGhostColorEmpty() {
        Paint ghostColor = ColorMapper.getGhostColor(0);
        assertEquals(Color.TRANSPARENT, ghostColor);
    }

    @Test
    @DisplayName("Should return semi-transparent white for ghost brick")
    void testGhostColorFilled() {
        Paint ghostColor = ColorMapper.getGhostColor(1);

        assertNotNull(ghostColor);
        assertTrue(ghostColor instanceof Color);

        Color color = (Color) ghostColor;
        assertEquals(1.0, color.getRed(), 0.01);
        assertEquals(1.0, color.getGreen(), 0.01);
        assertEquals(1.0, color.getBlue(), 0.01);
        assertEquals(0.2, color.getOpacity(), 0.01);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    @DisplayName("All non-zero brick types should have same ghost color")
    void testAllGhostColorsSame(int brickType) {
        Paint ghostColor = ColorMapper.getGhostColor(brickType);

        assertTrue(ghostColor instanceof Color);
        Color color = (Color) ghostColor;

        assertEquals(1.0, color.getRed(), 0.01);
        assertEquals(1.0, color.getGreen(), 0.01);
        assertEquals(1.0, color.getBlue(), 0.01);
        assertEquals(0.2, color.getOpacity(), 0.01);
    }

    @Test
    @DisplayName("Should return ghost stroke color")
    void testGhostStrokeColor() {
        Color strokeColor = ColorMapper.getGhostStrokeColor();

        assertNotNull(strokeColor);
        assertEquals(180, (int)(strokeColor.getRed() * 255));
        assertEquals(180, (int)(strokeColor.getGreen() * 255));
        assertEquals(180, (int)(strokeColor.getBlue() * 255));
        assertEquals(0.6, strokeColor.getOpacity(), 0.01);
    }

    @Test
    @DisplayName("Should return ghost stroke width")
    void testGhostStrokeWidth() {
        double strokeWidth = ColorMapper.getGhostStrokeWidth();
        assertEquals(1.5, strokeWidth, 0.01);
    }

    @Test
    @DisplayName("Each brick type should have unique color")
    void testUniqueBrickColors() {
        Paint color1 = ColorMapper.getBrickColor(1);
        Paint color2 = ColorMapper.getBrickColor(2);
        Paint color3 = ColorMapper.getBrickColor(3);
        Paint color4 = ColorMapper.getBrickColor(4);
        Paint color5 = ColorMapper.getBrickColor(5);
        Paint color6 = ColorMapper.getBrickColor(6);
        Paint color7 = ColorMapper.getBrickColor(7);

        assertNotEquals(color1, color2);
        assertNotEquals(color2, color3);
        assertNotEquals(color3, color4);
        assertNotEquals(color4, color5);
        assertNotEquals(color5, color6);
        assertNotEquals(color6, color7);
    }

    @Test
    @DisplayName("Negative brick types should return default color")
    void testNegativeBrickType() {
        Paint color = ColorMapper.getBrickColor(-1);
        assertEquals(Color.WHITE, color);
    }

    @Test
    @DisplayName("Large brick types should return default color")
    void testLargeBrickType() {
        Paint color = ColorMapper.getBrickColor(1000);
        assertEquals(Color.WHITE, color);
    }

    @Test
    @DisplayName("Ghost stroke width should be positive")
    void testGhostStrokeWidthPositive() {
        double strokeWidth = ColorMapper.getGhostStrokeWidth();
        assertTrue(strokeWidth > 0, "Stroke width should be positive");
    }

    @Test
    @DisplayName("Ghost stroke color should have valid RGB values")
    void testGhostStrokeColorValidRGB() {
        Color strokeColor = ColorMapper.getGhostStrokeColor();

        assertTrue(strokeColor.getRed() >= 0 && strokeColor.getRed() <= 1);
        assertTrue(strokeColor.getGreen() >= 0 && strokeColor.getGreen() <= 1);
        assertTrue(strokeColor.getBlue() >= 0 && strokeColor.getBlue() <= 1);
        assertTrue(strokeColor.getOpacity() >= 0 && strokeColor.getOpacity() <= 1);
    }
}
