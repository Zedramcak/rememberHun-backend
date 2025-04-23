package cz.adamzrcek.service;

import cz.adamzrcek.entity.User;
import cz.adamzrcek.entity.enums.Role;

public class TestUserFactory {
    public static User basicUser() {
        User user = new User();
        user.setUsername("username");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        return user;
    }
}
