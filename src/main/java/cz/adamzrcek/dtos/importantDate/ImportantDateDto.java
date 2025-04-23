package cz.adamzrcek.dtos.importantDate;

import cz.adamzrcek.entity.enums.ImportantDateType;

import java.time.LocalDate;

public record ImportantDateDto(
        Long id,
        String title,
        String note,
        LocalDate date,
        ImportantDateType type,
        boolean shouldBeNotified) {
}
