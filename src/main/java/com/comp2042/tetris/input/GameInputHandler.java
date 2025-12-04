package com.comp2042.tetris.input;

import com.comp2042.tetris.dto.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles keyboard input for the Tetris game.
 * Translates raw JavaFX key events into game-specific actions, respecting the current game state (playing, paused, game over).
 * Supports both arrow keys and WASD controls for accessibility.
 * Implements the Command pattern by delegating actions to callback interfaces.
 */
public class GameInputHandler {
    private final InputEventListener eventListener;
    private final GameInputCallback callback;

    /**
     * Constructor: An input handler with necessary dependencies.
     *
     * @param eventListener Listener to process game logic events
     * @param callback Callback to handle UI updates after events
     */
    public GameInputHandler(InputEventListener eventListener, GameInputCallback callback) {
        this.eventListener = eventListener;
        this.callback = callback;
    }

    /**
     * Handles a keyboard press event, routing it to the appropriate handler.
     * Checks game state before processing to ensure only valid actions are executed.
     *
     * @param keyEvent The JavaFX key event to process
     * @param canMove Whether the game is in a state that allows piece movement
     * @param canPause Whether the game is in a state that allows pausing
     */
    public void handleKeyPress(KeyEvent keyEvent, boolean canMove, boolean canPause) {
        KeyCode code = keyEvent.getCode();

        // Global controls (work in most states)
        // 'N' for New Game
        if (code == KeyCode.N && canMove) {
            callback.onNewGame();
            keyEvent.consume();
            return;
        }

        // 'P' for Pause
        if (code == KeyCode.P && canPause) {
            callback.onPause();
            keyEvent.consume();
            return;
        }

        // Game controls (only when active)
        if (canMove) {
            handleGameControls(code, keyEvent);
        }
    }

    /**
     * Handles game-specific controls for piece movement and actions.
     * Supports both arrow keys and WASD for better accessibility.
     * All events are marked as USER source to distinguish from automatic movement.
     *
     * @param code The key code pressed
     * @param keyEvent The key event to consume after processing
     */
    private void handleGameControls(KeyCode code, KeyEvent keyEvent) {
        switch (code) {
            // Left movement: Left Arrow or 'A'
            case LEFT, A -> {
                callback.onMove(eventListener.onLeftEvent(
                        new MoveEvent(EventType.LEFT, EventSource.USER)
                ));
                keyEvent.consume();
            }
            // Right movement: Right Arrow or 'D'
            case RIGHT, D -> {
                callback.onMove(eventListener.onRightEvent(
                        new MoveEvent(EventType.RIGHT, EventSource.USER)
                ));
                keyEvent.consume();
            }
            // Rotation: Up Arrow or 'W'
            case UP, W -> {
                callback.onMove(eventListener.onRotateEvent(
                        new MoveEvent(EventType.ROTATE, EventSource.USER)
                ));
                keyEvent.consume();
            }
            // Down movement: Down Arrow or 'S'
            case DOWN, S -> {
                callback.onMoveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                keyEvent.consume();
            }
            // Instant drop: Spacebar
            case SPACE -> {
                callback.onInstantDrop(new MoveEvent(EventType.INSTANT_DROP, EventSource.USER));
                keyEvent.consume();
            }
            // Hold piece: 'C' or Shift
            case C, SHIFT -> {
                callback.onHold(new MoveEvent(EventType.HOLD, EventSource.USER));
                keyEvent.consume();
            }
        }
    }
}
