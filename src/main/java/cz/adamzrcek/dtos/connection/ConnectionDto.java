package cz.adamzrcek.dtos.connection;

import cz.adamzrcek.modules.user.dtos.UserDto;

import java.time.LocalDateTime;

public record ConnectionDto(
        Long id,
        UserDto user1,
        UserDto user2,
        LocalDateTime created_at,
        String status
) {
}
