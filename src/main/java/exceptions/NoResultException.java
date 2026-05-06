package exceptions;

public class NoResultException extends RuntimeException {
    public NoResultException() {
        super("L'operazione non ha prodotto alcun risultato.");
    }

    public NoResultException(String message) {
        super(message);
    }

    public NoResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
