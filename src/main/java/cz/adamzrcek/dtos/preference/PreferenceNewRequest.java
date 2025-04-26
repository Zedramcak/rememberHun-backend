package cz.adamzrcek.dtos.preference;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceNewRequest {
    private Long categoryId;
    private String value;
}
