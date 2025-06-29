package by.dediev.tg_test_bot.service.stage.inMemoryStage.impl;

import by.dediev.tg_test_bot.model.enums.CurrentStateEnum;
import by.dediev.tg_test_bot.model.enums.TempData;
import by.dediev.tg_test_bot.model.enums.UserState;
import by.dediev.tg_test_bot.service.stage.inMemoryStage.InMemoryStage;
import lombok.ToString;
import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ToString
public class InMemoryStageImpl implements InMemoryStage {
    private final Map<Long, UserState> userStates = new ConcurrentHashMap<>();

    @Override
    public void createState(Long chatId, UserState state) {
        userStates.put(chatId, state);
    }

    @Override
    public void updateState(Long chatId, CurrentStateEnum newState) {
        UserState state = userStates.get(chatId);
        if (state != null) {
            state.setCurrentState(newState);
        }
        userStates.put(chatId, state);
    }

    @Override
    public UserState getState(Long chatId) {
        if (userStates.containsKey(chatId)) {
            return userStates.get(chatId);
        }
        TempData tempData = new TempData();
        tempData.setUserId(chatId);

        UserState userState = UserState.builder()
                .chatId(chatId)
                .currentState(CurrentStateEnum.EMPTY)
                .tempData(tempData)
                .build();
        createState(chatId, userState);
        return userState;
    }

    @Override
    public void resetState(Long chatId) {
        userStates.get(chatId).setCurrentState(CurrentStateEnum.EMPTY);
    }
}
