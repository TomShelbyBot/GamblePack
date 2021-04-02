package me.theseems.tomshelby.gamblepack.api;

import java.util.Collection;

public interface GameStateStrategy {
  /**
   * Get all state handlers there are for specific state
   *
   * @param state to get handlers of
   * @param <T> type of state to get handlers for
   * @return state handlers
   */
  <T extends GameState> Collection<StateHandler<T>> getStateHandlers(T state);

  /**
   * Add state handler
   *
   * @param stateHandler to add
   * @param state to add for
   * @param <T> type of state to add handler of
   */
  <T extends GameState> void addStateHandler(StateHandler<T> stateHandler, GameState state);

  /**
   * Remove state handler from handlers
   *
   * @param stateHandler to remove
   * @param state to remove for
   * @param <T> type of state which handler handles
   */
  <T extends GameState> void removeStateHandler(StateHandler<T> stateHandler, GameState state);

  /**
   * Get all states there are for the strategy
   *
   * @return all possible states
   */
  Collection<GameState> getStates();

  /**
   * Handle state change
   *
   * @param game to handle for
   * @param state to handle
   */
  default void handleState(Game game, GameState state) {
    for (StateHandler<GameState> stateHandler : getStateHandlers(state)) {
      stateHandler.handle(game, state);
    }
  }
}
