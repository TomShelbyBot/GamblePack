package me.theseems.tomshelby.gamblepack.utils;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.games.tic.TicBoard;
import me.theseems.tomshelby.gamblepack.games.tic.TicCell;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TicUtils {
  public static String getStringPosition(TicCell state) {
    switch (state) {
      default:
        return "UNKNOWN";
      case BLANK:
        return ".";
      case X:
        return "X";
      case O:
        return "O";
    }
  }

  public static InlineKeyboardMarkup prepareMarkup(Game game, TicBoard board) {
    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(board.getSize());

    for (int i = 0; i < board.getSize(); i++) {
      List<InlineKeyboardButton> buttons = new ArrayList<>();
      for (int j = 0; j < board.getSize(); j++) {
        TicCell position = board.getPoint(i, j);
        buttons.add(
            new InlineKeyboardButton()
                .setText(getStringPosition(position))
                .setCallbackData("gamest#tic#" + game.getUuid() + "#pos#" + j + "#" + i));
      }
      keyboard.add(buttons);
    }

    markup.setKeyboard(keyboard);
    return markup;
  }

  public static String getBoardAsciiVisual(TicBoard board) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < board.getSize(); i++) {
      StringBuilder row = new StringBuilder();
      for (int j = 0; j < board.getSize(); j++)
        row.append(getStringPosition(board.getPoint(i, j))).append(" ");

      builder.append(row.toString()).append("\n");
    }

    return builder.toString();
  }
}
