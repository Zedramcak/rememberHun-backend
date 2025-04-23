package cz.adamzrcek.dtos.preference;

import cz.adamzrcek.entity.enums.PreferencesCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceNewRequest {
    private PreferencesCategory category;
    private String value;
}
