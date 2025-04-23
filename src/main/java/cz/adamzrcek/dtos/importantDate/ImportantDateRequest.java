package cz.adamzrcek.dtos.importantDate;

import cz.adamzrcek.entity.enums.ImportantDateType;

import java.time.LocalDate;

public record ImportantDateRequest(
        String title,
        String note,
        LocalDate date,
        ImportantDateType type,
        boolean shouldBeNotified
) {
}
