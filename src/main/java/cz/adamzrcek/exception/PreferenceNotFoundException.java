package cz.adamzrcek.exception;

import cz.adamzrcek.modules.shared.exception.ResourceNotFoundException;

public class PreferenceNotFoundException extends ResourceNotFoundException {
    public PreferenceNotFoundException(String message) {
        super(message);
    }
}
