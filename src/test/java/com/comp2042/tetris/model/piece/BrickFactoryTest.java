package com.comp2042.tetris.model.piece;

import com.comp2042.tetris.model.piece.BrickFactory.BrickType;
import com.comp2042.tetris.model.piece.types.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Brick Factory Tests")
class BrickFactoryTest {

    @Test
    @DisplayName("Should create I-Brick")
    void testCreateIBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.I);
        assertInstanceOf(IBrick.class, brick);
    }

    @Test
    @DisplayName("Should create J-Brick")
    void testCreateJBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.J);
        assertInstanceOf(JBrick.class, brick);
    }

    @Test
    @DisplayName("Should create L-Brick")
    void testCreateLBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.L);
        assertInstanceOf(LBrick.class, brick);
    }

    @Test
    @DisplayName("Should create O-Brick")
    void testCreateOBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.O);
        assertInstanceOf(OBrick.class, brick);
    }

    @Test
    @DisplayName("Should create S-Brick")
    void testCreateSBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.S);
        assertInstanceOf(SBrick.class, brick);
    }

    @Test
    @DisplayName("Should create T-Brick")
    void testCreateTBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.T);
        assertInstanceOf(TBrick.class, brick);
    }

    @Test
    @DisplayName("Should create Z-Brick")
    void testCreateZBrick() {
        Brick brick = BrickFactory.createBrick(BrickType.Z);
        assertInstanceOf(ZBrick.class, brick);
    }

    @ParameterizedTest
    @EnumSource(BrickType.class)
    @DisplayName("Should create non-null bricks for all types")
    void testCreateNonNullBricks(BrickType type) {
        Brick brick = BrickFactory.createBrick(type);
        assertNotNull(brick);
    }

    @ParameterizedTest
    @EnumSource(BrickType.class)
    @DisplayName("All created bricks should have valid shapes")
    void testCreatedBricksHaveValidShapes(BrickType type) {
        Brick brick = BrickFactory.createBrick(type);
        List<int[][]> shapes = brick.getShapeMatrix();

        assertFalse(shapes.isEmpty());

        for (int[][] shape : shapes) {
            assertEquals(4, shape.length);
            for (int[] row : shape) {
                assertEquals(4, row.length);
            }
        }
    }

    @Test
    @DisplayName("Should create all 7 brick types")
    void testCreateAllBricks() {
        List<Brick> allBricks = BrickFactory.createAllBricks();

        assertEquals(7, allBricks.size(), "Should create exactly 7 brick types");
    }

    @Test
    @DisplayName("createAllBricks should contain one of each type")
    void testCreateAllBricksContainsAllTypes() {
        List<Brick> allBricks = BrickFactory.createAllBricks();

        boolean hasI = false, hasJ = false, hasL = false, hasO = false;
        boolean hasS = false, hasT = false, hasZ = false;

        for (Brick brick : allBricks) {
            if (brick instanceof IBrick) hasI = true;
            else if (brick instanceof JBrick) hasJ = true;
            else if (brick instanceof LBrick) hasL = true;
            else if (brick instanceof OBrick) hasO = true;
            else if (brick instanceof SBrick) hasS = true;
            else if (brick instanceof TBrick) hasT = true;
            else if (brick instanceof ZBrick) hasZ = true;
        }

        assertTrue(hasI && hasJ && hasL && hasO && hasS && hasT && hasZ,
                "Should contain all 7 brick types");
    }

    @Test
    @DisplayName("Each call to createAllBricks should create new instances")
    void testCreateAllBricksCreatesNewInstances() {
        List<Brick> bricks1 = BrickFactory.createAllBricks();
        List<Brick> bricks2 = BrickFactory.createAllBricks();

        assertNotSame(bricks1, bricks2);

        for (int i = 0; i < bricks1.size(); i++) {
            assertNotSame(bricks1.get(i), bricks2.get(i),
                    "Should create new brick instances");
        }
    }

    @Test
    @DisplayName("Factory should create independent brick instances")
    void testFactoryCreatesIndependentInstances() {
        Brick brick1 = BrickFactory.createBrick(BrickType.T);
        Brick brick2 = BrickFactory.createBrick(BrickType.T);

        assertNotSame(brick1, brick2, "Should create new instances each time");
    }

    @Test
    @DisplayName("BrickType enum should have 7 values")
    void testBrickTypeEnumCount() {
        BrickType[] types = BrickType.values();
        assertEquals(7, types.length, "Should have exactly 7 brick types");
    }
}