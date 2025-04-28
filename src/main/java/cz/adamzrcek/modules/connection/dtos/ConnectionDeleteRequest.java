package cz.adamzrcek.modules.connection.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionDeleteRequest{
    private Long connectionId;
}