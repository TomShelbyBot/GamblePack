package me.theseems.tomshelby.gamblepack.games.gamble;

import me.theseems.tomshelby.economypack.EconomyBotPackage;
import me.theseems.tomshelby.economypack.api.EconomyProvider;
import me.theseems.tomshelby.economypack.api.transaction.TransactionStatus;
import me.theseems.tomshelby.economypack.impl.types.SimpleDepositTransaction;
import me.theseems.tomshelby.economypack.impl.types.SimpleWithdrawTransaction;
import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import me.theseems.tomshelby.gamblepack.utils.GambleUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.theseems.tomshelby.gamblepack.games.gamble.GambleInfo.GAMBLE_ALL_WITHDRAWN;

public class GambleSubtractHandler implements StateHandler<GameState> {
  private final GambleInfo info;

  public GambleSubtractHandler(GambleInfo info) {
    this.info = info;
  }

  @Override
  public void handle(Game game, GameState state) {
    EconomyProvider provider = EconomyBotPackage.getOrCreate(info.getInitial().getChatId());
    for (User user : info.getAccepted()) {
      BigDecimal amount = provider.getMoney(user.getId());
      if (amount.compareTo(info.getAmount()) < 0) {
        GambleUtils.sendNotEnoughMoney(user, info);
        return;
      }
    }

    Set<User> succeed = new TreeSet<>(Comparator.comparing(User::getId));
    AtomicBoolean next = new AtomicBoolean(true);
    for (User user : info.getAccepted()) {
      if (!next.get()) break;
      provider
          .getTransactionExecutor()
          .execute(
              new SimpleWithdrawTransaction(provider, info.getAmount(), user.getId()),
              simpleWithdrawTransaction -> {
                if (simpleWithdrawTransaction.getStatus() != TransactionStatus.SUCCEEDED) {
                  BotShortcuts.edit(
                      new EditMessageText()
                          .setText(
                              "У участника '"
                                  + user.getUserName()
                                  + "' не удалось списать средства. Завершаем игру и возвращаем всё.")
                          .setChatId(info.getInitial().getChatId())
                          .setMessageId(info.getInitial().getMessageId()));

                  next.set(false);
                  game.setState(GameState.END);
                } else {
                  succeed.add(user);
                }
              });
    }

    if (!next.get()) {
      succeed.forEach(
          user ->
              provider
                  .getTransactionExecutor()
                  .execute(
                      new SimpleDepositTransaction(provider, info.getAmount(), user.getId()),
                      transaction -> {}));
      return;
    }

    game.setState(GAMBLE_ALL_WITHDRAWN);
  }
}
