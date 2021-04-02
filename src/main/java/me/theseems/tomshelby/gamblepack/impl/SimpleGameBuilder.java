package me.theseems.tomshelby.gamblepack.impl;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.GameStateStrategy;
import me.theseems.tomshelby.storage.SimpleTomMeta;
import me.theseems.tomshelby.storage.TomMeta;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;

public class SimpleGameBuilder {
  private final List<User> participants;
  private GameState gameState;
  private GameStateStrategy stateStrategy;
  private Long chatId;
  private UUID uuid;
  private TomMeta meta;

  public SimpleGameBuilder() {
    participants = new ArrayList<>();
    this.gameState = GameState.START;
    this.meta = new SimpleTomMeta();
  }

  public SimpleGameBuilder chat(Long chatId) {
    this.chatId = chatId;
    return this;
  }

  public SimpleGameBuilder state(GameState gameState) {
    this.gameState = gameState;
    return this;
  }

  public SimpleGameBuilder strategy(GameStateStrategy stateStrategy) {
    this.stateStrategy = stateStrategy;
    return this;
  }

  public SimpleGameBuilder participant(User participant) {
    this.participants.add(participant);
    return this;
  }

  public SimpleGameBuilder participants(User... participants) {
    this.participants.addAll(Arrays.asList(participants));
    return this;
  }

  public SimpleGameBuilder participants(Collection<User> participants) {
    this.participants.addAll(participants);
    return this;
  }

  public SimpleGameBuilder autoUuid() {
    this.uuid = GameApi.getGameManager().makeUuid();
    return this;
  }

  public SimpleGameBuilder customUuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }

  public SimpleGameBuilder emptyMeta() {
    this.meta = new SimpleTomMeta();
    return this;
  }

  public SimpleGameBuilder meta(TomMeta meta) {
    this.meta = meta;
    return this;
  }

  public Game build() {
    if (uuid == null) return new SimpleGame(participants, gameState, stateStrategy, chatId, meta);
    else return new SimpleGame(participants, gameState, stateStrategy, chatId, uuid, meta);
  }

  public Game buildTimed(Date until) {
    return SimpleTimedGame.makeTimed(build(), until);
  }
}
