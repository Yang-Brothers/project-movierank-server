package yangbrothers.movierank.ex;

import lombok.Getter;
import yangbrothers.movierank.dto.SignUpDTO;

@Getter
public class SignUpEx extends RuntimeException {

    private SignUpDTO signUpDTO;

    public SignUpEx() {
        super();
    }

    public SignUpEx(String message) {
        super(message);
    }

    public SignUpEx(String message, Throwable cause) {
        super(message, cause);
    }

    public SignUpEx(Throwable cause) {
        super(cause);
    }

    protected SignUpEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SignUpEx(String message, SignUpDTO signUpDTO) {
        super(message);
        this.signUpDTO = signUpDTO;
    }
}
