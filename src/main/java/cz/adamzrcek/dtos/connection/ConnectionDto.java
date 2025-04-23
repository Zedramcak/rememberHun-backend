package cz.adamzrcek.dtos.connection;

import cz.adamzrcek.dtos.user.UserDto;
import cz.adamzrcek.entity.enums.ConnectionStatus;

import java.time.LocalDateTime;

public record ConnectionDto(
        Long id,
        UserDto user1,
        UserDto user2,
        LocalDateTime created_at,
        ConnectionStatus status
) {
}
