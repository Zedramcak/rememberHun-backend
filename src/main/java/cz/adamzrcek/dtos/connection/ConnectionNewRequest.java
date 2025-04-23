package cz.adamzrcek.dtos.connection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionNewRequest {
    private Long userId;
}
