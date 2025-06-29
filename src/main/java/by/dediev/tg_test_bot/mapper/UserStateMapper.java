package by.dediev.tg_test_bot.mapper;

import by.dediev.tg_test_bot.entity.UserEntity;
import by.dediev.tg_test_bot.model.enums.UserState;

/**
 * Простейший маппер для сущности юзера
 */
public class UserStateMapper {
    public static UserEntity mapUserState(UserState userState) {
        return UserEntity.builder()
                .chatId(userState.getChatId())
                .build();
    }
}
