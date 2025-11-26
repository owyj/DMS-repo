package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.model.piece.types.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Brick Tests")
class BrickTest {

    @Test
    @DisplayName("I-Brick should have 2 rotations")
    void testIBrickRotations() {
        Brick brick = new IBrick();
        assertEquals(2, brick.getShapeMatrix().size());
    }

    @Test
    @DisplayName("J-Brick should have 4 rotations")
    void testJBrickRotations() {
        Brick brick = new JBrick();
        assertEquals(4, brick.getShapeMatrix().size());
    }

    @Test
    @DisplayName("L-Brick should have 4 rotations")
    void testLBrickRotations() {
        Brick brick = new LBrick();
        assertEquals(4, brick.getShapeMatrix().size());
    }

    @Test
    @DisplayName("O-Brick should have 1 rotation")
    void testOBrickRotations() {
        Brick brick = new OBrick();
        assertEquals(1, brick.getShapeMatrix().size());
    }

    @Test
    @DisplayName("S-Brick should have 2 rotations")
    void testSBrickRotations() {
        Brick brick = new SBrick();
        assertEquals(2, brick.getShapeMatrix().size());
    }

    @Test
    @DisplayName("T-Brick should have 4 rotations")
    void testTBrickRotations() {
        Brick brick = new TBrick();
        assertEquals(4, brick.getShapeMatrix().size());
    }

    @Test
    @DisplayName("Z-Brick should have 2 rotations")
    void testZBrickRotations() {
        Brick brick = new ZBrick();
        assertEquals(2, brick.getShapeMatrix().size());
    }

    @Test
    @DisplayName("NullBrick should have 1 empty shape")
    void testNullBrickShape() {
        Brick brick = NullBrick.getInstance();
        assertEquals(1, brick.getShapeMatrix().size());

        int[][] shape = brick.getShapeMatrix().get(0);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                assertEquals(0, shape[i][j], "NullBrick should be all zeros");
            }
        }
    }

    @Test
    @DisplayName("NullBrick should be singleton")
    void testNullBrickSingleton() {
        Brick brick1 = NullBrick.getInstance();
        Brick brick2 = NullBrick.getInstance();
        assertSame(brick1, brick2);
    }

    @ParameterizedTest
    @MethodSource("provideBricks")
    @DisplayName("All bricks should return copies of shape matrices")
    void testBricksReturnCopies(Brick brick) {
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1.get(0), shapes2.get(0),
                "Should return different array instances");
    }

    @ParameterizedTest
    @MethodSource("provideBricks")
    @DisplayName("All brick shapes should be 4x4 matrices")
    void testBrickShapesDimensions(Brick brick) {
        List<int[][]> shapes = brick.getShapeMatrix();

        for (int[][] shape : shapes) {
            assertEquals(4, shape.length, "Shape should have 4 rows");
            for (int[] row : shape) {
                assertEquals(4, row.length, "Each row should have 4 columns");
            }
        }
    }

    @Test
    @DisplayName("I-Brick should have correct color code (1)")
    void testIBrickColorCode() {
        Brick brick = new IBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasColorCode1 = false;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    hasColorCode1 = true;
                    break;
                }
            }
        }
        assertTrue(hasColorCode1, "I-Brick should have color code 1");
    }

    @Test
    @DisplayName("Each brick type should have exactly 4 filled cells")
    void testIBrickHasFourBlocks() {
        Brick brick = new IBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        int blockCount = 0;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    blockCount++;
                }
            }
        }
        assertEquals(4, blockCount, "Each tetromino should have 4 blocks");
    }

    @ParameterizedTest
    @MethodSource("provideColorCodes")
    @DisplayName("Each brick type should use unique color code")
    void testBrickColorCodes(Brick brick, int expectedColorCode) {
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasExpectedCode = false;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == expectedColorCode) {
                    hasExpectedCode = true;
                    break;
                }
            }
        }
        assertTrue(hasExpectedCode, "Brick should have color code " + expectedColorCode);
    }

    @Test
    @DisplayName("I-Brick horizontal and vertical orientations should be different")
    void testIBrickOrientations() {
        Brick brick = new IBrick();
        int[][] horizontal = brick.getShapeMatrix().get(0);
        int[][] vertical = brick.getShapeMatrix().get(1);

        assertFalse(areMatricesEqual(horizontal, vertical),
                "Two orientations should be different");
    }

    @Test
    @DisplayName("O-Brick should be same in all rotations")
    void testOBrickSymmetry() {
        Brick brick = new OBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        // O-brick should have blocks in 2x2 formation
        int blockCount = 0;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) blockCount++;
            }
        }
        assertEquals(4, blockCount);
    }

    private boolean areMatricesEqual(int[][] matrix1, int[][] matrix2) {
        if (matrix1.length != matrix2.length) return false;

        for (int i = 0; i < matrix1.length; i++) {
            if (matrix1[i].length != matrix2[i].length) return false;
            for (int j = 0; j < matrix1[i].length; j++) {
                if (matrix1[i][j] != matrix2[i][j]) return false;
            }
        }
        return true;
    }

    private static Stream<Arguments> provideBricks() {
        return Stream.of(
                Arguments.of(new IBrick()),
                Arguments.of(new JBrick()),
                Arguments.of(new LBrick()),
                Arguments.of(new OBrick()),
                Arguments.of(new SBrick()),
                Arguments.of(new TBrick()),
                Arguments.of(new ZBrick())
        );
    }

    private static Stream<Arguments> provideColorCodes() {
        return Stream.of(
                Arguments.of(new IBrick(), 1),
                Arguments.of(new JBrick(), 2),
                Arguments.of(new LBrick(), 3),
                Arguments.of(new OBrick(), 4),
                Arguments.of(new SBrick(), 5),
                Arguments.of(new TBrick(), 6),
                Arguments.of(new ZBrick(), 7)
        );
    }
}