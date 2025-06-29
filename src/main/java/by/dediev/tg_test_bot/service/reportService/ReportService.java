package by.dediev.tg_test_bot.service.reportService;

import by.dediev.tg_test_bot.entity.TempDataEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReportService {

    /**
     * Генерация документа docx с данными из формы
     * @param dataList - данные пользователя из формы (/form)
     * @return - байтовое представление документа
     */
    Mono<byte[]> generateReport(List<TempDataEntity> dataList);
}
