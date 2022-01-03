package yangbrothers.movierank.ex;

public class LoginEx extends RuntimeException {
    public LoginEx() {
        super();
    }

    public LoginEx(String message) {
        super(message);
    }

    public LoginEx(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginEx(Throwable cause) {
        super(cause);
    }

    protected LoginEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
