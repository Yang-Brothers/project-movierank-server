package yangbrothers.movierank.controller.ex;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.SignUpDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.ex.AuthenticationEx;
import yangbrothers.movierank.ex.LoginEx;
import yangbrothers.movierank.ex.SignUpEx;

@RestControllerAdvice
public class AuthExControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult loginExHandle(LoginEx ex) {
        CommonResult errorResult = getErrorResult(ex.getMessage(), ApiUtils.LOGIN_FAIL);

        return errorResult;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult authExHandle(AuthenticationEx ex) {
        CommonResult errorResult = getErrorResult(ex.getMessage(), ApiUtils.AUTHORIZATION_FAIL);

        return errorResult;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult signUpExHandle(SignUpEx ex) {
        if (ex.getSignUpDTO() != null) {
            SignUpDTO signUpDTO = ex.getSignUpDTO();
            ApiUtils.makeFailResult(signUpDTO, ex.getMessage(), ApiUtils.SIGNUP_FAIL);

            return signUpDTO;
        } else {
            CommonResult errorResult = getErrorResult(ex.getMessage(), ApiUtils.SIGNUP_FAIL);

            return errorResult;
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult usernameNotFoundExHandle(UsernameNotFoundException ex) {
        CommonResult errorResult = getErrorResult(ex.getMessage(), ApiUtils.USER_NOT_FOUND);

        return errorResult;
    }

    private CommonResult getErrorResult(String message, String errorCode) {
        CommonResult errorResult = new CommonResult();
        ApiUtils.makeFailResult(errorResult, message, errorCode);
        return errorResult;
    }
}
