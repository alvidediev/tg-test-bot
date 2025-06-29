package by.dediev.tg_test_bot.mapper;

import by.dediev.tg_test_bot.entity.TempDataEntity;
import by.dediev.tg_test_bot.model.enums.TempData;

/**
 * Простейший маппер. Подключать mapstruct для тестового не стал.
 */
public class TempDataMapper {
    public static TempDataEntity tampDataMapper(TempData tempData) {
        return TempDataEntity.builder()
                .userId(tempData.getUserId())
                .name(tempData.getName())
                .email(tempData.getEmail())
                .score(tempData.getScore())
                .build();
    }
}
