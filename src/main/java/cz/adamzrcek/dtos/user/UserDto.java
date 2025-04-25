package cz.adamzrcek.dtos.user;

public record UserDto(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName) {
}
