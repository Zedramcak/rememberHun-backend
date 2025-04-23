package cz.adamzrcek.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
