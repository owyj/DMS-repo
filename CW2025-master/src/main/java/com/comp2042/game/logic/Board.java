package com.comp2042.game.logic;

import com.comp2042.logic.bricks.ClearRow;
import com.comp2042.ui.ViewData;
import com.comp2042.logic.bricks.Brick;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    Brick holdBrick();

    Brick getHeldBrick();
}
