package yangbrothers.movierank.ex;

public class MovieNmRequiredEx extends RuntimeException {
    public MovieNmRequiredEx() {
        super();
    }

    public MovieNmRequiredEx(String message) {
        super(message);
    }

    public MovieNmRequiredEx(String message, Throwable cause) {
        super(message, cause);
    }

    public MovieNmRequiredEx(Throwable cause) {
        super(cause);
    }

    protected MovieNmRequiredEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
