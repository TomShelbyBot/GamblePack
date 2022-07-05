package me.theseems.tomshelby.gamblepack.games.sapper;

import me.theseems.tomshelby.gamblepack.api.GameState;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Collection;

public class SapperInfo {
  public static GameState CELL_CHOICE = new GameState("sapper_cell_choice");
  public static GameState CELL_END = new GameState("sapper_cell_end");

  private final SapperBoard board;
  private Message message;
  private Collection<User> winners = new ArrayList<>();

  public SapperInfo(SapperBoard board) {
    this.board = board;
  }

  public SapperBoard getBoard() {
    return board;
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public Collection<User> getWinners() {
    return winners;
  }

  public void setWinners(Collection<User> winners) {
    this.winners = winners;
  }
}
