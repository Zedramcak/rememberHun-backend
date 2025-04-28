package cz.adamzrcek.service;

import cz.adamzrcek.modules.referencedata.entity.Role;
import cz.adamzrcek.modules.user.entity.User;

public class TestUserFactory {
    public static User basicUser() {
        User user = new User();
        user.setUsername("username");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setRole(new Role(1L, "USER"));
        return user;
    }
}
