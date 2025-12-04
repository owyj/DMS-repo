package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.dto.NextBrickInfo;
import com.comp2042.tetris.model.piece.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Brick Rotator Tests")
class BrickRotatorTest {

    private BrickRotator rotator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
    }

    @Test
    @DisplayName("Should throw exception when setting null brick")
    void testSetNullBrick() {
        assertThrows(IllegalArgumentException.class, () -> rotator.setBrick(null));
    }

    @Test
    @DisplayName("Should set brick and reset to first shape")
    void testSetBrick() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);

        assertNotNull(rotator.getCurrentShape());
        assertEquals(brick.getShapeMatrix().get(0).length, rotator.getCurrentShape().length);
    }

    @Test
    @DisplayName("Should get current brick reference")
    void testGetBrick() {
        Brick brick = new JBrick();
        rotator.setBrick(brick);

        assertSame(brick, rotator.getBrick());
    }

    @Test
    @DisplayName("Should start at shape index 0")
    void testInitialShapeIndex() {
        Brick brick = new TBrick();
        rotator.setBrick(brick);

        int[][] firstShape = brick.getShapeMatrix().get(0);
        assertArrayEquals(firstShape[0], rotator.getCurrentShape()[0]);
    }

    @Test
    @DisplayName("Should set current shape to valid index")
    void testSetCurrentShape() {
        Brick brick = new JBrick();
        rotator.setBrick(brick);

        rotator.setCurrentShape(2);

        int[][] expectedShape = brick.getShapeMatrix().get(2);
        assertArrayEquals(expectedShape[0], rotator.getCurrentShape()[0]);
    }

    @Test
    @DisplayName("Should throw exception for negative shape index")
    void testSetNegativeShapeIndex() {
        Brick brick = new IBrick();
        rotator.setBrick(brick);

        assertThrows(IllegalArgumentException.class, () -> rotator.setCurrentShape(-1));
    }

    @Test
    @DisplayName("Should throw exception for shape index out of bounds")
    void testSetShapeIndexOutOfBounds() {
        Brick brick = new IBrick(); // has 2 rotations
        rotator.setBrick(brick);

        assertThrows(IllegalArgumentException.class, () -> rotator.setCurrentShape(5));
    }

    @Test
    @DisplayName("Should peek next shape without changing current")
    void testPeekNextShape() {
        Brick brick = new TBrick(); // has 4 rotations
        rotator.setBrick(brick);

        int[][] currentBefore = rotator.getCurrentShape();
        NextBrickInfo nextInfo = rotator.peekNextShape();
        int[][] currentAfter = rotator.getCurrentShape();

        assertArrayEquals(currentBefore[0], currentAfter[0],
                "Current shape should not change");
        assertEquals(1, nextInfo.getPosition());
    }

    @Test
    @DisplayName("Should peek next shape correctly for I-Brick")
    void testPeekNextShapeIBrick() {
        Brick brick = new IBrick(); // has 2 rotations
        rotator.setBrick(brick);
        rotator.setCurrentShape(0);

        NextBrickInfo nextInfo = rotator.peekNextShape();

        assertEquals(1, nextInfo.getPosition());
        assertNotNull(nextInfo.getShape());
    }

    @Test
    @DisplayName("Should wrap around when peeking next shape")
    void testPeekNextShapeWraparound() {
        Brick brick = new IBrick(); // has 2 rotations (0, 1)
        rotator.setBrick(brick);
        rotator.setCurrentShape(1); // Last rotation

        NextBrickInfo nextInfo = rotator.peekNextShape();

        assertEquals(0, nextInfo.getPosition(), "Should wrap to index 0");
    }

    @Test
    @DisplayName("Should handle O-Brick with single rotation")
    void testOBrickSingleRotation() {
        Brick brick = new OBrick(); // has 1 rotation
        rotator.setBrick(brick);

        NextBrickInfo nextInfo = rotator.peekNextShape();

        assertEquals(0, nextInfo.getPosition(), "Should wrap to same shape");
    }

    @Test
    @DisplayName("Should rotate through all T-Brick shapes")
    void testRotateThroughAllShapes() {
        Brick brick = new TBrick(); // has 4 rotations
        rotator.setBrick(brick);

        for (int i = 0; i < 4; i++) {
            NextBrickInfo nextInfo = rotator.peekNextShape();
            int expectedNext = (i + 1) % 4;
            assertEquals(expectedNext, nextInfo.getPosition());

            rotator.setCurrentShape(nextInfo.getPosition());
        }
    }

    @Test
    @DisplayName("Should return different shape instances for peek")
    void testPeekReturnsNewInstance() {
        Brick brick = new JBrick();
        rotator.setBrick(brick);

        NextBrickInfo info1 = rotator.peekNextShape();
        NextBrickInfo info2 = rotator.peekNextShape();

        assertNotSame(info1.getShape(), info2.getShape(),
                "Should return new array instances");
    }

    @Test
    @DisplayName("Should handle switching between different bricks")
    void testSwitchingBricks() {
        Brick iBrick = new IBrick();
        Brick tBrick = new TBrick();

        rotator.setBrick(iBrick);
        rotator.setCurrentShape(1);

        rotator.setBrick(tBrick);

        // Should reset to first shape of new brick
        int[][] expectedShape = tBrick.getShapeMatrix().get(0);
        assertArrayEquals(expectedShape[0], rotator.getCurrentShape()[0]);
    }

    @Test
    @DisplayName("Should get correct shape after multiple rotations")
    void testMultipleRotations() {
        Brick brick = new LBrick(); // has 4 rotations
        rotator.setBrick(brick);

        // Rotate to shape 2
        rotator.setCurrentShape(2);

        int[][] expected = brick.getShapeMatrix().get(2);
        assertArrayEquals(expected[0], rotator.getCurrentShape()[0]);
    }
}