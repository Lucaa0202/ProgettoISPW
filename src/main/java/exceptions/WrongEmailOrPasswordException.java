package exceptions;

public class WrongEmailOrPasswordException extends Exception {
    public WrongEmailOrPasswordException(String message) {
        super(message);
    }
}