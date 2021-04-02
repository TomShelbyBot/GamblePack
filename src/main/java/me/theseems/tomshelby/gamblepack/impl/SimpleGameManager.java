package me.theseems.tomshelby.gamblepack.impl;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleGameManager implements GameManager {
  private final Map<UUID, Game> gameMap;

  public SimpleGameManager() {
    gameMap = new ConcurrentHashMap<>();
  }

  /**
   * Register game
   *
   * @param game to register
   */
  @Override
  public void registerGame(Game game) {
    if (gameMap.containsKey(game.getUuid()))
      throw new IllegalStateException(
          "Game with existing in manager uuid '"
              + game.getUuid()
              + "' was attempted to be registered");

    gameMap.put(game.getUuid(), game);
  }

  /**
   * Unregister game from manager
   *
   * @param uuid to unregister
   */
  @Override
  public void unregisterGame(UUID uuid) {
    gameMap.remove(uuid);
  }

  /**
   * Make uuid for the game
   *
   * @return uuid
   */
  @Override
  public UUID makeUuid() {
    return UUID.randomUUID();
  }

  /**
   * Get game by it's uuid
   *
   * @param uuid to get for
   * @return game
   */
  @Override
  public Optional<Game> getGame(UUID uuid) {
    return Optional.ofNullable(gameMap.get(uuid));
  }

  /**
   * Get game UUIDs
   *
   * @return game uuids
   */
  @Override
  public Collection<UUID> getGameUuids() {
    return gameMap.keySet();
  }
}
