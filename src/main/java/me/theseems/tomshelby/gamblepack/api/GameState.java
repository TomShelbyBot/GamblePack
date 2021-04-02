package me.theseems.tomshelby.gamblepack.api;

import java.util.Objects;

public class GameState {
  public static final GameState START = new GameState("START");
  public static final GameState END = new GameState("END");

  public static GameState name(String name) {
    return new GameState(name);
  }

  private final String name;

  public GameState(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "GameState{" +
        "name='" + name + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameState gameState = (GameState) o;
    return name.equals(gameState.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
