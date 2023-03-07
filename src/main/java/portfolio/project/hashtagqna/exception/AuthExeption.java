package portfolio.project.hashtagqna.exception;

public class AuthExeption extends RuntimeException{
    public AuthExeption() {
    }

    public AuthExeption(String message) {
        super(message);
    }

    public AuthExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthExeption(Throwable cause) {
        super(cause);
    }

    public AuthExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
