package me.theseems.tomshelby.gamblepack.games.tic;

public class TicExceptions {
  public enum IllegalMoveType {
    GAME_ENDED,
    OUT_OF_TURN,
    OUT_OF_BORDERS,
    ALREADY_PLACED
  }

  public static class IllegalMoveException extends IllegalStateException {
    private final int x;
    private final int y;
    private final IllegalMoveType reason;
    private final TicCell move;

    public IllegalMoveException(int x, int y, IllegalMoveType reason) {
      this.x = x;
      this.y = y;
      this.move = null;
      this.reason = reason;
    }

    public IllegalMoveException(int x, int y, TicCell move, IllegalMoveType reason) {
      this.x = x;
      this.y = y;
      this.move = move;
      this.reason = reason;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public TicCell getMove() {
      return move;
    }

    public IllegalMoveType getReason() {
      return reason;
    }

    @Override
    public String getMessage() {
      return "Illegal move has taken place: x="
          + x
          + " y="
          + y
          + " type of "
          + reason
          + (move != null ? " with " + move : "");
    }
  }
}
