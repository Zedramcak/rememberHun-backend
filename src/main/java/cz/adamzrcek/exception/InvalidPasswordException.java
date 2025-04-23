package cz.adamzrcek.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String invalidPassword) {
        super(invalidPassword);
    }
}
