package portfolio.project.hashtagqna.exception;

public class NotMemberException extends RuntimeException{
    public NotMemberException() {
        super();
    }

    public NotMemberException(String message) {
        super(message);
    }

    public NotMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMemberException(Throwable cause) {
        super(cause);
    }

    protected NotMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
