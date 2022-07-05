package me.theseems.tomshelby.gamblepack.impl.board;

import me.theseems.tomshelby.gamblepack.api.board.Board;
import me.theseems.tomshelby.gamblepack.utils.GameExceptions;

import java.lang.reflect.Array;

public class GameBoard<T> implements Board<T> {
  private final T[][] board;

  public GameBoard(int size, Class<T> clazz, T initial) {
    //noinspection unchecked
    board = (T[][]) Array.newInstance(clazz, size, size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = initial;
      }
    }
  }

  private void preconditionsForPosition(int x, int y) {
    if (x < 0 || y < 0 || x >= board.length || y >= board.length)
      throw new GameExceptions.IllegalMoveException(
          x, y, GameExceptions.IllegalMoveType.OUT_OF_BORDERS);
  }

  public void set(int x, int y, T move) {
    preconditionsForPosition(x, y);
    board[x][y] = move;
  }

  public T get(int x, int y) {
    preconditionsForPosition(x, y);
    return board[x][y];
  }

  @Override
  public int getSize() {
    return board.length;
  }
}
