package me.theseems.tomshelby.gamblepack.games.coinbattle;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
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
    SendMessage sendMessage = new SendMessage().setChatId(game.getChatId());
    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    markup.setKeyboard(
        Collections.singletonList(
            Arrays.asList(
                new InlineKeyboardButton("Орел")
                    .setCallbackData("gamest#cb#" + game.getUuid() + "#c#h"),
                new InlineKeyboardButton("Решка")
                    .setCallbackData("gamest#cb#" + game.getUuid() + "#c#t"))));

    sendMessage.setReplyToMessageId(info.getMessage().getMessageId());
    sendMessage.setText("Выбирайте что кому должно выпасть");
    sendMessage.setReplyMarkup(markup);
    info.setMessage(Objects.requireNonNull(BotShortcuts.send(sendMessage)));
    game.setState(WAIT_FOR_CHOICE);
  }
}
