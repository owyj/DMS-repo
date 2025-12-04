package com.comp2042.tetris.dto;

import com.comp2042.tetris.input.EventSource;
import com.comp2042.tetris.input.EventType;

/**
 * Data Transfer Object (DTO) representing a game movement event.
 * Encapsulates both the type of movement (left, right, down, rotate, etc.) and the source of the event (user input or automatic timer).
 * This distinction allows the game to apply different logic based on event source, such as awarding soft drop points only for user-initiated downward movements.
 * This class is immutable (final) to ensure event data cannot be modified after creation.
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructor: A move event with specified type and source.
     *
     * @param eventType The type of movement being performed
     * @param eventSource The origin of the event (user input or game timer)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    // Getters: gets the type of movement event
    public EventType getEventType() {
        return eventType;
    }

    // Getters: gets the source of the event
    public EventSource getEventSource() {
        return eventSource;
    }
}
