package cz.adamzrcek.modules.connection.exception;

import cz.adamzrcek.modules.shared.exception.ResourceNotFoundException;

public class ConnectionNotFoundException extends ResourceNotFoundException {
    public ConnectionNotFoundException(String connectionNotFound) {
        super(connectionNotFound);
    }
}
