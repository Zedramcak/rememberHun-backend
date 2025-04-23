package cz.adamzrcek.dtos.user;

public record UserDto(
        Long id,
        String username,
        String email
) {
}
