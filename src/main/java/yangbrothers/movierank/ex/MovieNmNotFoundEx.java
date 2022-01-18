package yangbrothers.movierank.ex;

public class MovieNmNotFoundEx extends RuntimeException {
    public MovieNmNotFoundEx() {
        super();
    }

    public MovieNmNotFoundEx(String message) {
        super(message);
    }

    public MovieNmNotFoundEx(String message, Throwable cause) {
        super(message, cause);
    }

    public MovieNmNotFoundEx(Throwable cause) {
        super(cause);
    }

    protected MovieNmNotFoundEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
