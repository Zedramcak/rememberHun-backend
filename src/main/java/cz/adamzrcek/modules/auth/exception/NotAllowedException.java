package cz.adamzrcek.modules.auth.exception;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String s) {
        super(s);
    }
}
