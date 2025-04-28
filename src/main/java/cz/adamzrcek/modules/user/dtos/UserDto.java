package cz.adamzrcek.modules.user.dtos;

public record UserDto(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName) {
}
