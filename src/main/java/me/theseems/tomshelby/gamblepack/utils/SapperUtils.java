package me.theseems.tomshelby.gamblepack.utils;

import me.theseems.tomshelby.gamblepack.api.Game;
import me.theseems.tomshelby.gamblepack.games.sapper.SapperBoard;
import me.theseems.tomshelby.gamblepack.games.sapper.SapperCell;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class SapperUtils {
  public static String getStringCell(SapperCell cell) {
    if (cell.isHidden()) return "❔";

    switch (cell.getType()) {
      default:
        return "<!> " + cell.getType();
      case BLANK:
        return " ";
      case WIN:
        return "\uD83C\uDFC6";
      case BOMB:
        return "\uD83E\uDD2F";
    }
  }

  public static InlineKeyboardMarkup prepareMarkup(Game game, SapperBoard board) {
    List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
    for (int i = 0; i < board.getSize(); i++) {
      List<InlineKeyboardButton> row = new ArrayList<>();
      for (int j = 0; j < board.getSize(); j++) {
        row.add(
            new InlineKeyboardButton()
                .setText(getStringCell(board.get(i, j)))
                .setCallbackData("gamest#sapper#" + game.getUuid() + "#pos#" + i + "#" + j));
      }

      buttons.add(row);
    }

    return new InlineKeyboardMarkup(buttons);
  }
}
