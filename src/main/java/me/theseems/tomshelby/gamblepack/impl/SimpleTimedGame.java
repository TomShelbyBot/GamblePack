package me.theseems.tomshelby.gamblepack.impl;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.GameStateStrategy;
import me.theseems.tomshelby.gamblepack.api.TimedGame;
import me.theseems.tomshelby.storage.TomMeta;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class SimpleTimedGame extends SimpleGame implements TimedGame {
  private final Date until;

  public SimpleTimedGame(
      Collection<User> participants,
      GameState gameState,
      GameStateStrategy stateStrategy,
      Long chatId,
      UUID uuid,
      TomMeta meta,
      Date until) {
    super(participants, gameState, stateStrategy, chatId, uuid, meta);
    this.until = until;
  }

  public static SimpleTimedGame makeTimed(Game simpleGame, Date until) {
    return new SimpleTimedGame(
        simpleGame.getParticipants(),
        simpleGame.getState(),
        simpleGame.getStateStrategy(),
        simpleGame.getChatId(),
        simpleGame.getUuid(),
        simpleGame.getMeta(),
        until);
  }

  /**
   * Get game's end date
   *
   * @return until
   */
  @Override
  public Date getUntil() {
    return until;
  }
}
