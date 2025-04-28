package cz.adamzrcek.modules.auth.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String invalidPassword) {
        super(invalidPassword);
    }
}
