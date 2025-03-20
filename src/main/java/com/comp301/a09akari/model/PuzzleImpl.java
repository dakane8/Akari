package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {

  private final int[][] board;

  public PuzzleImpl(int[][] board) {
    this.board = board;
  }

  @Override
  public int getWidth() {
    return board[0].length;
  }

  @Override
  public int getHeight() {
    return board.length;
  }

  @Override
  public CellType getCellType(int r, int c) {
    if (r > this.getHeight() || r < 0 || c > this.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException("out of bounds getcelltype");
    }
    if (board[r][c] == 6) {
      return CellType.CORRIDOR;
    } else if (board[r][c] == 5) {
      return CellType.WALL;
    }
    return CellType.CLUE;
  }

  @Override
  public int getClue(int r, int c) {
    if (r > this.getHeight() || r < 0 || c > this.getWidth() || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (board[r][c] == 0) {
      return 0;
    }
    if (board[r][c] == 1) {
      return 1;
    }
    if (board[r][c] == 2) {
      return 2;
    }
    if (board[r][c] == 3) {
      return 3;
    }
    if (board[r][c] == 4) {
      return 4;
    }
    throw new IllegalArgumentException();
  }
}
