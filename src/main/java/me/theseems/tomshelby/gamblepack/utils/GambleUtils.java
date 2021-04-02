package me.theseems.tomshelby.gamblepack.utils;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.economypack.EconomyBotPackage;
import me.theseems.tomshelby.economypack.api.EconomyProvider;
import me.theseems.tomshelby.economypack.utils.DragUtils;
import me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackState;
import org.glassfish.grizzly.utils.Pair;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.util.*;

public class GambleUtils {
  private static void editMessageNotEnoughMoney(User user, GambleInfo info) {
    BotShortcuts.edit(
        new EditMessageText()
            .setText("У участника '" + user.getUserName() + "' недостаточно средств для игры.")
            .setChatId(info.getInitial().getChatId())
            .setMessageId(info.getInitial().getMessageId()));
  }

  private static void callbackFeedbackNotEnoughMoney(CallbackState state) {
    BotShortcuts.answer(state.getUpdate(), "Недостаточно денег для участия в игре.");
  }

  public static void sendNotEnoughMoney(User user, GambleInfo info, CallbackState state) {
    editMessageNotEnoughMoney(user, info);
    if (state != null) callbackFeedbackNotEnoughMoney(state);
  }

  public static void sendNotEnoughMoney(User user, GambleInfo info) {
    editMessageNotEnoughMoney(user, info);
  }

  public static Pair<Set<User>, Collection<String>> grabUsersFromArgs(ThomasBot bot, Update update, String[] strings) {
    Set<Integer> participantIds = new HashSet<>();
    List<String> failed = new ArrayList<>();

    participantIds.add(update.getMessage().getFrom().getId());
    for (String string : strings) {
      Optional<Integer> userId = DragUtils.dragUserId(update.getMessage().getChatId(), string);
      if (!userId.isPresent()) {
        failed.add(string);
      } else {
        participantIds.add(userId.get());
      }
    }

    Set<User> users = new TreeSet<>(Comparator.comparing(User::getId));
    participantIds.forEach(
        integer -> {
          try {
            users.add(
                bot
                    .execute(
                        new GetChatMember()
                            .setChatId(update.getMessage().getChatId())
                            .setUserId(integer))
                    .getUser());
          } catch (TelegramApiException e) {
            failed.add(String.valueOf(integer));
          }
        });

    return new Pair<>(users, failed);
  }

  public static boolean checkMoney(GambleInfo info, User user) {
    EconomyProvider provider = EconomyBotPackage.getOrCreate(info.getInitial().getChatId());
    BigDecimal amount = provider.getMoney(user.getId());

    return info.getAmount().compareTo(amount) < 0;
  }
}
