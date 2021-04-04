package me.theseems.tomshelby.gamblepack.games;

import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.games.tic.TicBoard;
import me.theseems.tomshelby.gamblepack.games.tic.TicInfo;
import me.theseems.tomshelby.gamblepack.games.tic.handlers.SendBoardHandler;
import me.theseems.tomshelby.gamblepack.games.tic.handlers.TicBoardHandler;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameStateStrategy;
import org.telegram.telegrambots.meta.api.objects.User;

public class TicStrategy extends SimpleGameStateStrategy {
  public TicStrategy(int size, User userForX, User userForY) {
    TicInfo info = new TicInfo(new TicBoard(size), userForX, userForY);
    addStateHandler(new SendBoardHandler(info), GameState.START);
    addStateHandler(new TicBoardHandler(info), TicInfo.TIC_CHOICE);
    addStateHandler(
        (game, state) -> GameApi.getGameManager().unregisterGame(game.getUuid()), GameState.END);
  }
}
