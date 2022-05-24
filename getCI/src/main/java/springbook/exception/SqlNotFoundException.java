package springbook.exception;

public class SqlNotFoundException extends RuntimeException{

    public SqlNotFoundException() {
        super();
    }

    public SqlNotFoundException(String message) {
        super(message);
    }

    public SqlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlNotFoundException(Throwable cause) {
        super(cause);
    }

    protected SqlNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
