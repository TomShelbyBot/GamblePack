package me.theseems.tomshelby.gamblepack.games.tic;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.api.GameState;
import me.theseems.tomshelby.gamblepack.utils.TicUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class TicInfo {
  private final TicBoard board;
  private Message message;
  private final User userForX;
  private final User userForY;

  public static GameState TIC_CHOICE = new GameState("tic_choice");

  public TicInfo(TicBoard board, User userForX, User userForY) {
    this.board = board;
    this.userForX = userForX;
    this.userForY = userForY;
  }

  public TicBoard getBoard() {
    return board;
  }

  public InlineKeyboardMarkup getMarkup(Game game) {
    return TicUtils.prepareMarkup(game, board);
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public User getUserForX() {
    return userForX;
  }

  public User getUserForY() {
    return userForY;
  }
}
