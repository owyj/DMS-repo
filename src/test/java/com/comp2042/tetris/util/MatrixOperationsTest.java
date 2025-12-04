package com.comp2042.tetris.util;

import com.comp2042.tetris.dto.ClearRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Matrix Operations Tests")
class MatrixOperationsTest {

    @Test
    @DisplayName("Should detect intersection when brick overlaps filled cell")
    void testIntersectWithFilledCell() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1},
                {0, 0}
        };

        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 2));
    }

    @Test
    @DisplayName("Should not detect intersection when brick is in empty space")
    void testNoIntersectInEmptySpace() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        assertFalse(MatrixOperations.intersect(matrix, brick, 1, 1));
    }

    @Test
    @DisplayName("Should detect intersection when brick is out of bounds (left)")
    void testIntersectOutOfBoundsLeft() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1}
        };

        assertTrue(MatrixOperations.intersect(matrix, brick, -1, 0));
    }

    @Test
    @DisplayName("Should detect intersection when brick is out of bounds (right)")
    void testIntersectOutOfBoundsRight() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1}
        };

        assertTrue(MatrixOperations.intersect(matrix, brick, 4, 0));
    }

    @Test
    @DisplayName("Should detect intersection when brick is out of bounds (bottom)")
    void testIntersectOutOfBoundsBottom() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1},
                {1}
        };

        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 2));
    }

    @Test
    @DisplayName("Should copy matrix correctly")
    void testCopyMatrix() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] copy = MatrixOperations.copy(original);

        assertNotSame(original, copy);
        assertArrayEquals(original[0], copy[0]);
        assertArrayEquals(original[1], copy[1]);
        assertArrayEquals(original[2], copy[2]);

        // Modify copy and ensure original is unchanged
        copy[1][1] = 99;
        assertEquals(5, original[1][1]);
        assertEquals(99, copy[1][1]);
    }

    @Test
    @DisplayName("Should merge brick into matrix at specified position")
    void testMergeBrickIntoMatrix() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {0, 1, 0},
                {1, 1, 1}
        };

        int[][] result = MatrixOperations.merge(matrix, brick, 1, 1);

        assertEquals(0, result[0][0]);
        assertEquals(1, result[1][2]);
        assertEquals(1, result[2][1]);
        assertEquals(1, result[2][2]);
        assertEquals(1, result[2][3]);
    }

    @Test
    @DisplayName("Should not clear rows when no complete rows exist")
    void testCheckRemovingNoCompleteRows() {
        int[][] matrix = {
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {0, 1, 1, 1}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
    }

    @Test
    @DisplayName("Should clear single complete row and award 100 points")
    void testCheckRemovingSingleRow() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(1, result.getLinesRemoved());
        assertEquals(100, result.getScoreBonus());

        int[][] newMatrix = result.getNewMatrix();
        assertEquals(0, newMatrix[2][0]);
        assertEquals(0, newMatrix[2][1]);
    }

    @Test
    @DisplayName("Should clear double rows and award 300 points")
    void testCheckRemovingDoubleRows() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(2, result.getLinesRemoved());
        assertEquals(300, result.getScoreBonus());
    }

    @Test
    @DisplayName("Should clear triple rows and award 500 points")
    void testCheckRemovingTripleRows() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(3, result.getLinesRemoved());
        assertEquals(500, result.getScoreBonus());
    }

    @Test
    @DisplayName("Should clear tetris (4 rows) and award 800 points")
    void testCheckRemovingTetris() {
        int[][] matrix = {
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus());
    }

    @Test
    @DisplayName("Should deep copy list of matrices")
    void testDeepCopyList() {
        int[][] matrix1 = {{1, 2}, {3, 4}};
        int[][] matrix2 = {{5, 6}, {7, 8}};
        List<int[][]> original = List.of(matrix1, matrix2);

        List<int[][]> copy = MatrixOperations.deepCopyList(original);

        assertEquals(original.size(), copy.size());
        assertNotSame(original.get(0), copy.get(0));
        assertArrayEquals(original.get(0)[0], copy.get(0)[0]);

        // Modify copy and ensure original is unchanged
        copy.get(0)[0][0] = 99;
        assertEquals(1, original.get(0)[0][0]);
        assertEquals(99, copy.get(0)[0][0]);
    }

    @Test
    @DisplayName("Should handle rows that are partially filled correctly")
    void testCheckRemovingPartialRows() {
        int[][] matrix = {
                {1, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 1, 1},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(2, result.getLinesRemoved());

        int[][] newMatrix = result.getNewMatrix();
        // The partial row should remain
        assertEquals(1, newMatrix[2][0]);
        assertEquals(0, newMatrix[2][1]);
        assertEquals(1, newMatrix[2][2]);
    }
}