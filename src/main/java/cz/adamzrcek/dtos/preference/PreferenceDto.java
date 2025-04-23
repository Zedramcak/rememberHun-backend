package cz.adamzrcek.dtos.preference;

import cz.adamzrcek.entity.enums.PreferencesCategory;

public record PreferenceDto(
        Long id,
        PreferencesCategory category,
        String value
) {
}
