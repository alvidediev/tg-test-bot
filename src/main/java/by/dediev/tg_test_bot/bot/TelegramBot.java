package by.dediev.tg_test_bot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
@RequiredArgsConstructor
public class TelegramBot extends TelegramWebhookBot {
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.path}")
    private String botPath;


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        // Этот метод должен вернуть ответ Telegram, который библиотека отправит сама
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if ("/start".equals(messageText)) {
                String welcomeMessage = "Привет! Чтобы начать опрос, введи команду /form";
                return new SendMessage(String.valueOf(chatId), welcomeMessage);
            }

            if ("/form".equals(messageText)) {
                String welcomeMessage = "Привет! Чтобы начать опрос, введи команду /form";
                return new SendMessage(String.valueOf(chatId), welcomeMessage);
            }
        }
        // Если мы не хотим ничего отвечать, возвращаем null
        return null;
    }
}
