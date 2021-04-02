package me.theseems.tomshelby.gamblepack.commands;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.games.GambleCoinBattleStrategy;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameBuilder;
import me.theseems.tomshelby.gamblepack.utils.GambleUtils;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

public class GambleCoinBattleBotCommand extends SimpleBotCommand {
  public GambleCoinBattleBotCommand() {
    super(SimpleCommandMeta.onLabel("gamblecoin").description("Зарубиться на монетка-баттле НА ШЕЛКЕЛИ"));
  }

  @Override
  public void handle(ThomasBot thomasBot, String[] strings, Update update) {
    if (strings.length < 2) {
      thomasBot.replyBackText(update, "Укажите стоимость и противника(-ов)!");
      return;
    }

    BigDecimal amount;
    try {
      amount = new BigDecimal(strings[0]);
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        thomasBot.replyBackText(update, "Укажите положительную сумму");
        return;
      }
    } catch (NumberFormatException e) {
      thomasBot.replyBackText(update, "Укажите валидную сумму ставки");
      return;
    }

    strings = Arrays.stream(strings).skip(1).toArray(String[]::new);
    Pair<Set<User>, Collection<String >> results = GambleUtils.grabUsersFromArgs(thomasBot, update, strings);

    Set<User> users = results.getFirst();
    Collection<String> failed = results.getSecond();

    if (!failed.isEmpty()) {
      thomasBot.replyBackText(
          update, "Следующих участников не удалось найти: " + Joiner.on(", ").join(failed));
    }

    if (users.size() < 2) {
      thomasBot.replyBackText(update, "Не могу начать игру, недостаточно участников");
      return;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, 60);

    Game game =
        new SimpleGameBuilder()
            .chat(update.getMessage().getChatId())
            .participants(users)
            .autoUuid()
            .strategy(new GambleCoinBattleStrategy(amount))
            .buildTimed(calendar.getTime());

    GameApi.getGameManager().registerGame(game);
    game.getStateStrategy().handleState(game, game.getState());
  }
}
