package exceptions;

public class MailAlreadyExistsException extends Exception {
    public MailAlreadyExistsException(String message) {
        super(message);
    }
}