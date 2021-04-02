package me.theseems.tomshelby.gamblepack.api;

import me.theseems.tomshelby.storage.TomMeta;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collection;
import java.util.UUID;

public interface Game {
  /**
   * Get all participants there are in game
   *
   * @return participants
   */
  Collection<User> getParticipants();

  /**
   * Get game's state
   *
   * @return state
   */
  GameState getState();

  /**
   * Set game's state
   *
   * @param state to set
   */
  void setState(GameState state);

  /**
   * Get game's state strategy
   *
   * @return strategy
   */
  GameStateStrategy getStateStrategy();

  /**
   * Get chat id where the game takes it's place
   *
   * @return chat id of the game
   */
  Long getChatId();

  /**
   * Get game's unique identifier
   *
   * @return identifier
   */
  UUID getUuid();

  /**
   * Get game's meta
   * @return game meta
   */
  TomMeta getMeta();
}
