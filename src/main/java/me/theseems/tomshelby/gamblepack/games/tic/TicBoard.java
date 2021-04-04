package me.theseems.tomshelby.gamblepack.games.tic;

public class TicBoard {
  private final TicCell[][] board;
  private TicCell winner;
  private int moveCount;

  public TicBoard(int size) {
    board = new TicCell[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = TicCell.BLANK;
      }
    }

    winner = null;
    moveCount = 0;
  }

  private void preconditionsForPosition(int x, int y) {
    if (x < 0 || y < 0 || x >= board.length || y >= board.length)
      throw new TicExceptions.IllegalMoveException(
          x, y, TicExceptions.IllegalMoveType.OUT_OF_BORDERS);
  }

  private void preconditionsForMove(int x, int y, TicCell move) {
    preconditionsForPosition(x, y);

    if (winner != null)
      throw new TicExceptions.IllegalMoveException(x, y, TicExceptions.IllegalMoveType.GAME_ENDED);

    if (moveCount % 2 == 0 && move != TicCell.X
        || moveCount % 2 != 0 && move != TicCell.O)
      throw new TicExceptions.IllegalMoveException(x, y, TicExceptions.IllegalMoveType.OUT_OF_TURN);

    if (board[x][y] != TicCell.BLANK)
      throw new TicExceptions.IllegalMoveException(
          x, y, TicExceptions.IllegalMoveType.ALREADY_PLACED);
  }

  public void makeMove(int x, int y, TicCell move) {
    preconditionsForMove(x, y, move);
    board[x][y] = move;
    moveCount++;
    winner = checkWinFromMove(x, y, move);
  }

  private TicCell internalGetPoint(int x, int y) {
    preconditionsForPosition(x, y);
    return board[x][y];
  }

  public TicCell getPoint(int x, int y) {
    //noinspection SuspiciousNameCombination
    return internalGetPoint(y, x);
  }

  private TicCell checkWinFromMove(int x, int y, TicCell state) {
    // Check columns
    for (int i = 0; i < board.length; i++) {
      if (board[x][i] != state) break;
      if (i == board.length - 1) {
        // 'State' wins
        return state;
      }
    }

    // Check row
    for (int i = 0; i < board.length; i++) {
      if (board[i][y] != state) break;
      if (i == board.length - 1) {
        // 'State' wins
        return state;
      }
    }

    // Check diagonal
    if (x == y) {
      for (int i = 0; i < board.length; i++) {
        if (board[i][i] != state) break;
        if (i == board.length - 1) {
          // 'State' wins
          return state;
        }
      }
    }

    // Check anti-diagonal
    if (x + y == board.length - 1) {
      for (int i = 0; i < board.length; i++) {
        if (board[i][(board.length - 1) - i] != state) break;
        if (i == board.length - 1) {
          // 'State' wins
          return state;
        }
      }
    }

    // Check draw
    if (moveCount == (Math.pow(board.length, 2) - 1)) {
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
    return board.length;
  }
}
