package me.theseems.tomshelby.gamblepack.games.gamble;

import me.theseems.tomshelby.gamblepack.api.GameState;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class GambleInfo {
  public static final GameState GAMBLE_REQUEST_SENT = new GameState("gamble_request_sent");
  public static final GameState GAMBLE_ALL_ACCEPTED = new GameState("gamble_all_accepted");
  public static final GameState GAMBLE_ALL_WITHDRAWN = new GameState("gamble_withdrawn");
  public static final GameState GAMBLE_WIN = new GameState("gamble_win");

  private Message initial;
  private final BigDecimal amount;
  private final Set<User> accepted;
  private final Set<User> winners;

  public GambleInfo(BigDecimal amount) {
    this.amount = amount;
    this.accepted = new TreeSet<>(Comparator.comparing(User::getId));
    this.winners = new TreeSet<>(Comparator.comparing(User::getId));
  }

  public Message getInitial() {
    return initial;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setInitial(Message initial) {
    this.initial = initial;
  }

  public Set<User> getAccepted() {
    return accepted;
  }

  public Set<User> getWinners() {
    return winners;
  }
}
