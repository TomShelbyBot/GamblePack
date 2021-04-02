package me.theseems.tomshelby.gamblepack.impl;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.GameStateStrategy;
import me.theseems.tomshelby.storage.TomMeta;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;

public class SimpleGame implements Game {
  private final List<User> participants;
  private GameState gameState;
  private final GameStateStrategy stateStrategy;
  private final Long chatId;
  private final UUID uuid;
  private final TomMeta meta;

  public SimpleGame(
      Collection<User> participants,
      GameState gameState,
      GameStateStrategy stateStrategy,
      Long chatId,
      UUID uuid,
      TomMeta meta) {
    this.participants = new ArrayList<>(participants);
    this.gameState = gameState;
    this.stateStrategy = stateStrategy;
    this.chatId = chatId;
    this.uuid = uuid;
    this.meta = meta;
  }

  public SimpleGame(
      Collection<User> participants,
      GameState gameState,
      GameStateStrategy stateStrategy,
      Long chatId,
      TomMeta meta) {
    this.participants = new ArrayList<>(participants);
    this.gameState = gameState;
    this.stateStrategy = stateStrategy;
    this.chatId = chatId;
    this.uuid = GameApi.getGameManager().makeUuid();
    this.meta = meta;
  }

  /**
   * Get all participants there are in game
   *
   * @return participants
   */
  @Override
  public Collection<User> getParticipants() {
    return Collections.unmodifiableList(participants);
  }

  /**
   * Get game's state
   *
   * @return state
   */
  @Override
  public GameState getState() {
    return gameState;
  }

  /**
   * Set game's state
   *
   * @param state to set
   */
  @Override
  public void setState(GameState state) {
    if (state == gameState) return;
    getStateStrategy().handleState(this, state);
    this.gameState = state;
  }

  /**
   * Get game's state strategy
   *
   * @return strategy
   */
  @Override
  public GameStateStrategy getStateStrategy() {
    return stateStrategy;
  }

  /**
   * Get chat id where the game takes it's place
   *
   * @return chat id of the game
   */
  @Override
  public Long getChatId() {
    return chatId;
  }

  /**
   * Get game's unique identifier
   *
   * @return identifier
   */
  @Override
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Get game's meta
   *
   * @return game meta
   */
  @Override
  public TomMeta getMeta() {
    return meta;
  }
}
