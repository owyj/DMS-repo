package com.comp2042.tetris.dto;

import com.comp2042.tetris.input.EventSource;
import com.comp2042.tetris.input.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Tests")
class DTOTests {

    @Test
    @DisplayName("MoveEvent should store event type and source")
    void testMoveEvent() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

        assertEquals(EventType.DOWN, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    @DisplayName("MoveEvent should handle thread source")
    void testMoveEventThreadSource() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);

        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    @DisplayName("MoveEvent should handle all event types")
    void testMoveEventAllTypes() {
        for (EventType type : EventType.values()) {
            MoveEvent event = new MoveEvent(type, EventSource.USER);
            assertEquals(type, event.getEventType());
        }
    }

    @Test
    @DisplayName("ClearRow should store lines removed and score bonus")
    void testClearRow() {
        int[][] matrix = {{0, 0}, {0, 0}};
        ClearRow clearRow = new ClearRow(2, matrix, 300);

        assertEquals(2, clearRow.getLinesRemoved());
        assertEquals(300, clearRow.getScoreBonus());
    }

    @Test
    @DisplayName("ClearRow should return copy of matrix")
    void testClearRowMatrixCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        ClearRow clearRow = new ClearRow(1, original, 100);

        int[][] retrieved = clearRow.getNewMatrix();
        assertNotSame(original, retrieved);

        // Modify retrieved and check original unchanged
        retrieved[0][0] = 99;
        assertEquals(1, original[0][0]);
    }

    @Test
    @DisplayName("GameStateView should store brick data and positions")
    void testGameStateView() {
        int[][] brickData = {{1, 1}, {1, 1}};
        int[][] nextBrick = {{2, 2}, {2, 2}};
        int[][] heldBrick = {{0, 0}, {0, 0}};

        GameStateView view = new GameStateView(
                brickData, 5, 10, nextBrick, heldBrick, 5, 15
        );

        assertEquals(5, view.getXPosition());
        assertEquals(10, view.getYPosition());
        assertEquals(5, view.getGhostXPosition());
        assertEquals(15, view.getGhostYPosition());
    }

    @Test
    @DisplayName("GameStateView should return copies of arrays")
    void testGameStateViewArrayCopies() {
        int[][] brickData = {{1, 1}, {1, 1}};
        int[][] nextBrick = {{2, 2}, {2, 2}};
        int[][] heldBrick = {{3, 3}, {3, 3}};

        GameStateView view = new GameStateView(
                brickData, 0, 0, nextBrick, heldBrick, 0, 0
        );

        int[][] retrievedBrick = view.getBrickData();
        int[][] retrievedNext = view.getNextBrickData();
        int[][] retrievedHeld = view.getHeldBrickData();

        assertNotSame(brickData, retrievedBrick);
        assertNotSame(nextBrick, retrievedNext);
        assertNotSame(heldBrick, retrievedHeld);

        // Modify and check originals unchanged
        retrievedBrick[0][0] = 99;
        assertEquals(1, brickData[0][0]);
    }

    @Test
    @DisplayName("MoveResultData should store clear row and view data")
    void testMoveResultData() {
        int[][] matrix = {{0, 0}};
        ClearRow clearRow = new ClearRow(1, matrix, 100);

        int[][] brickData = {{1, 1}};
        GameStateView view = new GameStateView(
                brickData, 0, 0, brickData, brickData, 0, 0
        );

        MoveResultData result = new MoveResultData(clearRow, view);

        assertNotNull(result.getClearRow());
        assertNotNull(result.getViewData());
        assertEquals(1, result.getClearRow().getLinesRemoved());
    }

    @Test
    @DisplayName("MoveResultData should handle null clear row")
    void testMoveResultDataNullClearRow() {
        int[][] brickData = {{1, 1}};
        GameStateView view = new GameStateView(
                brickData, 0, 0, brickData, brickData, 0, 0
        );

        MoveResultData result = new MoveResultData(null, view);

        assertNull(result.getClearRow());
        assertNotNull(result.getViewData());
    }

    @Test
    @DisplayName("NextBrickInfo should store shape and position")
    void testNextBrickInfo() {
        int[][] shape = {{1, 0}, {1, 1}};
        NextBrickInfo info = new NextBrickInfo(shape, 2);

        assertEquals(2, info.getPosition());
        assertNotNull(info.getShape());
    }

    @Test
    @DisplayName("NextBrickInfo should return copy of shape")
    void testNextBrickInfoShapeCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        NextBrickInfo info = new NextBrickInfo(original, 0);

        int[][] retrieved = info.getShape();
        assertNotSame(original, retrieved);

        retrieved[0][0] = 99;
        assertEquals(1, original[0][0]);
    }

    @Test
    @DisplayName("EventType should have all expected values")
    void testEventTypeValues() {
        EventType[] types = EventType.values();

        assertTrue(types.length >= 6);
        assertNotNull(EventType.valueOf("DOWN"));
        assertNotNull(EventType.valueOf("LEFT"));
        assertNotNull(EventType.valueOf("RIGHT"));
        assertNotNull(EventType.valueOf("ROTATE"));
        assertNotNull(EventType.valueOf("INSTANT_DROP"));
        assertNotNull(EventType.valueOf("HOLD"));
    }

    @Test
    @DisplayName("EventSource should have USER and THREAD")
    void testEventSourceValues() {
        EventSource[] sources = EventSource.values();

        assertEquals(2, sources.length);
        assertNotNull(EventSource.valueOf("USER"));
        assertNotNull(EventSource.valueOf("THREAD"));
    }
}
