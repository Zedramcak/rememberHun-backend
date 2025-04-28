package cz.adamzrcek.modules.user.exception;

import cz.adamzrcek.modules.shared.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
