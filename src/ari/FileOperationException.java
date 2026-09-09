package ari;

public class FileOperationException extends Exception {
    public FileOperationException(String message) { super(message); }
    public FileOperationException(String operation, String fileName, Throwable cause) { super(String.format("Failed to %s file '%s': %s", operation, fileName, cause.getMessage()), cause); }
}
