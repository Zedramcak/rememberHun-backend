package cz.adamzrcek.exception;

public class ConnectionNotFoundException extends ResourceNotFoundException {
    public ConnectionNotFoundException(String connectionNotFound) {
        super(connectionNotFound);
    }
}
