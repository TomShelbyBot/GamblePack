package me.theseems.tomshelby.gamblepack.games.tic.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.games.tic.TicInfo;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import me.theseems.tomshelby.gamblepack.utils.TicUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendBoardHandler implements StateHandler<GameState> {
  private TicInfo info;

  public SendBoardHandler(TicInfo info) {
    this.info = info;
  }

  @Override
  public void handle(Game game, GameState state) {
    info.setMessage(
        BotShortcuts.send(
            new SendMessage()
                .setText(
                    "Поехали!\n\n"
                        + info.getUserForX().getUserName()
                        + " играет крестиками\n"
                        + info.getUserForY().getUserName()
                        + " играет ноликами")
                .setReplyMarkup(TicUtils.prepareMarkup(game, info.getBoard()))
                .setChatId(game.getChatId())));

    game.setState(TicInfo.TIC_CHOICE);
  }
}
