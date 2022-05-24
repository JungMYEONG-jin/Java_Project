package springbook.exception;

public class SqlRetrievalFailureException extends RuntimeException{

    public SqlRetrievalFailureException(SqlNotFoundException message)
    {
        super(message);
    }

    public SqlRetrievalFailureException(String message, Throwable cause){
        super(message, cause);
    }
}
