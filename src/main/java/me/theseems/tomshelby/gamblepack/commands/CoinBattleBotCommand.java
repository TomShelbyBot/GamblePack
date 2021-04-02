package me.theseems.tomshelby.gamblepack.commands;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.games.CoinBattleStrategy;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameBuilder;
import me.theseems.tomshelby.gamblepack.utils.GambleUtils;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

public class CoinBattleBotCommand extends SimpleBotCommand {
  public CoinBattleBotCommand() {
    super(SimpleCommandMeta.onLabel("coinbattle").description("Зарубиться на монетка-баттле"));
  }

  @Override
  public void handle(ThomasBot thomasBot, String[] strings, Update update) {
    if (strings.length == 0) {
      thomasBot.replyBackText(update, "Укажите противника");
      return;
    }

    Pair<Set<User>, Collection<String>> results =
        GambleUtils.grabUsersFromArgs(thomasBot, update, strings);

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
            .strategy(new CoinBattleStrategy())
            .buildTimed(calendar.getTime());

    GameApi.getGameManager().registerGame(game);
    game.getStateStrategy().handleState(game, game.getState());
  }
}
