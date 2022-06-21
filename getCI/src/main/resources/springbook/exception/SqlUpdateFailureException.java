package kakao.getCI.springbook.exception;

public class SqlUpdateFailureException extends RuntimeException{

    public SqlUpdateFailureException(String message) {
        super(message);
    }

    public SqlUpdateFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlUpdateFailureException(Throwable cause) {
        super(cause);
    }

    protected SqlUpdateFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
