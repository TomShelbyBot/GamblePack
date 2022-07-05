package me.theseems.tomshelby.gamblepack.games.sapper.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.games.sapper.SapperCell;
import me.theseems.tomshelby.gamblepack.games.sapper.SapperInfo;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackState;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackStateHandler;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import me.theseems.tomshelby.gamblepack.utils.GameExceptions;
import me.theseems.tomshelby.gamblepack.utils.SapperUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SapperBoardHandler extends CallbackStateHandler {
  private final SapperInfo info;

  public SapperBoardHandler(SapperInfo info) {
    this.info = info;
  }

  private void sendInvalidQuery(CallbackState state) {
    BotShortcuts.answer(state.getUpdate(), "Невалидный запрос.");
  }

  @Override
  public void handle(Game game, CallbackState state) {
    if (state.getArgs().length < 5 || !state.getArgs()[2].equals("pos")) {
      sendInvalidQuery(state);
      return;
    }

    String rawX = state.getArgs()[3];
    String rawY = state.getArgs()[4];

    int x, y;
    try {
      x = Integer.parseInt(rawX);
      y = Integer.parseInt(rawY);
    } catch (NumberFormatException e) {
      sendInvalidQuery(state);
      return;
    }

    try {
      SapperCell cell = info.getBoard().move(x, y, state.getUpdate().getCallbackQuery().getFrom());

      if (cell.getType().equals(SapperCell.Type.BLANK)) {
        BotShortcuts.answer(state.getUpdate(), "Пусто.. пронесло)");
        BotShortcuts.edit(
            new EditMessageText()
                .setText("Сейчас ход: @" + info.getBoard().getCurrentPlayer().getUserName())
                .setChatId(info.getMessage().getChatId())
                .setMessageId(info.getMessage().getMessageId())
                .setReplyMarkup(SapperUtils.prepareMarkup(game, info.getBoard())));
        return;
      }

      unregisterCallback(game);
      Set<User> winners = new TreeSet<>(Comparator.comparing(User::getId));
      User from = state.getUpdate().getCallbackQuery().getFrom();

      String reason;
      if (cell.getType() == SapperCell.Type.BOMB) {
        winners.addAll(game.getParticipants());
        winners.remove(from);
        BotShortcuts.answer(state.getUpdate(), "БОМБА!");
        reason = from.getUserName() + " вытащил бомбу";
      } else {
        winners.add(state.getUpdate().getCallbackQuery().getFrom());
        reason = from.getUserName() + " вытащил победу!";
      }

      for (int i = 0; i < info.getBoard().getSize(); i++) {
        for (int j = 0; j < info.getBoard().getSize(); j++) {
          info.getBoard().get(i, j).setHidden(false);
        }
      }

      BotShortcuts.edit(
          new EditMessageText()
              .setText(reason)
              .setChatId(info.getMessage().getChatId())
              .setMessageId(info.getMessage().getMessageId())
              .setReplyMarkup(SapperUtils.prepareMarkup(game, info.getBoard())));

      info.setWinners(winners);
      game.setState(SapperInfo.CELL_END);

    } catch (GameExceptions.IllegalMoveException e) {
      switch (e.getReason()) {
        case GAME_ENDED:
          BotShortcuts.answer(state.getUpdate(), "У нас уже есть победитель!");
          break;
        case OUT_OF_TURN:
          BotShortcuts.answer(state.getUpdate(), "Не Ваш ход!");
          break;
        case OUT_OF_BORDERS:
          sendInvalidQuery(state);
          break;
        case ALREADY_PLACED:
          BotShortcuts.answer(state.getUpdate(), "Эта клетка уже открыта!");
          break;
      }
    }
  }
}
