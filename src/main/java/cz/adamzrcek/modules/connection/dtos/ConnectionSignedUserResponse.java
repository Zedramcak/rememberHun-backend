package cz.adamzrcek.modules.connection.dtos;

import cz.adamzrcek.modules.user.dtos.UserDto;

import java.time.LocalDateTime;

public record ConnectionSignedUserResponse(
    Long id,
    UserDto partner,
    LocalDateTime created_at,
    String status
) {}
