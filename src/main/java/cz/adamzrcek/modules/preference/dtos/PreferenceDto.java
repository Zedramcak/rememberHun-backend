package cz.adamzrcek.modules.preference.dtos;

public record PreferenceDto(
        Long id,
        String category,
        String value
) {
}
