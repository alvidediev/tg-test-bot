package by.dediev.tg_test_bot.repository.usersRepository;

import by.dediev.tg_test_bot.entity.UserEntity;
import reactor.core.publisher.Mono;

/**
 * Репозиторный слой доступа к бд. Транзакциями управляет TransactionalOperator.
 */
public interface UserRepository {
    /**
     * Transactional; Сохранение в базу.
     */
    Mono<UserEntity> save(UserEntity userEntity);

    /**
     * Получение пользователя по chatId
     */
    Mono<UserEntity> findByChatId(Long chatId);

    /**
     * Существует ли в базе пользователь по chatId
     */
    Mono<Boolean> existsByChatId(Long chatId);
}
