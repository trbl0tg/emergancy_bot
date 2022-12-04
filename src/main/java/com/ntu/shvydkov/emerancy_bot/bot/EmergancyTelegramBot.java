package com.ntu.shvydkov.emerancy_bot.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
@Component
public class EmergancyTelegramBot extends TelegramLongPollingBot {

    @Getter
    String botUsername = "ntu_emergancy_bot";

    @Getter
    String botToken = "5818457683:AAFmTcqYrT9sOxdBnndSeml6DOFpyfpIlmA";

    private final UpdateReceiver updateReceiver;

    public EmergancyTelegramBot(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    @Override
    public void onUpdateReceived(Update update) {
        PartialBotApiMethod<? extends Serializable> responseToUser = updateReceiver.handleUpdate(update, this);

        if (responseToUser instanceof SendDocument) {
            try {
                execute((SendDocument) responseToUser);
            } catch (TelegramApiException e) {
                log.error("Error occurred while sending message to user: {}", e.getMessage());
            }
        }

        if (responseToUser instanceof SendLocation) {
            try {
                execute((SendLocation) responseToUser);
            } catch (TelegramApiException e) {
                log.error("Error occurred while sending location to user: {}", e.getMessage());
            }
        }

        if (responseToUser instanceof SendPhoto) {
            try {
                execute((SendPhoto) responseToUser);
            } catch (TelegramApiException e) {
                log.error("Error occurred while sending message to user: {}", e.getMessage());
            }
        }

        if (responseToUser instanceof BotApiMethod) {
            try {
                execute((BotApiMethod<? extends Serializable>) responseToUser);
            } catch (Exception e) {
                log.error("Error occurred while sending message to user: {}", e.getMessage());
            }
        }
    }
}