package me.theseems.tomshelby.gamblepack.games.coinbattle;

import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.games.coinbattle.handlers.ChoseWinnerHandler;
import me.theseems.tomshelby.gamblepack.games.coinbattle.handlers.PickSideHandler;
import me.theseems.tomshelby.gamblepack.games.coinbattle.handlers.PrepareSidesHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleAcceptRequestHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleSendRequestHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleSubtractHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleWinHandler;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameStateStrategy;

import java.math.BigDecimal;

public class GambleCoinBattleStrategy extends SimpleGameStateStrategy {
  private final GambleInfo gambleInfo;
  private final CoinBattleInfo coinBattleInfo;

  public GambleCoinBattleStrategy(BigDecimal amount) {
    gambleInfo = new GambleInfo(amount);
    coinBattleInfo = new CoinBattleInfo();

    addStateHandler(new GambleSendRequestHandler(gambleInfo), GameState.START);
    addStateHandler(new GambleAcceptRequestHandler(gambleInfo), GambleInfo.GAMBLE_REQUEST_SENT);
    addStateHandler(new GambleSubtractHandler(gambleInfo), GambleInfo.GAMBLE_ALL_ACCEPTED);
    addStateHandler(new PrepareSidesHandler(coinBattleInfo), GambleInfo.GAMBLE_ALL_WITHDRAWN);
    addStateHandler(new PickSideHandler(coinBattleInfo), CoinBattleInfo.WAIT_FOR_CHOICE);
    addStateHandler(new ChoseWinnerHandler(coinBattleInfo), CoinBattleInfo.ALL_CHOSEN);
    addStateHandler(new GambleWinHandler(gambleInfo), GambleInfo.GAMBLE_WIN);
    addStateHandler(
        (game, state) -> {
          gambleInfo.getWinners().addAll(coinBattleInfo.getWinners());
          game.setState(GambleInfo.GAMBLE_WIN);
        },
        CoinBattleInfo.BATTLE_END);

    addStateHandler(
        (game, state) -> GameApi.getGameManager().unregisterGame(game.getUuid()), GameState.END);
  }

  public CoinBattleInfo getCoinBattleInfo() {
    return coinBattleInfo;
  }

  public GambleInfo getGambleInfo() {
    return gambleInfo;
  }
}
