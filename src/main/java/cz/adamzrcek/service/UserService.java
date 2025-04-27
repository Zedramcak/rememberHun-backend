package cz.adamzrcek.service;

import cz.adamzrcek.entity.User;

public interface UserService {
    User getCurrentUser();
    User getUserById(Long id);
}
