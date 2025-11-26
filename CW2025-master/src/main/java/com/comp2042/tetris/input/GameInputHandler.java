package com.comp2042.tetris.input;

import com.comp2042.tetris.dto.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameInputHandler {
    private final InputEventListener eventListener;
    private final GameInputCallback callback;

    public GameInputHandler(InputEventListener eventListener, GameInputCallback callback) {
        this.eventListener = eventListener;
        this.callback = callback;
    }

    public void handleKeyPress(KeyEvent keyEvent, boolean canMove, boolean canPause) {
        KeyCode code = keyEvent.getCode();

        // Global controls (work in most states)
        if (code == KeyCode.N && canMove) {
            callback.onNewGame();
            keyEvent.consume();
            return;
        }

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

    private void handleGameControls(KeyCode code, KeyEvent keyEvent) {
        switch (code) {
            case LEFT, A -> {
                callback.onMove(eventListener.onLeftEvent(
                        new MoveEvent(EventType.LEFT, EventSource.USER)
                ));
                keyEvent.consume();
            }
            case RIGHT, D -> {
                callback.onMove(eventListener.onRightEvent(
                        new MoveEvent(EventType.RIGHT, EventSource.USER)
                ));
                keyEvent.consume();
            }
            case UP, W -> {
                callback.onMove(eventListener.onRotateEvent(
                        new MoveEvent(EventType.ROTATE, EventSource.USER)
                ));
                keyEvent.consume();
            }
            case DOWN, S -> {
                callback.onMoveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                keyEvent.consume();
            }
            case SPACE -> {
                callback.onInstantDrop(new MoveEvent(EventType.INSTANT_DROP, EventSource.USER));
                keyEvent.consume();
            }
            case C, SHIFT -> {
                callback.onHold(new MoveEvent(EventType.HOLD, EventSource.USER));
                keyEvent.consume();
            }
        }
    }
}
