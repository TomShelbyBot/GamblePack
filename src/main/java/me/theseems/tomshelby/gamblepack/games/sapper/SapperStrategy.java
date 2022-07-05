package me.theseems.tomshelby.gamblepack.games.sapper;

import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.games.sapper.handlers.SapperBoardHandler;
import me.theseems.tomshelby.gamblepack.games.sapper.handlers.SendSapperBoardHandler;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameStateStrategy;

public class SapperStrategy extends SimpleGameStateStrategy {
  public SapperStrategy(SapperBoard board) {
    SapperInfo info = new SapperInfo(board);
    addStateHandler(new SendSapperBoardHandler(info), GameState.START);
    addStateHandler(new SapperBoardHandler(info), SapperInfo.CELL_CHOICE);
    addStateHandler((game, state) -> game.setState(GameState.END), SapperInfo.CELL_END);
    addStateHandler(
        (game, state) -> GameApi.getGameManager().unregisterGame(game.getUuid()), GameState.END);
  }
}
