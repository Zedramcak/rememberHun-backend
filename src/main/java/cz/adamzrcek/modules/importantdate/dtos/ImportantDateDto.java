package cz.adamzrcek.modules.importantdate.dtos;

import java.time.LocalDate;

public record ImportantDateDto(
        Long id,
        String title,
        String note,
        LocalDate date,
        String type,
        boolean shouldBeNotified) {
}
