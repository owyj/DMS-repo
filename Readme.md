# Tetris Game - Documentation

## Github Repository
```
https://github.com/owyj/DMS-repo
```

## Table of Contents
- [Compilation Instructions](#compilation-instructions)
- [Implemented and Working Properly](#implemented-and-working-properly)
- [Implemented but Not Working Properly](#implemented-but-not-working-properly)
- [Features Not Implemented](#features-not-implemented)
- [New Java Classes](#new-java-classes)
- [Modified Java Classes](#modified-java-classes)
- [Unexpected Problems](#unexpected-problems)

---

## Compilation Instructions

### Prerequisites
- **Java JDK 23 or higher**
- **JavaFX SDK 21.0.6 or higher**
- **Maven** (optional, if using Maven build)

### Steps to Compile and Run
#### Using IDE (Intellij IDEA)
1. **Clone the repository:**
   ```
   git clone https://github.com/owyj/DMS-repo.git
   cd DMS-repo/CW2025-master
   ```

2. **Configure JavaFX:**
   - Download JavaFX SDK from **https://openjfx.io/**
   - Set PATH_TO_FX environment variable to your JavaFX lib directory
  
3. **Compile the project:**
   ```
   javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -d bin src/main/java/com/comp2042/tetris/**/*.java
   ```
   
5. **Run the application:**
   ```
   java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp bin com.comp2042.tetris.app.Main
   ```

---

## Implemented and Working Properly

### Tetris Game Features

| # | Feature | Location | Description |
|---|---------|----------|-------------|
| 1 | **Refactored Package Structure** | Entire codebase | Reorganized into MVC pattern hierarchy |
| 2 | **State Pattern for Game States** | `model/GameState.java`, `model/PlayingState.java`, `model/PausedState.java`, `model/GameOverState.java` | Manages Playing, Paused, Game Over states without boolean flags |
| 3 | **Factory Pattern for Brick Generation** | `model/piece/BrickFactory.java` | Enum-based approach for creating brick types |
| 4 | **Bag Randomizer for Brick Generation** | `model/piece/RandomBrickGenerator.java` | Ensures all 7 brick types appear once before repeating |
| 5 | **Hold Piece Feature** | `model/GameBoard.java`, `controller/GameController.java`, `view/BrickRenderer.java` | Hold current piece and swap with previously held piece |
| 6 | **Hard Drop Feature** | `controller/GameController.java`, `input/GameInputHandler.java` | Space key instantly drops piece with bonus points |
| 7 | **Ghost Piece Preview** | `model/GameBoard.java`, `view/BrickRenderer.java`, `util/ColorMapper.java` | Translucent preview showing where piece will land |
| 8 | **Level System with Dynamic Speed** | `model/LevelManager.java`, `controller/GameController.java` | Progressive speed increase as lines are cleared |
| 9 | **Enhanced Scoring System** | `util/MatrixOperations.java`, `controller/GameController.java` | Tetris-standard scoring (see table below) |
| 10 | **High Score Persistence** | `util/HighScoreManager.java`, `model/Score.java` | File-based high score saving in `.tetrisjfx/tetris_highscore.dat` |
| 11 | **Improved Wall Kick System** | `model/GameBoard.java` | Multiple offset attempts for rotation with special I-piece handling |
| 12 | **Background Music System** | `util/MusicManager.java`, `app/controller/StartMenuController.java` | Singleton-based music manager for gameplay |
| 13 | **Pause Overlay with Options** | `view/component/PauseOverlayManager.java` | Resume, instructions, and main menu options |
| 14 | **Main Menu System** | `app/controller/StartMenuController.java`, `util/SceneNavigator.java` | Start game, view instructions, exit options |
| 15 | **Game Over Panel Enhancement** | `view/component/GameOverPanel.java` | Styled buttons for New Game and Main Menu |
| 16 | **Instructions Dialog** | `util/DialogHelper.java` | Reusable dialog with controls and scoring info |
| 17 | **Enhanced Visual Styling** | `util/ColorMapper.java`, `view/BrickRenderer.java`, `view/BoardRenderer.java` | Centralized colors and rounded rectangle rendering |
| 18 | **Next Brick Preview** | `view/BrickRenderer.java`, `view/GameViewController.java` | Enhanced next piece display |
| 19 | **Null Object Pattern for NullBrick** | `model/piece/types/NullBrick.java` | Singleton representing absence of held brick |
| 20 | **Enhanced Input Handling** | `input/GameInputHandler.java`, `input/GameInputCallback.java` | WASD and arrow key support with callback architecture |

### Package Structure Details

| Package | Purpose |
|---------|---------|
| `com.comp2042.tetris.app` | Application entry point |
| `com.comp2042.tetris.controller` | Game logic controllers |
| `com.comp2042.tetris.model` | Data models and game state |
| `com.comp2042.tetris.view` | UI components and renderers |
| `com.comp2042.tetris.dto` | Data Transfer Objects |
| `com.comp2042.tetris.input` | Input handling |
| `com.comp2042.tetris.util` | Utility classes |

### Scoring System Details

| Action | Points |
|--------|--------|
| Single line | 100 |
| Double line | 300 |
| Triple line | 500 |
| Tetris (4 lines) | 800 |
| Soft drop | 1 per cell |
| Hard drop | 5 per cell |

### Instructions

| Key(s) | Action |
|--------|--------|
| **← → or A D** | Move piece left/right |
| **↑ or W** | Rotate piece |
| **↓ or S** | Soft drop (move down faster) |
| **SPACE** | Hard drop |
| **C or SHIFT** | Hold piece |
| **P** | Pause/Resume |
| **N** | New game |

---

## Implemented but Not Working Properly

| # | Issue | Location | Attempted Solution | Status |
|---|-------|----------|-------------------|--------|
| 1 | **Initial Brick Position** - Y-position was 10 in original, refactored uses 0 | `model/GameBoard.java` (createNewBrick) | Changed initial offset to `new Point(4, 0)` | Requires further testing |
| 2 | **Layout Offset Constants** - Magic numbers may not align on all screens | `view/BrickRenderer.java` (BRICK_PANEL_Y_OFFSET_INIT, etc.) | Consolidated into named constants | Needs fine-tuning |

---

## Features Not Implemented

| Feature | Reason |
|---------|--------|
| **Multiplayer Mode** | Time constraints and complexity; requires networking, lobby systems, synchronization |
| **Touch/Mobile Controls** | Desktop-focused with JavaFX; mobile would require different UI framework |
| **Customizable Key Bindings** | Input system refactored but full rebinding with persistence needs additional UI |
| **Sound Effects** | Focus on background music; line clear/rotation sounds were lower priority |
| **Replay System** | Requires input recording and playback logic beyond current scope |
| **Visual Themes/Skins** | Time constraints; ColorMapper enables future themes but not implemented |

---

## New Java Classes

### Model Package

| Class | Description |
|-------|-------------|
| `model/GameState.java` | Interface defining contract for game states with `canMove()`, `canPause()`, `getStateName()` |
| `model/PlayingState.java` | Concrete GameState for active gameplay; allows movement and pausing |
| `model/PausedState.java` | Concrete GameState for paused state; prevents movement, allows unpausing |
| `model/GameOverState.java` | Concrete GameState for game over; prevents all actions until new game |
| `model/LevelManager.java` | Manages level progression, line counting, speed adjustments, UI binding properties |
| `model/piece/BrickFactory.java` | Factory class with enum-based brick creation; `createAllBricks()` method |
| `model/piece/types/NullBrick.java` | Null Object pattern singleton representing no held brick |

### Controller Package

| Class | Description |
|-------|-------------|
| `controller/GameStateManager.java` | Manages state transitions, timeline control, pause/resume, game over display |

### View Package

| Class | Description |
|-------|-------------|
| `view/BrickRenderer.java` | Handles all brick rendering (current, ghost, hold, next) |
| `view/BoardRenderer.java` | Manages game board display and updates |
| `view/component/PauseOverlayManager.java` | Creates and manages pause screen overlay with buttons |
| `view/component/GameOverPanel.java` | Enhanced game over display with action buttons |

### Utility Package

| Class | Description |
|-------|-------------|
| `util/ColorMapper.java` | Centralizes color definitions for bricks and ghost pieces |
| `util/HighScoreManager.java` | File I/O for high score persistence; creates game directory |
| `util/MusicManager.java` | Singleton music player; handles loading, playing, pausing, volume |
| `util/DialogHelper.java` | Utility for showing dialogs (instructions, errors) |
| `util/SceneNavigator.java` | Handles scene navigation (menu/game) with cleanup |

### Input Package

| Class | Description |
|-------|-------------|
| `input/GameInputCallback.java` | Callback interface separating input detection from execution |
| `input/GameInputHandler.java` | Processes keyboard input; maps keys to actions via callbacks |
| `input/EventType.java` | Enum for move event types; includes `INSTANT_DROP` and `HOLD` |
| `input/EventSource.java` | Enum distinguishing user input from automated events |

### DTO Package

| Class | Description |
|-------|-------------|
| `dto/MoveEvent.java` | DTO for movement events; combines type and source |
| `dto/MoveResultData.java` | Renamed from DownData; contains clear row info and view state |
| `dto/GameStateView.java` | Renamed from ViewData; includes ghost position data |
| `dto/NextBrickInfo.java` | Renamed from NextShapeInfo; encapsulates next rotation info |
| `dto/ClearRow.java` | Contains line clear results and new matrix state |

### Application Package

| Class | Description |
|-------|-------------|
| `app/controller/StartMenuController.java` | Controller for main menu FXML; handles button actions |

---

## Modified Java Classes

### 1. Core Model Classes

#### a. `model/GameBoard.java` (formerly `SimpleBoard.java`)

| Change | Description |
|--------|-------------|
| Hold piece functionality | Added `heldBrick` and `canHold` fields |
| `holdBrick()` method | Implements hold restriction logic |
| Ghost piece calculation | Added `calculateGhostPosition()` method |
| Enhanced `getViewData()` | Includes held brick and ghost position |
| Updated `newGame()` | Resets held brick state |
| Fixed initial position | Changed from `(4, 10)` to `(4, 0)` |
| Improved wall kick | Enhanced `rotateLeftBrick()` with I-brick handling |
| Helper method | Added `isIBrick()` for rotation logic |

**Rationale:** Supports new gameplay features (hold, ghost piece) and improves rotation mechanics.

---

#### b. `model/Score.java`

| Change | Description |
|--------|-------------|
| High score property | Added `IntegerProperty` for high score |
| HighScoreManager integration | Persistence support |
| New methods | `highScoreProperty()`, `getHighScore()`, `reloadHighScoreFromFile()`, `saveIfHighScore()` |
| Modified `add()` | Auto-updates and saves high scores |
| Modified `reset()` | Reloads high score |
| Validation | Prevents negative score additions |

**Rationale:** Implements high score tracking and persistence for player engagement.

---

#### c. `model/Board.java`

| Change | Description |
|--------|-------------|
| Return type change | `getViewData()` returns `GameStateView` instead of `ViewData` |
| New method | Added `holdBrick()` signature |

**Rationale:** Interface updated for new features and improved naming.

---

### 2. Brick Generation

#### a. `model/piece/RandomBrickGenerator.java`

| Change | Description |
|--------|-------------|
| Bag randomizer algorithm | Replaced simple random generation |
| `currentBag` field | Tracks current bag state |
| `refillBag()` method | Shuffles complete set |
| BrickFactory integration | Uses `BrickFactory.createAllBricks()` |
| Pre-fill queue | First bag in constructor |
| Auto-refill | When queue drops below 7 pieces |

**Rationale:** Fairer piece distribution prevents long droughts of specific pieces.

---

#### b. `model/piece/BrickRotator.java`

| Change | Description |
|--------|-------------|
| Method rename | `getNextShape()` → `peekNextShape()` |
| Return type | Changed to `NextBrickInfo` |
| Validation | Added checks in `setCurrentShape()` and `setBrick()` |
| New method | Added `getBrick()` |

**Rationale:** Improved naming clarity and defensive programming.

---

### 3. Brick Types

| Classes Modified | Changes |
|------------------|---------|
| `IBrick.java`, `JBrick.java`, `LBrick.java`, `OBrick.java`, `SBrick.java`, `TBrick.java`, `ZBrick.java` | Made `final`, made `public`, moved to `model.piece.types` package |

**Rationale:** Improved encapsulation; prevents accidental inheritance.

---

### 4. View and Rendering

#### a. `view/GameViewController.java` (formerly `GuiController.java`)

| Change | Description |
|--------|-------------|
| Rendering extraction | Logic moved to `BrickRenderer` and `BoardRenderer` |
| State management | Added `GameStateManager` |
| Pause functionality | Added `PauseOverlayManager` |
| Input handling | Implements `GameInputCallback` interface |
| UI bindings | Level and line display bindings added |
| Enhanced `bindScore()` | Includes level and lines properties |
| New methods | `updateGameSpeed()`, `instantDrop()`, `hold()`, `setupNextBrickLabel()`, `exitToMainMenu()` |
| Improved `handleMoveResult()` | Updates all visual elements |

**Rationale:** Separation of concerns for maintainability.

---

#### b. `view/component/NotificationPanel.java`

| Change | Description |
|--------|-------------|
| Package move | From default package to `view.component` |

**Rationale:** Package reorganization.

---

### 5. Controller

#### a. `controller/GameController.java`

| Change | Description |
|--------|-------------|
| Board type | Changed from `SimpleBoard` to `GameBoard` |
| LevelManager integration | Added level management |
| Scoring constants | Added soft drop and hard drop points |
| Enhanced `onDownEvent()` | Integrates with level manager |
| New methods | `updateGameSpeed()`, `onInstantDropEvent()`, `onHoldEvent()` |
| Modified `createNewGame()` | Resets level manager |

**Rationale:** Integration of level system and new gameplay features.

---

### 6. Input Handling

#### a. `input/InputEventListener.java`

| Change | Description |
|--------|-------------|
| Return types | Changed from `ViewData`/`DownData` to `GameStateView`/`MoveResultData` |
| New methods | `onInstantDropEvent()`, `onHoldEvent()` |

**Rationale:** Updated for new features and improved naming.

---

### 7. Utilities

#### a. `util/MatrixOperations.java`

| Change | Description |
|--------|-------------|
| Enhanced `intersect()` | Improved bounds checking |
| Fixed coordinates | Consistent i/j vs x/y ordering |
| Improved `checkOutOfBound()` | Better logic clarity |
| Updated `checkRemoving()` | Tetris-standard scoring (100/300/500/800) |
| Private constructor | Prevents instantiation |

**Rationale:** Fixed bugs, improved scoring, enforced utility class pattern.

---

### 8. Application Entry

#### a. `app/Main.java`

| Change | Description |
|--------|-------------|
| Initial scene | Changed to main menu (`startMenu.fxml`) |
| Window title | "TetrisJFX - Main Menu" |
| Window size | Adjusted to 350x600 |
| Non-resizable | Added `setResizable(false)` |
| Exit handler | Added `setOnCloseRequest` to stop music |
| Removed | Immediate GameController initialization |

**Rationale:** Proper application flow with main menu first and resource cleanup on exit.

---

## Unexpected Problems

| # | Problem | Location | Solution | Outcome/Lesson |
|---|---------|----------|----------|----------------|
| 1 | **Coordinate System Inconsistency** - `i` and `j` used inconsistently for x/y axes causing collision bugs | `util/MatrixOperations.java` | Standardized: `j` for x-coordinate (columns), `i` for y-coordinate (rows) | Coordinate consistency is critical; add clear comments |
| 2 | **Ghost Piece Calculation Edge Cases** - Failed near top of board or invalid states | `model/GameBoard.java` | Added validation before calculation; fallback to current position | Ghost piece now reliably shows correct position |
| 3 | **Hold Piece State Management** - Allowed repeated swaps in same turn | `model/GameBoard.java` | Added `canHold` flag; reset only on new brick creation | Matches standard Tetris rules |
| 4 | **Music Resource Loading** - Relative paths failed in different contexts | `util/MusicManager.java` | Used `getClass().getClassLoader().getResource()`; added null checks | Game runs even if music file missing |
| 5 | **Scene Navigation Memory Leaks** - Timeline and music continued in background | `util/SceneNavigator.java`, `controller/GameStateManager.java` | Created SceneNavigator with proper cleanup; added `setOnCloseRequest` | Prevents resource leaks |
| 6 | **FXML Controller Initialization Order** - NullPointerExceptions on components | `view/GameViewController.java` | Moved setup to methods after FXML injection; added null checks | Always check null on FXML-injected components |
| 7 | **Pause Overlay Sizing** - Didn't cover entire game area | `view/component/PauseOverlayManager.java` | Created `updateSize()` based on parent pane; use `toFront()` | Overlay now properly covers game |
| 8 | **High Score File Permissions** - Directory creation failed on some systems | `util/HighScoreManager.java` | Added try-catch around I/O; game continues without saving | Future: add user feedback or alternative storage |
| 9 | **Property Binding Timing** - Visual elements not updating | `view/GameViewController.java` | Ensure binding after JavaFX initialization; verify `SimpleIntegerProperty` usage | JavaFX binding requires proper init order |
| 10 | **Wall Kick Collision Detection** - I-piece rotation issues | `model/GameBoard.java` | Separate wall kick arrays; I-piece has aggressive kicks (±2) | Required extensive playtesting |

---
