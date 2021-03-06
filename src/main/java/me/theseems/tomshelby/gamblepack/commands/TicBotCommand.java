package me.theseems.tomshelby.gamblepack.commands;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.command.SimpleBotCommand;
import me.theseems.tomshelby.command.SimpleCommandMeta;
import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.games.tic.TicStrategy;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameBuilder;
import me.theseems.tomshelby.gamblepack.utils.GambleUtils;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;
import java.util.stream.Collectors;

public class TicBotCommand extends SimpleBotCommand {
  public TicBotCommand() {
    super(SimpleCommandMeta.onLabel("tic").description("Зарубиться в крестики-нолики"));
  }

  @Override
  public void handle(ThomasBot thomasBot, String[] strings, Update update) {
    if (strings.length == 0) {
      thomasBot.replyBackText(update, "Укажите противника");
      return;
    }

    int size = 3;
    try {
      int newSize = Integer.parseInt(strings[0]);
      if (2 <= newSize && newSize < 6) {
        size = newSize;
      }

      strings =
          Arrays.stream(strings).skip(1).collect(Collectors.toList()).toArray(new String[] {});
    } catch (NumberFormatException ignored) {
    }

    Pair<Set<User>, Collection<String>> results =
        GambleUtils.grabUsersFromArgs(thomasBot, 2, update, strings);

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

    List<User> userList = new ArrayList<>(users);
    Collections.shuffle(userList);

    Game game =
        new SimpleGameBuilder()
            .chat(update.getMessage().getChatId())
            .participants(users)
            .autoUuid()
            .strategy(new TicStrategy(size, userList.get(0), userList.get(1)))
            .buildTimed(calendar.getTime());

    GameApi.getGameManager().registerGame(game);
    game.getStateStrategy().handleState(game, game.getState());
  }
}
