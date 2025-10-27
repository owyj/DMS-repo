package com.comp2042.input;

import com.comp2042.game.logic.DownData;
import com.comp2042.game.logic.MoveEvent;
import com.comp2042.ui.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
