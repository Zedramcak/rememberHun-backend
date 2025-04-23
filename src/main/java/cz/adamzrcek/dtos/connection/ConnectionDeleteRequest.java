package cz.adamzrcek.dtos.connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ConnectionDeleteRequest{
    private Long connectionId;
}