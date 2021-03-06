package me.theseems.tomshelby.gamblepack.games.gamble.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import me.theseems.tomshelby.gamblepack.utils.GambleUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo.GAMBLE_REQUEST_SENT;

public class GambleSendRequestHandler implements StateHandler<GameState> {
  private final GambleInfo info;

  public GambleSendRequestHandler(GambleInfo info) {
    this.info = info;
  }

  @Override
  public void handle(Game game, GameState state) {
    SendMessage sendMessage =
        SendMessage.builder()
            .chatId(game.getChatId().toString())
            .text("Играем на шелкили: **" + info.getAmount() + "**")
            .build();

    sendMessage.enableMarkdown(true);

    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    markup.setKeyboard(
        Collections.singletonList(
            Arrays.asList(
                InlineKeyboardButton.builder()
                    .text("Принять")
                    .callbackData("gamest#gmb#" + game.getUuid() + "#a")
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Откзаться")
                    .callbackData("gamest#gmb#" + game.getUuid() + "#d")
                    .build())));

    sendMessage.setReplyMarkup(markup);
    info.setInitial(Objects.requireNonNull(BotShortcuts.send(sendMessage)));

    for (User user : game.getParticipants()) {
      if (!GambleUtils.checkMoney(info, user)) {
        GambleUtils.sendNotEnoughMoney(user, info);
        game.setState(GameState.END);
        return;
      }
    }

    game.setState(GAMBLE_REQUEST_SENT);
  }
}
