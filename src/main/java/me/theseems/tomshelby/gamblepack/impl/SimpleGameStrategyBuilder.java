package me.theseems.tomshelby.gamblepack.impl;

import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackStateHandler;

public class SimpleGameStrategyBuilder {
  private final SimpleGameStateStrategy strategy;

  public SimpleGameStrategyBuilder() {
    strategy = new SimpleGameStateStrategy();
  }

  public <T extends GameState> SimpleGameStrategyBuilder state(
      String name, StateHandler<T> handler) {
    return state(GameState.name(name), handler);
  }

  public <T extends GameState> SimpleGameStrategyBuilder state(
      GameState state, StateHandler<T> handler) {
    strategy.addStateHandler(handler, state);
    return this;
  }

  public SimpleGameStrategyBuilder start(StateHandler<GameState> handler) {
    strategy.addStateHandler(handler, GameState.START);
    return this;
  }

  public SimpleGameStrategyBuilder end(StateHandler<GameState> handler) {
    strategy.addStateHandler(handler, GameState.END);
    return this;
  }

  public SimpleGameStrategyBuilder callback(GameState state, CallbackStateHandler callbackStateHandler) {
    strategy.addStateHandler(callbackStateHandler, state);
    return this;
  }

  public SimpleGameStrategyBuilder defaultEnd() {
    strategy.addStateHandler(
        (game, state) -> GameApi.getGameManager().unregisterGame(game.getUuid()), GameState.END);
    return this;
  }

  public <T extends GameState> SimpleGameStrategyBuilder startWarpsTo(T nextState) {
    strategy.addStateHandler((game, state) -> game.setState(nextState), GameState.START);
    return this;
  }

  public SimpleGameStateStrategy build() {
    return strategy;
  }
}
