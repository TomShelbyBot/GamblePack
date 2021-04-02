package me.theseems.tomshelby.gamblepack.games.gamble;

import com.google.common.base.Joiner;
import me.theseems.tomshelby.economypack.EconomyBotPackage;
import me.theseems.tomshelby.economypack.api.EconomyProvider;
import me.theseems.tomshelby.economypack.impl.types.SimpleDepositTransaction;
import me.theseems.tomshelby.gamblepack.GambleBotPackage;
import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.api.StateHandler;
import me.theseems.tomshelby.gamblepack.utils.BotShortcuts;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.stream.Collectors;

public class GambleWinHandler implements StateHandler<GameState> {
  private final GambleInfo info;

  public GambleWinHandler(GambleInfo info) {
    this.info = info;
  }

  private void sendMoney(EconomyProvider provider, Collection<User> users, BigDecimal amount) {
    for (User winner : users) {
      provider
          .getTransactionExecutor()
          .execute(
              new SimpleDepositTransaction(provider, amount, winner.getId()),
              transaction -> {
                SendMessage sendMessage =
                    new SendMessage()
                        .setChatId(info.getInitial().getChatId())
                        .setText(
                            "Зачислено +"
                                + DecimalFormat.getNumberInstance().format(amount)
                                + " на счет @"
                                + winner.getUserName());

                BotShortcuts.sendAsync(sendMessage);
              });
    }
  }

  private void logWinners(Game game) {
    GambleBotPackage.getPackLogger()
        .info(
            "[GAME "
                + game.getUuid()
                + "] Winners: "
                + Joiner.on(' ')
                    .join(
                        info.getWinners().stream()
                            .map(User::getUserName)
                            .collect(Collectors.toList())));
  }

  @Override
  public void handle(Game game, GameState state) {
    logWinners(game);
    EconomyProvider provider = EconomyBotPackage.getOrCreate(info.getInitial().getChatId());

    if (info.getWinners().size() == 0) sendMoney(provider, info.getAccepted(), info.getAmount());
    else {
      BigDecimal winAmount =
          info.getAmount()
              .multiply(BigDecimal.valueOf(info.getAccepted().size()))
              .setScale(15, RoundingMode.HALF_DOWN)
              .divide(
                  BigDecimal.valueOf(info.getWinners().size()).setScale(15, RoundingMode.HALF_DOWN),
                  RoundingMode.HALF_DOWN);
      sendMoney(provider, info.getWinners(), winAmount);
    }

    game.setState(GameState.END);
  }
}
