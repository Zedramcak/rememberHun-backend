package cz.adamzrcek.modules.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private String code;
    private LocalDateTime timestamp;
}
