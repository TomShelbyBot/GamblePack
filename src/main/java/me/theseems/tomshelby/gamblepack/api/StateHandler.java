package me.theseems.tomshelby.gamblepack.api;

public interface StateHandler<T extends GameState> {
  void handle(Game game, T state);
}
