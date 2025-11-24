package com.comp2042.tetris.dto;

import com.comp2042.tetris.input.EventSource;
import com.comp2042.tetris.input.EventType;

public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventSource getEventSource() {
        return eventSource;
    }
}
