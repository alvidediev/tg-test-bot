package by.dediev.tg_test_bot.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempDataEntity {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private Integer score;
}
