package me.theseems.tomshelby.gamblepack.games.tic;

import me.theseems.tomshelby.gamblepack.api.board.Board;
import me.theseems.tomshelby.gamblepack.impl.board.GameBoard;
import me.theseems.tomshelby.gamblepack.utils.GameExceptions;

public class TicBoard {
  private final Board<TicCell> board;
  private TicCell winner;
  private int moveCount;

  public TicBoard(int size) {
    this.board = new GameBoard<>(size, TicCell.class, TicCell.BLANK);
    moveCount = 0;
  }

  private void preconditionsForPosition(int x, int y) {
    if (x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize())
      throw new GameExceptions.IllegalMoveException(
          x, y, GameExceptions.IllegalMoveType.OUT_OF_BORDERS);
  }

  private void preconditionsForMove(int x, int y, TicCell move) {
    preconditionsForPosition(x, y);

    if (winner != null)
      throw new GameExceptions.IllegalMoveException(x, y, GameExceptions.IllegalMoveType.GAME_ENDED);

    if (moveCount % 2 == 0 && move != TicCell.X || moveCount % 2 != 0 && move != TicCell.O)
      throw new GameExceptions.IllegalMoveException(x, y, GameExceptions.IllegalMoveType.OUT_OF_TURN);

    if (board.get(x, y) != TicCell.BLANK)
      throw new GameExceptions.IllegalMoveException(
          x, y, GameExceptions.IllegalMoveType.ALREADY_PLACED);
  }

  public void makeMove(int x, int y, TicCell move) {
    preconditionsForMove(x, y, move);
    board.set(x, y, move);
    moveCount++;
    winner = checkWinFromMove(x, y, move);
  }

  public TicCell getPoint(int x, int y) {
    return board.get(x, y);
  }

  private TicCell checkWinFromMove(int x, int y, TicCell state) {
    // Check columns
    for (int i = 0; i < board.getSize(); i++) {
      if (board.get(x, i) != state) break;
      if (i == board.getSize() - 1) {
        // 'State' wins
        return state;
      }
    }

    // Check row
    for (int i = 0; i < board.getSize(); i++) {
      if (board.get(i, y) != state) break;
      if (i == board.getSize() - 1) {
        // 'State' wins
        return state;
      }
    }

    // Check diagonal
    if (x == y) {
      for (int i = 0; i < board.getSize(); i++) {
        if (board.get(i, i) != state) break;
        if (i == board.getSize() - 1) {
          // 'State' wins
          return state;
        }
      }
    }

    // Check anti-diagonal
    if (x + y == board.getSize() - 1) {
      for (int i = 0; i < board.getSize(); i++) {
        if (board.get(i, (board.getSize() - 1) - i) != state) break;
        if (i == board.getSize() - 1) {
          // 'State' wins
          return state;
        }
      }
    }

    // Check draw
    if (moveCount == Math.pow(board.getSize(), 2)) {
      return TicCell.BLANK;
    }

    return null;
  }

  public TicCell getWinner() {
    return winner;
  }

  public int getMoveCount() {
    return moveCount;
  }

  public int getSize() {
    return board.getSize();
  }
}
