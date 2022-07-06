package me.theseems.tomshelby.gamblepack.games.coinbattle.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.games.coinbattle.CoinBattleInfo;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static me.theseems.tomshelby.gamblepack.games.coinbattle.CoinBattleInfo.WAIT_FOR_CHOICE;

public class PrepareSidesHandler implements StateHandler<GameState> {
  private final CoinBattleInfo info;

  public PrepareSidesHandler(CoinBattleInfo info) {
    this.info = info;
  }

  @Override
  public void handle(Game game, GameState state) {
    SendMessage sendMessage = new SendMessage();
    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    markup.setKeyboard(
        Collections.singletonList(
            Arrays.asList(
                InlineKeyboardButton.builder()
                    .text("Орел")
                    .callbackData("gamest#cb#" + game.getUuid() + "#c#h")
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Решка")
                    .callbackData("gamest#cb#" + game.getUuid() + "#c#t")
                    .build())));

    sendMessage.setChatId(game.getChatId().toString());
    sendMessage.setReplyMarkup(markup);
    sendMessage.setText("Выбирайте что кому должно выпасть");
    info.setMessage(Objects.requireNonNull(BotShortcuts.send(sendMessage)));
    game.setState(WAIT_FOR_CHOICE);
  }
}
