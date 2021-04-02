package me.theseems.tomshelby.gamblepack.impl.callback;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;

import java.util.UUID;

public abstract class CallbackStateHandler implements StateHandler<GameState> {
  private UUID gameUUID;

  public abstract void handle(Game game, CallbackState callbackState);

  @Override
  public void handle(Game game, GameState state) {
    if (!CallbackStateManager.contains(game)) {
      this.gameUUID = game.getUuid();
      CallbackStateManager.add(game, this);
    }
  }

  public UUID getGameUUID() {
    return gameUUID;
  }

  public void unregisterCallback(Game game) {
    CallbackStateManager.remove(game);
  }
}
