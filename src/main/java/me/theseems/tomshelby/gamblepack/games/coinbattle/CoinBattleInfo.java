package me.theseems.tomshelby.gamblepack.games.coinbattle;

import me.theseems.tomshelby.gamblepack.api.GameState;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CoinBattleInfo {
  private static final SecureRandom secureRandom = new SecureRandom();

  /** Custom coin battle game states. */
  public static final GameState WAIT_FOR_CHOICE = GameState.name("wait_for_choice");

  public static final GameState ALL_CHOSEN = GameState.name("all_chosen");

  public static final GameState BATTLE_END = GameState.name("battle_end");

  /** Info for one session of the game. */
  private Message message;
  private final Set<User> winners = new HashSet<>();

  private final Map<User, Boolean> userChoices = new HashMap<>();

  public void setMessage(Message message) {
    this.message = message;
  }

  public Message getMessage() {
    return message;
  }

  public Map<User, Boolean> getUserChoices() {
    return userChoices;
  }

  public static SecureRandom getRandom() {
    return secureRandom;
  }

  public Set<User> getWinners() {
    return winners;
  }
}
