package me.theseems.tomshelby.gamblepack.games.coinbattle;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackState;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackStateHandler;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;

import static me.theseems.tomshelby.gamblepack.games.coinbattle.CoinBattleInfo.ALL_CHOSEN;

public class PickSideHandler extends CallbackStateHandler {
  private final CoinBattleInfo info;

  public PickSideHandler(CoinBattleInfo info) {
    this.info = info;
  }

  private void sendDecline(CallbackState state) {
    BotShortcuts.answer(state.getUpdate(), "Невалидный запрос", true);
  }

  @Override
  public void handle(Game game, CallbackState state) {
    if (state.getArgs().length < 3) {
      sendDecline(state);
      return;
    }

    User from = state.getUpdate().getCallbackQuery().getFrom();
    String picked = state.getArgs()[state.getArgs().length - 1];

    if (!picked.equals("h") && !picked.equals("t")) {
      sendDecline(state);
      return;
    }

    if (info.getUserChoices().containsKey(from)) {
      BotShortcuts.answer(state.getUpdate(), "Вы уже выбрали сторону монетки", true);
      return;
    }

    info.getUserChoices().put(from, picked.equals("h"));

    StringBuilder builder = new StringBuilder();
    info.getUserChoices().forEach(
        (user, result) ->
            builder
                .append(user.getUserName())
                .append(" - выбрал какую-то из сторон")
                .append("\n"));

    BotShortcuts.edit(
        new EditMessageText()
            .setChatId(game.getChatId())
            .setMessageId(info.getMessage().getMessageId())
            .setText(builder.toString())
            .setReplyMarkup(info.getMessage().getReplyMarkup()));

    BotShortcuts.answer(
        state.getUpdate(), "Вы выбрали " + (picked.equals("h") ? "орла" : "решку"));

    boolean ready =
        game.getParticipants().stream()
            .allMatch(
                participantId ->
                    info.getUserChoices().keySet().stream()
                        .anyMatch(user -> user.equals(participantId)));

    if (ready) {
      game.setState(ALL_CHOSEN);
      unregisterCallback(game);
    }
  }
}
