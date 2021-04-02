package me.theseems.tomshelby.gamblepack.impl.callback;

import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.callback.CallbackHandler;
import me.theseems.tomshelby.gamblepack.GambleBotPackage;
import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CallbackStateManager implements CallbackHandler {
  private static final Map<Game, CallbackStateHandler> waiters = new ConcurrentHashMap<>();

  public CallbackStateManager() {
    Main.getBot().getCallbackManager().addCallbackHandler(this);
  }

  private Optional<UUID> parseUUID(String string) {
    try {
      return Optional.of(UUID.fromString(string));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  @Override
  public void onCallback(ThomasBot thomasBot, Update update, String[] strings) {
    Optional<UUID> optionalUUID = parseUUID(strings[1]);
    if (!optionalUUID.isPresent()) {
      BotShortcuts.answer(update, "Невалидный запрос");
      return;
    }

    Optional<Game> optionalGame = GameApi.getGameManager().getGame(optionalUUID.get());
    if (!optionalGame.isPresent()) {
      BotShortcuts.answer(update, "Эта игра уже закончилась");
      return;
    }

    Game game = optionalGame.get();
    if (!waiters.containsKey(game)) {
      BotShortcuts.answer(update, "Игра не поддерживает данный вид взаимодействия");
      return;
    }

    if (!game.getParticipants().stream()
        .map(User::getId)
        .collect(Collectors.toList())
        .contains(update.getCallbackQuery().getFrom().getId())) {
      BotShortcuts.answer(update, "Вы не участвуете в этой игре");
      GambleBotPackage.getPackLogger()
          .info(
              "No participant in: "
                  + game.getParticipants()
                  + " for "
                  + update.getCallbackQuery().getFrom());
      return;
    }

    waiters.get(game).handle(game, new CallbackState(update, strings, thomasBot));
  }

  public static boolean contains(Game game) {
    return waiters.containsKey(game);
  }

  public static void add(Game game, CallbackStateHandler stateHandler) {
    waiters.put(game, stateHandler);
  }

  public static void remove(Game game) {
    waiters.remove(game);
  }

  @Override
  public String getName() {
    return "gamest";
  }

  @Override
  public String getPrefix() {
    return "gamest";
  }
}
