package cz.adamzrcek.dtos.importantDate;

import java.time.LocalDate;

public record ImportantDateRequest(
        String title,
        String note,
        LocalDate date,
        Long typeId,
        boolean shouldBeNotified
) {
}
