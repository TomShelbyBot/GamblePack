package me.theseems.tomshelby.gamblepack.utils;

import me.theseems.tomshelby.Main;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

public class BotShortcuts {
  public static Message send(SendMessage message) {
    try {
      return Main.getBot().execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void sendAsync(SendMessage message) {
    try {
      Main.getBot()
          .executeAsync(
              message,
              new SentCallback<Message>() {
                @Override
                public void onResult(BotApiMethod<Message> botApiMethod, Message message) {}

                @Override
                public void onError(
                    BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {}

                @Override
                public void onException(BotApiMethod<Message> botApiMethod, Exception e) {}
              });
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public static void answer(Update update, String message, boolean alert) {
    try {
      Main.getBot()
          .execute(
              AnswerCallbackQuery.builder()
                  .callbackQueryId(update.getCallbackQuery().getId())
                  .text(message)
                  .showAlert(alert)
                  .build());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public static void answer(Update update, String message) {
    answer(update, message, false);
  }

  public static void edit(EditMessageText editMessageText) {
    try {
      Main.getBot().execute(editMessageText);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
