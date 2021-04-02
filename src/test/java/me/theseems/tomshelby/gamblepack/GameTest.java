package me.theseems.tomshelby.gamblepack;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameBuilder;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameManager;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameStateStrategy;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameStrategyBuilder;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GameTest {
  public void initialize() {
    GameApi.setGameManager(new SimpleGameManager());
  }

  private static class CustomState extends GameState {
    private String someData;

    public CustomState(String someData) {
      super("custom");
      this.someData = someData;
    }

    public String getSomeData() {
      return someData;
    }

    public void setSomeData(String someData) {
      this.someData = someData;
    }

    @Override
    public String toString() {
      return "CustomState{" + "someData='" + someData + '\'' + "} " + super.toString();
    }
  }

  private void send(SendMessage message) {
    try {
      Main.getBot().execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void game_simpleTest() {
    initialize();

    SimpleGameStateStrategy strategy =
        new SimpleGameStrategyBuilder()
            .startWarpsTo(new CustomState("aa"))
            .state(
                "custom",
                (StateHandler<CustomState>)
                    (game, state) -> {
                      System.out.println("State1 " + state.someData);
                      game.setState(GameState.name("state2"));
                    })
            .state(
                "state2",
                (game, state) -> {
                  System.out.println("State2");
                  game.setState(GameState.END);
                })
            .defaultEnd()
            .build();

    Game game = new SimpleGameBuilder().strategy(strategy).build();

    game.getStateStrategy().handleState(game, GameState.START);
  }
}
