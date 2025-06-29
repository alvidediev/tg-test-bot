package by.dediev.tg_test_bot.service.telegramDocumentSender.impl;

import by.dediev.tg_test_bot.service.telegramDocumentSender.TelegramDocumentSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramDocumentSenderImpl implements TelegramDocumentSender {
    @Value("${telegram.bot.token}")
    private String botToken;

    private final WebClient webClient = WebClient.create();

    @Override
    public Mono<Void> sendDocument(Long chatId, byte[] docxBytes) {
        return webClient.post()
                .uri("https://api.telegram.org/bot{token}/sendDocument", botToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("chat_id", String.valueOf(chatId))
                        .with("document", new ByteArrayResource(docxBytes) {
                            @Override
                            public String getFilename() {
                                return "report.docx";
                            }
                        }))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(resp -> log.info("Document sent: {}", resp))
                .then();
    }
}
