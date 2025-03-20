package com.comp301.a09akari.model;

import java.util.ArrayList;

public class ModelImpl implements Model {

  private final PuzzleLibrary puzzleLibrary;
  private int libIndex;
  private int[][] lampLocations;
  private final boolean solved;
  private final ArrayList<ModelObserver> observers;

  public ModelImpl(PuzzleLibrary puzzleLibrary) {
    if (puzzleLibrary == null) {
      throw new IllegalArgumentException("Puzzle library cannot be null");
    }
    this.puzzleLibrary = puzzleLibrary;
    this.libIndex = 0;
    this.solved = false;
    this.observers = new ArrayList<>();
    int r = puzzleLibrary.getPuzzle(libIndex).getHeight();
    int c = puzzleLibrary.getPuzzle(libIndex).getWidth();
    lampLocations = new int[r][c];
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        lampLocations[i][j] = 0;
      }
    }
  }

  @Override
  public void addLamp(int r, int c) {
    checkPuzzleSquareAddLamp(r, c);
    lampLocations[r][c] = 1;
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }

  private void checkPuzzleSquareAddLamp(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(libIndex);
    if (r >= puzzle.getHeight() || r < 0 || c >= puzzle.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException("Cannot add lamp, this cell is out of bounds");
    }
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cannot add lamp, this cell is not a corridor");
    }
  }

  @Override
  public void removeLamp(int r, int c) {
    checkPuzzleSquareRemoveLamp(r, c);
    lampLocations[r][c] = 0;
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }

  private void checkPuzzleSquareRemoveLamp(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(libIndex);
    if (r > puzzle.getWidth() || r < 0 || c > puzzle.getHeight() || c < 0) {
      throw new IndexOutOfBoundsException("Cannot remove lamp, this cell is out of bounds");
    }
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cannot remove lamp, this cell is not a corridor");
    }
  }

  @Override
  public boolean isLit(int r, int c) {
    checkPuzzleSquareIsLit(r, c);
    boolean isLit = false;
    // check from right to left
    for (int i = getActivePuzzle().getWidth() - 1; i > c; i--) {
      if (getActivePuzzle().getCellType(r, i) == CellType.CORRIDOR) {
        if (lampLocations[r][i] == 1) {
          isLit = true;
        }
      }
      if (getActivePuzzle().getCellType(r, i) != CellType.CORRIDOR) {
        isLit = false;
      }
    }
    if (isLit) {
      return true;
    }

    // check from left to right
    for (int j = 0; j <= c; j++) {
      if (getActivePuzzle().getCellType(r, j) == CellType.CORRIDOR) {
        if (lampLocations[r][j] == 1) {
          isLit = true;
        }
      }
      if (getActivePuzzle().getCellType(r, j) != CellType.CORRIDOR) {
        isLit = false;
      }
      if (j == c && isLit) {
        return true;
      }
    }
    if (isLit) {
      return true;
    }

    // check from top to bottom
    for (int i = 0; i <= r; i++) {
      if (getActivePuzzle().getCellType(i, c) == CellType.CORRIDOR) {
        if (lampLocations[i][c] == 1) {
          isLit = true;
        }
      }
      if (getActivePuzzle().getCellType(i, c) != CellType.CORRIDOR) {
        isLit = false;
      }
      if (i == r && isLit) {
        return true;
      }
    }
    if (isLit) {
      return true;
    }

    // check from bottom to top
    for (int i = getActivePuzzle().getHeight() - 1; i > r; i--) {
      if (getActivePuzzle().getCellType(i, c) == CellType.CORRIDOR) {
        if (lampLocations[i][c] == 1) {
          isLit = true;
        }
      }
      if (getActivePuzzle().getCellType(i, c) != CellType.CORRIDOR) {
        isLit = false;
      }
    }
    if (isLit) {
      return true;
    }
    return isLit;
  }

  private void checkPuzzleSquareIsLit(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(libIndex);
    if (r >= puzzle.getHeight() || r < 0 || c >= puzzle.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException("Cannot check isLit, this cell is out of bounds");
    }
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cannot check isLit, this cell is not a corridor");
    }
  }

  @Override
  public boolean isLamp(int r, int c) {
    checkPuzzleSquareIsLamp(r, c);
    return lampLocations[r][c] == 1;
  }

  private void checkPuzzleSquareIsLamp(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(libIndex);
    if (r > puzzle.getHeight() || r < 0 || c > puzzle.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException("Cannot check isLamp, this cell is out of bounds");
    }
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cannot check isLamp, this cell is not a corridor");
    }
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    Puzzle p = puzzleLibrary.getPuzzle(libIndex);
    boolean illegal = false;
    if (r > p.getHeight() || r < 0 || c > p.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (lampLocations[r][c] != 1) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < r; i++) {
      if (p.getCellType(i, c) == CellType.CORRIDOR) {
        if (lampLocations[i][c] == 1) {
          illegal = true;
        }
      }
      if (p.getCellType(i, c) == CellType.WALL || p.getCellType(i, c) == CellType.CLUE) {
        illegal = false;
      }
    }
    if (illegal) {
      return true;
    }
    for (int i = p.getHeight() - 1; i > r; i--) {
      if (p.getCellType(i, c) == CellType.CORRIDOR) {
        if (lampLocations[i][c] == 1) {
          illegal = true;
        }
      }
      if (p.getCellType(i, c) == CellType.WALL || p.getCellType(i, c) == CellType.CLUE) {
        illegal = false;
      }
    }
    if (illegal) {
      return true;
    }
    for (int j = 0; j < c; j++) {
      if (p.getCellType(r, j) == CellType.CORRIDOR) {
        if (lampLocations[r][j] == 1) {
          illegal = true;
        }
      }
      if (p.getCellType(r, j) == CellType.WALL || p.getCellType(r, j) == CellType.CLUE) {
        illegal = false;
      }
    }
    if (illegal) {
      return true;
    }
    for (int j = p.getWidth() - 1; j > c; j--) {
      if (p.getCellType(r, j) == CellType.CORRIDOR) {
        if (lampLocations[r][j] == 1) {
          illegal = true;
        }
      }
      if (p.getCellType(r, j) == CellType.WALL || p.getCellType(r, j) == CellType.CLUE) {
        illegal = false;
      }
    }
    return illegal;
  }

  private void checkPuzzleSquareIsLampIllegal(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(libIndex);
    if (r > puzzle.getWidth() || r < 0 || c > puzzle.getHeight() || c < 0) {
      throw new IndexOutOfBoundsException("Cannot check isLampIllegal, this cell is out of bounds");
    }
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cannot check isLampIllegal, this cell is not a corridor");
    }
  }

  @Override
  public Puzzle getActivePuzzle() {
    return puzzleLibrary.getPuzzle(libIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return libIndex;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index >= puzzleLibrary.size() || index < 0) {
      throw new IndexOutOfBoundsException("Cannot set active puzzle index out of bounds");
    }
    libIndex = index;
    int r = puzzleLibrary.getPuzzle(libIndex).getHeight();
    int c = puzzleLibrary.getPuzzle(libIndex).getWidth();
    lampLocations = new int[r][c];
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        lampLocations[i][j] = 0;
      }
    }
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    int r = puzzleLibrary.getPuzzle(libIndex).getHeight();
    int c = puzzleLibrary.getPuzzle(libIndex).getWidth();
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        lampLocations[i][j] = 0;
      }
    }
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public boolean isSolved() {
    Puzzle p = puzzleLibrary.getPuzzle(libIndex);
    for (int r = 0; r < p.getHeight(); r++) {
      for (int c = 0; c < p.getWidth(); c++) {
        switch (p.getCellType(r, c)) {
          case WALL:
            break;
          case CORRIDOR:
            if (this.isLamp(r, c)) {
              if (this.isLampIllegal(r, c)) {
                return false;
              }
            }
            if (!this.isLit(r, c)) {
              return false;
            }
            break;
          case CLUE:
            if (!this.isClueSatisfied(r, c)) {
              return false;
            }
            break;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    Puzzle p = puzzleLibrary.getPuzzle(libIndex);
    if (r > p.getHeight() || r < 0 || c > p.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (p.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException();
    }
    int numLamps = 0;
    boolean ri = true;
    boolean l = true;
    boolean top = true;
    boolean bott = true;
    if (r - 1 < 0) {
      top = false;
    }
    if (r + 1 >= p.getHeight()) {
      bott = false;
    }
    if (c - 1 < 0) {
      l = false;
    }
    if (c + 1 >= p.getWidth()) {
      ri = false;
    }
    // if 0 lamps allowed
    if (p.getClue(r, c) == 0) {
      if (top) {
        if (p.getCellType(r - 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r - 1, c)) {
            return false;
          }
        }
      }
      if (bott) {
        if (p.getCellType(r + 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r + 1, c)) {
            return false;
          }
        }
      }
      if (l) {
        if (p.getCellType(r, c - 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c - 1)) {
            return false;
          }
        }
      }
      if (ri) {
        if (p.getCellType(r, c + 1) == CellType.CORRIDOR) {
          return !this.isLamp(r, c + 1);
        }
      }
      return true;
    }
    // if 1 lamp allowed
    if (p.getClue(r, c) == 1) {
      if (top) {
        if (p.getCellType(r - 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r - 1, c)) {
            numLamps++;
          }
        }
      }
      if (bott) {
        if (p.getCellType(r + 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r + 1, c)) {
            numLamps++;
          }
        }
      }
      if (l) {
        if (p.getCellType(r, c - 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c - 1)) {
            numLamps++;
          }
        }
      }
      if (ri) {
        if (p.getCellType(r, c + 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c + 1)) {
            numLamps++;
          }
        }
      }
      if (numLamps == 1) {
        return true;
      }
    }
    // if 2 lamps allowed
    if (p.getClue(r, c) == 2) {
      if (top) {
        if (p.getCellType(r - 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r - 1, c)) {
            numLamps++;
          }
        }
      }
      if (bott) {
        if (p.getCellType(r + 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r + 1, c)) {
            numLamps++;
          }
        }
      }
      // up - what if no cell above
      if (l) {
        if (p.getCellType(r, c - 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c - 1)) {
            numLamps++;
          }
        }
      }
      if (ri) {
        if (p.getCellType(r, c + 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c + 1)) {
            numLamps++;
          }
        }
      }
      if (numLamps == 2) {
        return true;
      }
    }
    // if 3 lamps
    if (p.getClue(r, c) == 3) {
      if (top) {
        if (p.getCellType(r - 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r - 1, c)) {
            numLamps++;
          }
        }
      }
      if (bott) {
        if (p.getCellType(r + 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r + 1, c)) {
            numLamps++;
          }
        }
      }
      if (l) {
        if (p.getCellType(r, c - 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c - 1)) {
            numLamps++;
          }
        }
      }
      if (ri) {
        if (p.getCellType(r, c + 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c + 1)) {
            numLamps++;
          }
        }
      }
      if (numLamps == 3) {
        return true;
      }
    }
    // if 4 lamps
    if (p.getClue(r, c) == 4) {
      if (top) {
        if (p.getCellType(r - 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r - 1, c)) {
            numLamps++;
          }
        }
      }
      if (bott) {
        if (p.getCellType(r + 1, c) == CellType.CORRIDOR) {
          if (this.isLamp(r + 1, c)) {
            numLamps++;
          }
        }
      }
      if (l) {
        if (p.getCellType(r, c - 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c - 1)) {
            numLamps++;
          }
        }
      }
      if (ri) {
        if (p.getCellType(r, c + 1) == CellType.CORRIDOR) {
          if (this.isLamp(r, c + 1)) {
            numLamps++;
          }
        }
      }
      return numLamps == 4;
    }
    return false;
  }

  private void checkPuzzleSquareIsClueSatisfied(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(libIndex);
    if (r > puzzle.getWidth() || r < 0 || c > puzzle.getHeight() || c < 0) {
      throw new IndexOutOfBoundsException(
          "Cannot check isClueSatisfied, this cell is out of bounds");
    }
    if (puzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cannot check is ClueSatisfied, this cell is not a clue");
    }
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }
}
