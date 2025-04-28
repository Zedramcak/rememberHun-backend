package cz.adamzrcek.modules.preference.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceNewRequest {
    private Long categoryId;
    private String value;
}
