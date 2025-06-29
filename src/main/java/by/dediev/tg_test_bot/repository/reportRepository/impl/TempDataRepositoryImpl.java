package by.dediev.tg_test_bot.repository.reportRepository.impl;

import by.dediev.tg_test_bot.entity.TempDataEntity;
import by.dediev.tg_test_bot.repository.reportRepository.TempDataRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TempDataRepositoryImpl implements TempDataRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator tx;


    @Override
    public Mono<TempDataEntity> save(TempDataEntity tempDataEntity) {
        return databaseClient
                .sql("""
                        INSERT INTO report (user_id, name, email, score)
                        VALUES (:userId, :name, :email, :score)
                        RETURNING id, user_id, name, email, score
                        """)
                .bind("userId", tempDataEntity.getUserId())
                .bind("name", tempDataEntity.getName())
                .bind("email", tempDataEntity.getEmail())
                .bind("score", tempDataEntity.getScore())
                .map(TempDataRepositoryImpl::map)
                .one()
                .as(tx::transactional);
    }

    @Override
    public Flux<TempDataEntity> findAll() {
        return databaseClient
                .sql("SELECT * FROM report")
                .map((row, metadata) -> TempDataEntity.builder()
                        .id(row.get("id", Long.class))
                        .userId(row.get("user_id", Long.class))
                        .name(row.get("name", String.class))
                        .email(row.get("email", String.class))
                        .score(row.get("score", Integer.class))
                        .build())
                .all();
    }

    private static TempDataEntity map(Row row, RowMetadata metadata) {
        return TempDataEntity.builder()
                .userId(row.get("user_id", Long.class))
                .name(row.get("name", String.class))
                .email(row.get("email", String.class))
                .score(row.get("score", Integer.class))
                .build();
    }
}
