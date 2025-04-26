package cz.adamzrcek.dtos.importantDate;

import java.time.LocalDate;

public record ImportantDateDto(
        Long id,
        String title,
        String note,
        LocalDate date,
        String type,
        boolean shouldBeNotified) {
}
