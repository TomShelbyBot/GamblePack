package me.theseems.tomshelby.gamblepack.games.sapper;

import me.theseems.tomshelby.gamblepack.api.board.Board;
import me.theseems.tomshelby.gamblepack.impl.board.GameBoard;
import me.theseems.tomshelby.gamblepack.utils.GameExceptions;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.objects.User;

import java.security.SecureRandom;
import java.util.*;

public class SapperBoard implements Board<SapperCell> {
  private static final SecureRandom boardRandom = new SecureRandom();

  private final Board<SapperCell> board;
  private List<User> participants;
  private int moveCount;

  private void with(SapperCell.Type type, int count) {
    Set<Pair<Integer, Integer>> coordinates = new HashSet<>();
    while (coordinates.size() < count) {
      int rx = boardRandom.nextInt(board.getSize());
      int ry = boardRandom.nextInt(board.getSize());

      if (board.get(rx, ry).getType() == SapperCell.Type.BLANK) {
        coordinates.add(new Pair<>(rx, ry));
      }
    }

    coordinates.forEach(
        coords -> board.set(coords.getFirst(), coords.getSecond(), new SapperCell(type)));
  }

  public SapperBoard withBombs(int count) {
    with(SapperCell.Type.BOMB, count);
    return this;
  }

  public SapperBoard withWins(int count) {
    with(SapperCell.Type.WIN, count);
    return this;
  }

  public SapperBoard(int size) {
    board = new GameBoard<>(size, SapperCell.class, new SapperCell(SapperCell.Type.BLANK, true));
    for (int y = 0; y < board.getSize(); y++) {
      for (int x = 0; x < board.getSize(); x++) {
        board.set(x, y, new SapperCell(SapperCell.Type.BLANK, true, x, y));
      }
    }
  }

  public void fillPlayers(Collection<User> players) {
    participants = new ArrayList<>(players);
    Collections.shuffle(participants);
  }

  public User getCurrentPlayer() {
    return participants.get(moveCount);
  }

  public SapperCell move(int x, int y, User user) {
    if (!user.getId().equals(getCurrentPlayer().getId()))
      throw new GameExceptions.IllegalMoveException(x, y, GameExceptions.IllegalMoveType.OUT_OF_TURN);

    SapperCell cell = get(x, y);
    if (!cell.isHidden())
      throw new GameExceptions.IllegalMoveException(x, y, GameExceptions.IllegalMoveType.ALREADY_PLACED);

    cell.setHidden(false);
    moveCount++;
    if (moveCount >= participants.size()) {
      moveCount = 0;
    }

    return cell;
  }

  @Override
  public SapperCell get(int x, int y) {
    return board.get(x, y);
  }

  @Override
  public void set(int x, int y, SapperCell value) {
    board.set(x, y, value);
    value.setX(x);
    value.setY(y);
  }

  @Override
  public int getSize() {
    return board.getSize();
  }
}
