package cz.adamzrcek.exception;

public class PreferenceNotFoundException extends ResourceNotFoundException {
    public PreferenceNotFoundException(String message) {
        super(message);
    }
}
