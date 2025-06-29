package by.dediev.tg_test_bot.bot;

import by.dediev.tg_test_bot.entity.TempDataEntity;
import by.dediev.tg_test_bot.mapper.TempDataMapper;
import by.dediev.tg_test_bot.mapper.UserStateMapper;
import by.dediev.tg_test_bot.model.enums.CurrentStateEnum;
import by.dediev.tg_test_bot.model.enums.UserState;
import by.dediev.tg_test_bot.repository.reportRepository.TempDataRepository;
import by.dediev.tg_test_bot.repository.usersRepository.UserRepository;
import by.dediev.tg_test_bot.service.reportService.ReportService;
import by.dediev.tg_test_bot.service.stage.inMemoryStage.InMemoryStage;
import by.dediev.tg_test_bot.service.telegramDocumentSender.TelegramDocumentSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

@Component
@Getter
@Slf4j
@RequiredArgsConstructor
public class TelegramBot {
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.path}")
    private String botPath;
    private final InMemoryStage inMemoryStage;

    private final TempDataRepository tempDataRepository;
    private final UserRepository userRepository;
    private final ReportService reportService;
    private final TelegramDocumentSender  telegramDocumentSender;

    public Mono<?> onWebhookReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            UserState userState = inMemoryStage.getState(chatId);

            // Обработка команды /start
            if ("/start".equals(messageText)) {
                inMemoryStage.resetState(chatId);
                return userRepository.existsByChatId(chatId)
                        .flatMap(isExists -> {
                            if (isExists) {
                                return Mono.just(new SendMessage(String.valueOf(chatId), "Привет! Чтобы начать опрос, введи команду /form"));
                            }
                            return userRepository.save(UserStateMapper.mapUserState(userState))
                                    .map(entity -> new SendMessage(String.valueOf(chatId), "Привет! Чтобы начать опрос, введи команду /form"))
                                    .doOnError(err -> log.error("Error while saving user! Details: {}", err.getMessage()));
                        });
            }

            // Обработка команды /form
            if ("/form".equals(messageText)) {
                inMemoryStage.updateState(chatId, CurrentStateEnum.AWAITING_NAME);
                return Mono.just(new SendMessage(String.valueOf(chatId), "Введите, пожалуйста, имя:"));
            }

            // Обработка команды /report
            if ("/report".equals(messageText)) {
                return tempDataRepository.findAll()
                        .collectList()
                        .flatMap(reportService::generateReport)
                        .flatMap(bytes -> telegramDocumentSender.sendDocument(chatId ,bytes))
                        .doOnError(err -> log.error("Error while saving report! Details: {}", err.getMessage()));
            }

            // Обработка ввода имени
            if (inMemoryStage.getState(chatId).getCurrentState().equals(CurrentStateEnum.AWAITING_NAME)) {
                userState.getTempData().setName(messageText);
                inMemoryStage.updateState(chatId, CurrentStateEnum.AWAITING_EMAIL);
                return Mono.just(new SendMessage(String.valueOf(chatId), "Теперь введите ваш email:"));
            }

            // Обработка ввода email
            if (userState != null && userState.getCurrentState() == CurrentStateEnum.AWAITING_EMAIL) {
                if (isValidEmail(messageText)) {
                    userState.getTempData().setEmail(messageText);
                    inMemoryStage.updateState(chatId, CurrentStateEnum.AWAITING_SCORE);
                    return Mono.just(new SendMessage(String.valueOf(chatId), "Оцените бота от 1 до 10:"));
                } else {
                    return Mono.just(new SendMessage(String.valueOf(chatId), "Некорректный email. Попробуйте ещё раз:"));
                }
            }

            // Обработка оценки
            if (userState != null && userState.getCurrentState() == CurrentStateEnum.AWAITING_SCORE) {
                try {
                    int score = Integer.parseInt(messageText);
                    if (score >= 1 && score <= 10) {
                        userState.getTempData().setScore(score);

                        inMemoryStage.resetState(chatId);
                        return userRepository.findByChatId(chatId)
                                .flatMap(userEntity -> {
                                    TempDataEntity tempDataEntity = TempDataMapper.tampDataMapper(userState.getTempData());
                                    tempDataEntity.setUserId(userEntity.getId());
                                    return tempDataRepository.save(tempDataEntity)
                                            .flatMap(entity -> Mono.just(new SendMessage(String.valueOf(chatId), "Спасибо! Данные сохранены.")))
                                            .onErrorResume(throwable -> {
                                                log.error("""
                                                        Error while trying to save temp data! Details: {},
                                                        """, throwable.getMessage());
                                                inMemoryStage.resetState(chatId);
                                                return Mono.just(new SendMessage(String.valueOf(chatId),
                                                        """
                                                                Что-то пошло не так...пожалуйста, повторите попытку!
                                                                """));
                                            });
                                });

                    }
                } catch (NumberFormatException e) {
                    return Mono.just(new SendMessage(String.valueOf(chatId), "Пожалуйста, введите число от 1 до 10:"));
                }
            }
        }
        return null;
    }

    /**
     * Простейшая валидация через регулярные выражения. Для тестового не увидел смысла подключать библиотеки
     *
     * @param email - эл почта
     * @return - логическое выражение соответствия эл почты
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
