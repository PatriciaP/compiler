package compiler.exceptions;

public class Exceptions extends Exception {

    public Exceptions() {
    }

    public Exceptions(String message) {
        super(message);
    }

    public Exceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public Exceptions(Throwable cause) {
        super(cause);
    }

    public Exceptions(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
