package me.theseems.tomshelby.gamblepack.impl.callback;

import me.theseems.tomshelby.ThomasBot;
import me.theseems.tomshelby.gamblepack.api.GameState;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class CallbackState extends GameState {
  private final Update update;
  private final String[] args;
  private final ThomasBot thomasBot;

  public CallbackState(Update update, String[] args, ThomasBot thomasBot) {
    super("CALLBACK_STATE");
    this.update = update;
    this.args = args;
    this.thomasBot = thomasBot;
  }

  @Override
  public String toString() {
    return "CallbackState{" +
        "update=" + update +
        ", args=" + Arrays.toString(args) +
        ", thomasBot=" + thomasBot +
        "} " + super.toString();
  }

  public Update getUpdate() {
    return update;
  }

  public String[] getArgs() {
    return args;
  }

  public ThomasBot getThomasBot() {
    return thomasBot;
  }
}
