package me.theseems.tomshelby.gamblepack.games.tic.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.games.tic.TicCell;
import me.theseems.tomshelby.gamblepack.games.tic.TicExceptions;
import me.theseems.tomshelby.gamblepack.games.tic.TicInfo;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackState;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackStateHandler;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import me.theseems.tomshelby.gamblepack.utils.TicUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;

public class TicBoardHandler extends CallbackStateHandler {
  private TicInfo info;

  public TicBoardHandler(TicInfo info) {
    this.info = info;
  }

  private void sendInvalidQuery(CallbackState state) {
    BotShortcuts.answer(state.getUpdate(), "Невалидный запрос.");
  }

  private void sendDraw() {
    BotShortcuts.send(
        new SendMessage()
            .setText("Вышла ничья!")
            .setChatId(info.getMessage().getChatId())
            .setReplyToMessageId(info.getMessage().getMessageId()));
  }

  private void sendWinner(TicCell cell) {
    User winner = cell == TicCell.X ? info.getUserForX() : info.getUserForY();
    BotShortcuts.send(
        new SendMessage()
            .setText("У нас победитель!\n\nПоздравляем, @" + winner.getUserName())
            .setChatId(info.getMessage().getChatId())
            .setReplyToMessageId(info.getMessage().getMessageId()));
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
      TicCell cell =
          state.getUpdate().getCallbackQuery().getFrom().getId().equals(info.getUserForX().getId())
              ? TicCell.X
              : TicCell.O;

      info.getBoard().makeMove(x, y, cell);

      BotShortcuts.edit(
          new EditMessageText()
              .setText(info.getMessage().getText())
              .setChatId(info.getMessage().getChatId())
              .setMessageId(info.getMessage().getMessageId())
              .setReplyMarkup(TicUtils.prepareMarkup(game, info.getBoard())));
      BotShortcuts.answer(state.getUpdate(), "Ход принят");

      if (info.getBoard().getWinner() != null) {
        unregisterCallback(game);

        if (info.getBoard().getWinner() == TicCell.BLANK) {
          sendDraw();
        } else {
          sendWinner(info.getBoard().getWinner());
        }

        game.setState(GameState.END);
      }

    } catch (TicExceptions.IllegalMoveException e) {
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
          BotShortcuts.answer(state.getUpdate(), "Эта клетка уже занята!");
          break;
      }
    }
  }
}