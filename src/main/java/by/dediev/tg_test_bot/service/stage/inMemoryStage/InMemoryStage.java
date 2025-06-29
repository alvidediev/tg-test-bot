package by.dediev.tg_test_bot.service.stage.inMemoryStage;

import by.dediev.tg_test_bot.model.enums.CurrentStateEnum;
import by.dediev.tg_test_bot.model.enums.UserState;

/**
 * Сервис управление состояние заполненности данных из /form.
 * Для хранения данных используется ConcurrentHashMap так как у нас неблокуирующий стек
 */
public interface InMemoryStage {
    /**
     * Создания состояния
     */
    void createState(Long chatId, UserState state);

    /**
     * Обновление состояния
     */
    void updateState(Long chatId, CurrentStateEnum newState);

    /**
     * Получение состояния
     */
    UserState getState(Long chatId);

    /**
     *  Ресеттит состояние
     */
    void resetState(Long chatId);
}
