package by.dediev.tg_test_bot.repository.usersRepository.impl;

import by.dediev.tg_test_bot.entity.UserEntity;
import by.dediev.tg_test_bot.repository.usersRepository.UserRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator tx;

    @Override
    public Mono<UserEntity> save(UserEntity userEntity) {


        return databaseClient
                .sql("""
                        INSERT INTO users (chat_id)
                        VALUES (:chatId)
                        RETURNING id, chat_id
                        """)
                .bind("chatId", userEntity.getChatId())
                .map(UserRepositoryImpl::map)
                .one()
                .as(tx::transactional);
    }

    @Override
    public Mono<UserEntity> findByChatId(Long chatId) {
        return databaseClient
                .sql("""
                        SELECT * FROM users
                        WHERE users.chat_id = :chatId
                        """)
                .bind("chatId", chatId)
                .map(UserRepositoryImpl::map)
                .one();
    }

    @Override
    public Mono<Boolean> existsByChatId(Long chatId) {
        return databaseClient
                .sql("""
                        SELECT EXISTS(SELECT 1 FROM users WHERE users.chat_id = :chatId);
                        """)
                .bind("chatId", chatId)
                .map((row, rowMetadata) -> row.get("exists", Boolean.class))
                .one();
    }

    private static UserEntity map(Row row, RowMetadata rowMetadata) {
        return UserEntity.builder()
                .id(row.get("id", Long.class))
                .chatId(row.get("chat_id", Long.class))
                .build();
    }
}
