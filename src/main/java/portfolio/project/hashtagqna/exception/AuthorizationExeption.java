package portfolio.project.hashtagqna.exception;

public class AuthorizationExeption extends RuntimeException{
    public AuthorizationExeption() {
    }

    public AuthorizationExeption(String message) {
        super(message);
    }

    public AuthorizationExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationExeption(Throwable cause) {
        super(cause);
    }

    public AuthorizationExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
