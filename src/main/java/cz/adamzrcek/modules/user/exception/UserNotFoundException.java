package cz.adamzrcek.modules.user.exception;

import cz.adamzrcek.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
