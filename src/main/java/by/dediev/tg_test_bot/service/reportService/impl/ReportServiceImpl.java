package by.dediev.tg_test_bot.service.reportService.impl;

import by.dediev.tg_test_bot.entity.TempDataEntity;
import by.dediev.tg_test_bot.service.reportService.ReportService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public Mono<byte[]> generateReport(List<TempDataEntity> dataList) {
        return Mono.fromCallable(() -> {
            try (XWPFDocument document = new XWPFDocument()) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setBold(true);
                run.setFontSize(14);
                run.setText("Результаты опроса:");

                XWPFTable table = document.createTable();

                // Header row
                XWPFTableRow headerRow = table.getRow(0);
                headerRow.getCell(0).setText("Имя");
                headerRow.addNewTableCell().setText("Email");
                headerRow.addNewTableCell().setText("Оценка");

                for (TempDataEntity entry : dataList) {
                    XWPFTableRow row = table.createRow();
                    row.getCell(0).setText(entry.getName());
                    row.getCell(1).setText(entry.getEmail());
                    row.getCell(2).setText(String.valueOf(entry.getScore()));
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                document.write(out);
                return out.toByteArray();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
