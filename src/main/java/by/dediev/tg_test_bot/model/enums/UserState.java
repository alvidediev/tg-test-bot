package by.dediev.tg_test_bot.model.enums;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserState {
    private Long chatId;
    private CurrentStateEnum currentState;
    private TempData tempData;
}
