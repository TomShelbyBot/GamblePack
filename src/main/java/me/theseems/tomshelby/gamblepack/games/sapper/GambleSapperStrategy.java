package me.theseems.tomshelby.gamblepack.games.sapper;

import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleAcceptRequestHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleSendRequestHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleSubtractHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.handlers.GambleWinHandler;
import me.theseems.tomshelby.gamblepack.games.sapper.handlers.SapperBoardHandler;
import me.theseems.tomshelby.gamblepack.games.sapper.handlers.SendSapperBoardHandler;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameStateStrategy;

import java.math.BigDecimal;

public class GambleSapperStrategy extends SimpleGameStateStrategy {
  public GambleSapperStrategy(SapperBoard board, BigDecimal amount) {
    GambleInfo gambleInfo = new GambleInfo(amount);
    SapperInfo info = new SapperInfo(board);
    addStateHandler(new GambleSendRequestHandler(gambleInfo), GameState.START);
    addStateHandler(new GambleAcceptRequestHandler(gambleInfo), GambleInfo.GAMBLE_REQUEST_SENT);
    addStateHandler(new GambleSubtractHandler(gambleInfo), GambleInfo.GAMBLE_ALL_ACCEPTED);
    addStateHandler(new SendSapperBoardHandler(info), GambleInfo.GAMBLE_ALL_WITHDRAWN);
    addStateHandler(new SapperBoardHandler(info), SapperInfo.CELL_CHOICE);
    addStateHandler(
        (game, state) -> {
          System.out.println("CELL END");
          gambleInfo.getWinners().addAll(info.getWinners());
          game.setState(GambleInfo.GAMBLE_WIN);
        },
        SapperInfo.CELL_END);
    addStateHandler(new GambleWinHandler(gambleInfo), GambleInfo.GAMBLE_WIN);

    addStateHandler(
        (game, state) -> GameApi.getGameManager().unregisterGame(game.getUuid()), GameState.END);
  }
}
