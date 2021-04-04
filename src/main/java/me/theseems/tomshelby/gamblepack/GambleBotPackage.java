package me.theseems.tomshelby.gamblepack;

import me.theseems.tomshelby.gamblepack.api.GameApi;
import me.theseems.tomshelby.gamblepack.commands.CoinBattleBotCommand;
import me.theseems.tomshelby.gamblepack.commands.GambleCoinBattleBotCommand;
import me.theseems.tomshelby.gamblepack.commands.TicBotCommand;
import me.theseems.tomshelby.gamblepack.impl.SimpleGameManager;
import me.theseems.tomshelby.gamblepack.impl.callback.CallbackStateManager;
import me.theseems.tomshelby.pack.JavaBotPackage;
import org.slf4j.Logger;

public class GambleBotPackage extends JavaBotPackage {
  private static Logger logger;

  @Override
  public void onEnable() {
    new CallbackStateManager();
    GameApi.setGameManager(new SimpleGameManager());
    getBot().getCommandContainer().attach(new CoinBattleBotCommand());
    getBot().getCommandContainer().attach(new GambleCoinBattleBotCommand());
    getBot().getCommandContainer().attach(new TicBotCommand());

    logger = this.getLogger();
  }

  @Override
  public void onDisable() {
    getBot().getCommandContainer().detach("coinbattle");
    getBot().getCommandContainer().detach("gamblecoin");
    getBot().getCommandContainer().detach("tic");
  }

  public static Logger getPackLogger() {
    return logger;
  }
}
