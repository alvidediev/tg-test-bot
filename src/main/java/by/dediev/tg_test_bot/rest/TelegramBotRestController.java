package by.dediev.tg_test_bot.rest;

import by.dediev.tg_test_bot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TelegramBotRestController {

    private final TelegramBot telegramBot;

    @PostMapping
    public Mono<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookReceived(update);
    }
}
