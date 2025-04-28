package cz.adamzrcek.modules.user.service;

import cz.adamzrcek.modules.user.entity.User;

public interface UserService {
    User getCurrentUser();
    User getUserById(Long id);
}
