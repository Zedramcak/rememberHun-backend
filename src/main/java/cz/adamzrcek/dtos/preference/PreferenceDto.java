package cz.adamzrcek.dtos.preference;

public record PreferenceDto(
        Long id,
        String category,
        String value
) {
}
