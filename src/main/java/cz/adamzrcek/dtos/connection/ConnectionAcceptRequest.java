package cz.adamzrcek.dtos.connection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionAcceptRequest {
    private Long connectionId;
}
