package by.dediev.tg_test_bot.service.telegramDocumentSender;

import reactor.core.publisher.Mono;

/**
 * Сервис отправки пользователю документа (отчета)
 */
public interface TelegramDocumentSender {
    Mono<Void> sendDocument(Long chatId, byte[] docxBytes);
}
