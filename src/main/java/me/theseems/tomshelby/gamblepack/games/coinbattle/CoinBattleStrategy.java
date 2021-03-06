package me.theseems.tomshelby.gamblepack.games.coinbattle;

import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.games.coinbattle.handlers.ChoseWinnerHandler;
import me.theseems.tomshelby.gamblepack.games.coinbattle.handlers.PickSideHandler;
import me.theseems.tomshelby.gamblepack.games.coinbattle.handlers.PrepareSidesHandler;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameStateStrategy;

public class CoinBattleStrategy extends SimpleGameStateStrategy {
  public CoinBattleStrategy() {
    CoinBattleInfo info = new CoinBattleInfo();
    addStateHandler(new PrepareSidesHandler(info), GameState.START);
    addStateHandler(new PickSideHandler(info), CoinBattleInfo.WAIT_FOR_CHOICE);
    addStateHandler(new ChoseWinnerHandler(info), CoinBattleInfo.ALL_CHOSEN);
    addStateHandler((game, state) -> game.setState(GameState.END), CoinBattleInfo.BATTLE_END);
    addStateHandler(
        (game, state) -> GameApi.getGameManager().unregisterGame(game.getUuid()), GameState.END);
  }
}
