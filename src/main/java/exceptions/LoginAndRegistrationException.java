package exceptions;

public class LoginAndRegistrationException extends Exception {

    public LoginAndRegistrationException() {
        super();
    }

    public LoginAndRegistrationException(String message) {
        super(message);
    }

    public LoginAndRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}