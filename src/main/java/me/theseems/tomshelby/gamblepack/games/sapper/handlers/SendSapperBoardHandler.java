package me.theseems.tomshelby.gamblepack.games.sapper.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.games.sapper.SapperInfo;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import me.theseems.tomshelby.gamblepack.utils.SapperUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendSapperBoardHandler implements StateHandler<GameState> {
  private final SapperInfo info;

  public SendSapperBoardHandler(SapperInfo info) {
    this.info = info;
  }

  @Override
  public void handle(Game game, GameState state) {
    info.getBoard().fillPlayers(game.getParticipants());
    info.setMessage(
        BotShortcuts.send(
            SendMessage.builder()
                .text("Сейчас ход: @" + info.getBoard().getCurrentPlayer().getUserName())
                .replyMarkup(SapperUtils.prepareMarkup(game, info.getBoard()))
                .chatId(game.getChatId().toString())
                .build()));

    game.setState(SapperInfo.CELL_CHOICE);
  }
}
