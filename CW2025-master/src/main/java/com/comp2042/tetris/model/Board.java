package com.comp2042.tetris.model;

import com.comp2042.tetris.dto.ClearRow;
import com.comp2042.tetris.dto.GameStateView;
import com.comp2042.tetris.model.piece.Brick;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    GameStateView getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    Brick holdBrick();
}
