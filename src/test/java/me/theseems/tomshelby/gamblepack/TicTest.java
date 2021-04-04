package me.theseems.tomshelby.gamblepack;

import me.theseems.tomshelby.gamblepack.games.tic.TicBoard;
import me.theseems.tomshelby.gamblepack.games.tic.TicExceptions;
import me.theseems.tomshelby.gamblepack.games.tic.TicCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicTest {

  private void moveAndDisplay(TicBoard board, int x, int y, TicCell state) {
    board.makeMove(x, y, state);
    // System.out.println(TicUtils.getBoardAsciiVisual(board));
  }

  @Test
  public void tic_game1() {
    // 3x3
    TicBoard board = new TicBoard(3);

    moveAndDisplay(board, 0, 0, TicCell.X);
    moveAndDisplay(board, 1, 0, TicCell.O);
    moveAndDisplay(board, 2, 0, TicCell.X);
    moveAndDisplay(board, 2, 1, TicCell.O);
    moveAndDisplay(board, 1, 1, TicCell.X);
    moveAndDisplay(board, 2, 2, TicCell.O);
    moveAndDisplay(board, 0, 2, TicCell.X);

    // X . X
    // O X .
    // X O O
    Assertions.assertNotNull(board.getWinner());
    Assertions.assertEquals(TicCell.X, board.getWinner());

    Assertions.assertThrows(
        TicExceptions.IllegalMoveException.class, () -> board.makeMove(0, 1, TicCell.X));
  }
}
