package cz.adamzrcek.modules.importantdate.dtos;

import java.time.LocalDate;

public record ImportantDateRequest(
        String title,
        String note,
        LocalDate date,
        Long typeId,
        boolean shouldBeNotified
) {
}
