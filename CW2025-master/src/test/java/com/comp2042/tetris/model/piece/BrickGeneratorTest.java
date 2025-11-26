package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.model.piece.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Brick Generator Tests")
class BrickGeneratorTest {

    private BrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    @Test
    @DisplayName("Should return non-null brick")
    void testGetBrickNotNull() {
        Brick brick = generator.getBrick();
        assertNotNull(brick);
    }

    @Test
    @DisplayName("Should return non-null next brick")
    void testGetNextBrickNotNull() {
        Brick nextBrick = generator.getNextBrick();
        assertNotNull(nextBrick);
    }

    @Test
    @DisplayName("Should return valid brick types")
    void testGetBrickReturnsValidType() {
        Brick brick = generator.getBrick();

        boolean isValidType = brick instanceof IBrick ||
                brick instanceof JBrick ||
                brick instanceof LBrick ||
                brick instanceof OBrick ||
                brick instanceof SBrick ||
                brick instanceof TBrick ||
                brick instanceof ZBrick;

        assertTrue(isValidType, "Should return one of the 7 Tetris brick types");
    }

    @Test
    @DisplayName("Should implement 7-bag randomizer")
    void testSevenBagRandomizer() {
        Set<String> brickTypes = new HashSet<>();

        // Get 7 bricks - should contain all 7 types
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass().getSimpleName());
        }

        assertEquals(7, brickTypes.size(),
                "First 7 bricks should contain all 7 types (7-bag system)");
    }

    @Test
    @DisplayName("Should generate all brick types within 14 pieces")
    void testAllTypesGenerated() {
        Set<Class<?>> brickClasses = new HashSet<>();

        // Within 14 pieces (2 bags), we should see all 7 types
        for (int i = 0; i < 14; i++) {
            Brick brick = generator.getBrick();
            brickClasses.add(brick.getClass());
        }

        assertTrue(brickClasses.contains(IBrick.class));
        assertTrue(brickClasses.contains(JBrick.class));
        assertTrue(brickClasses.contains(LBrick.class));
        assertTrue(brickClasses.contains(OBrick.class));
        assertTrue(brickClasses.contains(SBrick.class));
        assertTrue(brickClasses.contains(TBrick.class));
        assertTrue(brickClasses.contains(ZBrick.class));
    }

    @Test
    @DisplayName("Next brick should become current brick after getBrick")
    void testNextBrickProgression() {
        Brick next = generator.getNextBrick();
        String nextType = next.getClass().getSimpleName();

        Brick current = generator.getBrick();
        String currentType = current.getClass().getSimpleName();

        assertEquals(nextType, currentType,
                "Next brick should become current after calling getBrick");
    }

    @Test
    @DisplayName("Should continuously generate bricks without errors")
    void testContinuousGeneration() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                Brick brick = generator.getBrick();
                assertNotNull(brick);
            }
        });
    }

    @Test
    @DisplayName("Should maintain queue of upcoming bricks")
    void testBrickQueue() {
        // Get next brick reference
        Brick next1 = generator.getNextBrick();

        // Get current brick (next1 should become current)
        Brick current = generator.getBrick();

        // Verify they're the same type
        assertEquals(next1.getClass(), current.getClass());
    }

    @Test
    @DisplayName("Generated bricks should have valid shape matrices")
    void testGeneratedBricksHaveValidShapes() {
        for (int i = 0; i < 20; i++) {
            Brick brick = generator.getBrick();
            List<int[][]> shapes = brick.getShapeMatrix();

            assertFalse(shapes.isEmpty(), "Brick should have at least one shape");

            for (int[][] shape : shapes) {
                assertEquals(4, shape.length, "Shape should be 4x4");
                for (int[] row : shape) {
                    assertEquals(4, row.length, "Each row should have 4 columns");
                }
            }
        }
    }

    @Test
    @DisplayName("Should not return null brick as next brick")
    void testNextBrickNeverNull() {
        for (int i = 0; i < 50; i++) {
            generator.getBrick();
            Brick nextBrick = generator.getNextBrick();
            assertNotNull(nextBrick);
            assertFalse(nextBrick instanceof NullBrick);
        }
    }

    @Test
    @DisplayName("Distribution should be fair over large sample")
    void testFairDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        int totalBricks = 700; // 100 complete bags

        for (int i = 0; i < totalBricks; i++) {
            Brick brick = generator.getBrick();
            String type = brick.getClass().getSimpleName();
            distribution.put(type, distribution.getOrDefault(type, 0) + 1);
        }

        // Each type should appear exactly 100 times
        assertEquals(7, distribution.size(), "All 7 types should appear");

        for (int count : distribution.values()) {
            assertEquals(100, count,
                    "Each brick type should appear equally in 7-bag system");
        }
    }

    @Test
    @DisplayName("Bag should refill automatically")
    void testBagRefill() {
        // Exhaust first bag
        for (int i = 0; i < 7; i++) {
            generator.getBrick();
        }

        // Should still be able to get more bricks
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 20; i++) {
                Brick brick = generator.getBrick();
                assertNotNull(brick);
            }
        });
    }
}