package me.theseems.tomshelby.gamblepack.games.coinbattle.handlers;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.games.coinbattle.CoinBattleInfo;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.theseems.tomshelby.gamblepack.games.coinbattle.CoinBattleInfo.BATTLE_END;

public class ChoseWinnerHandler implements StateHandler<GameState> {
  private final CoinBattleInfo info;

  public ChoseWinnerHandler(CoinBattleInfo info) {
    this.info = info;
  }

  private void editOriginal(String winString) {
    StringBuilder builder = new StringBuilder();
    builder.append("Игра в монетку закончилась. ").append("\n\n");

    info.getUserChoices()
        .forEach(
            (user, choice) ->
                builder
                    .append(user.getUserName())
                    .append(" выбрал(а) ")
                    .append(choice ? "орла" : "решку")
                    .append("\n"));

    builder.append("\n").append(winString);

    BotShortcuts.edit(
        new EditMessageText()
            .setMessageId(info.getMessage().getMessageId())
            .setChatId(info.getMessage().getChatId())
            .setText(builder.toString()));
  }

  @Override
  public void handle(Game game, GameState state) {
    boolean result = CoinBattleInfo.getRandom().nextBoolean();
    List<User> winners = new ArrayList<>();

    info.getUserChoices()
        .forEach(
            (user, userResult) -> {
              if (userResult.equals(result)) winners.add(user);
            });

    String winnerString =
        (winners.isEmpty()
            ? "Никто не угадал..."
            : "Победители: "
                + winners.stream().map(User::getUserName).collect(Collectors.joining(", ")));

    editOriginal("Выпало: " + (result ? "Орел" : "Решка") + "\n" + winnerString);
    info.getWinners().addAll(winners);
    game.setState(BATTLE_END);
  }
}
