package ari;

public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String fieldName, String reason) {
        super(String.format("Invalid input for '%s': %s", fieldName, reason));
    }
}
