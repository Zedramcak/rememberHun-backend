package cz.adamzrcek.dtos.connection;

import cz.adamzrcek.dtos.user.UserDto;

import java.time.LocalDateTime;

public record ConnectionSignedUserResponse(
    Long id,
    UserDto partner,
    LocalDateTime created_at,
    String status
) {}
