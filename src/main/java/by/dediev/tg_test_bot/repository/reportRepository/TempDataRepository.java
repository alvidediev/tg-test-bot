package by.dediev.tg_test_bot.repository.reportRepository;

import by.dediev.tg_test_bot.entity.TempDataEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Репозиторный слой к данным, заполненным пользователем
 */
public interface TempDataRepository {
    /**
     * Transactional; Сохранение в бд состояния
     */
    Mono<TempDataEntity> save(TempDataEntity tempDataEntity);

    /**
     * Найти все записи
     */
    Flux<TempDataEntity> findAll();
}
