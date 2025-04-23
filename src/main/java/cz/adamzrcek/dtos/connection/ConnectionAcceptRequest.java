package cz.adamzrcek.dtos.connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ConnectionAcceptRequest {
    private Long connectionId;
}
