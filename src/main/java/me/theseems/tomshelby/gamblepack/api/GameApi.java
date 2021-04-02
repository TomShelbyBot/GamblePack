package me.theseems.tomshelby.gamblepack.api;

public class GameApi {
  private static GameManager gameManager;

  public GameApi() {}

  public static GameManager getGameManager() {
    return gameManager;
  }

  public static void setGameManager(GameManager gameManager) {
    GameApi.gameManager = gameManager;
  }
}
