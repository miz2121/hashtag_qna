package portfolio.project.hashtagqna.exception;

public class AlreadyExistEmailNicknameException extends RuntimeException{
    public AlreadyExistEmailNicknameException() {
    }

    public AlreadyExistEmailNicknameException(String message) {
        super(message);
    }

    public AlreadyExistEmailNicknameException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistEmailNicknameException(Throwable cause) {
        super(cause);
    }

    public AlreadyExistEmailNicknameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
