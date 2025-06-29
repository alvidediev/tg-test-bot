package by.dediev.tg_test_bot.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
    private Long id;
    private Long chatId;
}
