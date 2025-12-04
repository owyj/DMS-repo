package com.comp2042.tetris.input;

import com.comp2042.tetris.dto.GameStateView;
import com.comp2042.tetris.dto.MoveEvent;
import com.comp2042.tetris.dto.MoveResultData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game Input Handler Tests")
class GameInputHandlerTest {

    private GameInputHandler handler;
    private TestInputEventListener eventListener;
    private TestGameInputCallback callback;

    @BeforeEach
    void setUp() {
        eventListener = new TestInputEventListener();
        callback = new TestGameInputCallback();
        handler = new GameInputHandler(eventListener, callback);
    }

    @Test
    @DisplayName("Should handle left arrow key")
    void testLeftArrowKey() {
        KeyEvent event = createKeyEvent(KeyCode.LEFT);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveCalled);
        assertEquals(EventType.LEFT, eventListener.lastEventType);
    }

    @Test
    @DisplayName("Should handle A key for left movement")
    void testAKey() {
        KeyEvent event = createKeyEvent(KeyCode.A);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveCalled);
        assertEquals(EventType.LEFT, eventListener.lastEventType);
    }

    @Test
    @DisplayName("Should handle right arrow key")
    void testRightArrowKey() {
        KeyEvent event = createKeyEvent(KeyCode.RIGHT);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveCalled);
        assertEquals(EventType.RIGHT, eventListener.lastEventType);
    }

    @Test
    @DisplayName("Should handle D key for right movement")
    void testDKey() {
        KeyEvent event = createKeyEvent(KeyCode.D);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveCalled);
        assertEquals(EventType.RIGHT, eventListener.lastEventType);
    }

    @Test
    @DisplayName("Should handle up arrow for rotation")
    void testUpArrowKey() {
        KeyEvent event = createKeyEvent(KeyCode.UP);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveCalled);
        assertEquals(EventType.ROTATE, eventListener.lastEventType);
    }

    @Test
    @DisplayName("Should handle W key for rotation")
    void testWKey() {
        KeyEvent event = createKeyEvent(KeyCode.W);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveCalled);
        assertEquals(EventType.ROTATE, eventListener.lastEventType);
    }

    @Test
    @DisplayName("Should handle down arrow for soft drop")
    void testDownArrowKey() {
        KeyEvent event = createKeyEvent(KeyCode.DOWN);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveDownCalled);
        assertNotNull(callback.lastMoveDownEvent);
        assertEquals(EventType.DOWN, callback.lastMoveDownEvent.getEventType());
    }

    @Test
    @DisplayName("Should handle S key for soft drop")
    void testSKey() {
        KeyEvent event = createKeyEvent(KeyCode.S);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onMoveDownCalled);
        assertNotNull(callback.lastMoveDownEvent);
        assertEquals(EventType.DOWN, callback.lastMoveDownEvent.getEventType());
    }

    @Test
    @DisplayName("Should handle space for instant drop")
    void testSpaceKey() {
        KeyEvent event = createKeyEvent(KeyCode.SPACE);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onInstantDropCalled);
        assertNotNull(callback.lastInstantDropEvent);
        assertEquals(EventType.INSTANT_DROP, callback.lastInstantDropEvent.getEventType());
    }

    @Test
    @DisplayName("Should handle C key for hold")
    void testCKey() {
        KeyEvent event = createKeyEvent(KeyCode.C);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onHoldCalled);
        assertNotNull(callback.lastHoldEvent);
        assertEquals(EventType.HOLD, callback.lastHoldEvent.getEventType());
    }

    @Test
    @DisplayName("Should handle SHIFT key for hold")
    void testShiftKey() {
        KeyEvent event = createKeyEvent(KeyCode.SHIFT);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onHoldCalled);
        assertNotNull(callback.lastHoldEvent);
        assertEquals(EventType.HOLD, callback.lastHoldEvent.getEventType());
    }

    @Test
    @DisplayName("Should handle P key for pause")
    void testPKey() {
        KeyEvent event = createKeyEvent(KeyCode.P);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onPauseCalled);
    }

    @Test
    @DisplayName("Should handle N key for new game")
    void testNKey() {
        KeyEvent event = createKeyEvent(KeyCode.N);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onNewGameCalled);
    }

    @Test
    @DisplayName("Should not handle game controls when canMove is false")
    void testNoGameControlsWhenCannotMove() {
        KeyEvent event = createKeyEvent(KeyCode.LEFT);
        handler.handleKeyPress(event, false, true);

        assertFalse(callback.onMoveCalled);
    }

    @Test
    @DisplayName("Should not handle pause when canPause is false")
    void testNoPauseWhenCannotPause() {
        KeyEvent event = createKeyEvent(KeyCode.P);
        handler.handleKeyPress(event, true, false);

        assertFalse(callback.onPauseCalled);
    }

    @Test
    @DisplayName("Should pass USER event source for key presses")
    void testUserEventSource() {
        KeyEvent event = createKeyEvent(KeyCode.DOWN);
        handler.handleKeyPress(event, true, true);

        assertNotNull(callback.lastMoveDownEvent);
        assertEquals(EventSource.USER, callback.lastMoveDownEvent.getEventSource());
    }

    @Test
    @DisplayName("Should allow new game even when cannot move")
    void testNewGameAlwaysAllowed() {
        KeyEvent event = createKeyEvent(KeyCode.N);
        handler.handleKeyPress(event, true, true);

        assertTrue(callback.onNewGameCalled);
    }

    @Test
    @DisplayName("Should allow pause control when can pause")
    void testPauseWhenAllowed() {
        KeyEvent event = createKeyEvent(KeyCode.P);
        handler.handleKeyPress(event, false, true);

        assertTrue(callback.onPauseCalled);
    }

    // Helper method to create KeyEvent
    private KeyEvent createKeyEvent(KeyCode code) {
        return new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                code,
                false,
                false,
                false,
                false
        );
    }

    // Test implementations
    private static class TestInputEventListener implements InputEventListener {
        EventType lastEventType;
        EventSource lastEventSource;

        @Override
        public MoveResultData onDownEvent(MoveEvent event) {
            lastEventType = event.getEventType();
            lastEventSource = event.getEventSource();
            return new MoveResultData(null, createDummyGameStateView());
        }

        @Override
        public GameStateView onLeftEvent(MoveEvent event) {
            lastEventType = event.getEventType();
            lastEventSource = event.getEventSource();
            return createDummyGameStateView();
        }

        @Override
        public GameStateView onRightEvent(MoveEvent event) {
            lastEventType = event.getEventType();
            lastEventSource = event.getEventSource();
            return createDummyGameStateView();
        }

        @Override
        public GameStateView onRotateEvent(MoveEvent event) {
            lastEventType = event.getEventType();
            lastEventSource = event.getEventSource();
            return createDummyGameStateView();
        }

        @Override
        public MoveResultData onInstantDropEvent(MoveEvent event) {
            lastEventType = event.getEventType();
            lastEventSource = event.getEventSource();
            return new MoveResultData(null, createDummyGameStateView());
        }

        @Override
        public GameStateView onHoldEvent(MoveEvent event) {
            lastEventType = event.getEventType();
            lastEventSource = event.getEventSource();
            return createDummyGameStateView();
        }

        @Override
        public void createNewGame() {
        }

        private GameStateView createDummyGameStateView() {
            int[][] dummy = {{0}};
            return new GameStateView(dummy, 0, 0, dummy, dummy, 0, 0);
        }
    }

    private static class TestGameInputCallback implements GameInputCallback {
        boolean onMoveCalled = false;
        boolean onMoveDownCalled = false;
        boolean onInstantDropCalled = false;
        boolean onHoldCalled = false;
        boolean onNewGameCalled = false;
        boolean onPauseCalled = false;
        MoveEvent lastMoveDownEvent = null;
        MoveEvent lastInstantDropEvent = null;
        MoveEvent lastHoldEvent = null;

        @Override
        public void onMove(GameStateView gameStateView) {
            onMoveCalled = true;
        }

        @Override
        public void onMoveDown(MoveEvent event) {
            onMoveDownCalled = true;
            lastMoveDownEvent = event;
        }

        @Override
        public void onInstantDrop(MoveEvent event) {
            onInstantDropCalled = true;
            lastInstantDropEvent = event;
        }

        @Override
        public void onHold(MoveEvent event) {
            onHoldCalled = true;
            lastHoldEvent = event;
        }

        @Override
        public void onNewGame() {
            onNewGameCalled = true;
        }

        @Override
        public void onPause() {
            onPauseCalled = true;
        }
    }
}
