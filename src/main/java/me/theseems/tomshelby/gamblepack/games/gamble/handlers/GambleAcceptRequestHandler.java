package me.theseems.tomshelby.gamblepack.games.gamble.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackState;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackStateHandler;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import me.theseems.tomshelby.gamblepack.utils.GambleUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo.GAMBLE_ALL_ACCEPTED;

public class GambleAcceptRequestHandler extends CallbackStateHandler {
  private final GambleInfo info;

  public GambleAcceptRequestHandler(GambleInfo info) {
    this.info = info;
  }

  private void sendAccepted(CallbackState state) {
    BotShortcuts.answer(state.getUpdate(), "Вы подтвердили участие в игре.");
  }

  private void sendAlreadyAccepted(CallbackState state) {
    BotShortcuts.answer(state.getUpdate(), "Вы УЖЕ подтвердили участие.");
  }

  private void sendInvalidQuery(CallbackState state) {
    BotShortcuts.answer(state.getUpdate(), "Невалидный запрос.");
  }

  private void sendNotEveryoneMessage(Set<User> different) {
    StringBuilder builder = new StringBuilder();

    builder.append("Готовы: ");
    info.getAccepted().forEach(user -> builder.append(user.getUserName()).append(" "));
    builder.append("\n");

    builder.append("Еще не отметились: ");
    different.forEach(user -> builder.append(user.getUserName()).append(" "));

    BotShortcuts.edit(
        new EditMessageText()
            .setText(builder.toString())
            .setMessageId(info.getInitial().getMessageId())
            .setReplyMarkup(info.getInitial().getReplyMarkup())
            .setChatId(info.getInitial().getChatId()));
  }

  private void sendReadyMessage() {
    BotShortcuts.edit(
        new EditMessageText()
            .setText("Все готовы к игре в монетку на шелкели: " + info.getAmount())
            .setMessageId(info.getInitial().getMessageId())
            .setChatId(info.getInitial().getChatId()));
  }

  private void sendDeclineMessage(User from) {
    BotShortcuts.edit(
        new EditMessageText()
            .setText("Игрок '" + from.getUserName() + "' отклонил предложение.")
            .setMessageId(info.getInitial().getMessageId())
            .setChatId(info.getInitial().getChatId()));
  }

  private Set<User> calculateDiff(Game game) {
    Set<User> different = new TreeSet<>(Comparator.comparing(User::getId));
    different.addAll(game.getParticipants());
    different.removeAll(info.getAccepted());

    return different;
  }

  @Override
  public void handle(Game game, CallbackState state) {
    if (state.getArgs().length == 0) {
      return;
    }

    User from = state.getUpdate().getCallbackQuery().getFrom();
    String picked = state.getArgs()[state.getArgs().length - 1];

    if (picked.equals("d")) {
      sendDeclineMessage(from);
      game.setState(GameState.END);
      return;
    }

    if (!picked.equals("a")) {
      sendInvalidQuery(state);
      return;
    }

    if (info.getAccepted().stream()
        .map(User::getId)
        .collect(Collectors.toList())
        .contains(from.getId())) {
      sendAlreadyAccepted(state);
      return;
    }

    if (!GambleUtils.checkMoney(info, from)) {
      GambleUtils.sendNotEnoughMoney(from, info, state);
      game.setState(GameState.END);
      return;
    }

    info.getAccepted().add(from);
    sendAccepted(state);

    Set<User> different = calculateDiff(game);
    if (different.size() == 0) {
      sendReadyMessage();
      unregisterCallback(game);
      game.setState(GAMBLE_ALL_ACCEPTED);
    } else {
      sendNotEveryoneMessage(different);
    }
  }
}
