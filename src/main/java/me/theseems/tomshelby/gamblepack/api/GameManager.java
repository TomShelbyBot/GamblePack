package me.theseems.tomshelby.gamblepack.api;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface GameManager {
  /**
   * Register game
   *
   * @param game to register
   */
  void registerGame(Game game);

  /**
   * Unregister game from manager
   *
   * @param uuid to unregister
   */
  void unregisterGame(UUID uuid);

  /**
   * Make uuid for the game
   *
   * @return uuid
   */
  UUID makeUuid();

  /**
   * Get game by it's uuid
   *
   * @param uuid to get for
   * @return game
   */
  Optional<Game> getGame(UUID uuid);

  /**
   * Get game UUIDs
   *
   * @return game uuids
   */
  Collection<UUID> getGameUuids();
}
