package cz.adamzrcek.exception;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String s) {
        super(s);
    }
}
