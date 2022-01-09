package yangbrothers.movierank.controller.ex;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yangbrothers.movierank.util.ApiUtil;
import yangbrothers.movierank.dto.SignUpResponseDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.ex.AuthenticationEx;
import yangbrothers.movierank.ex.LoginEx;
import yangbrothers.movierank.ex.SignUpEx;

@RestControllerAdvice
public class AuthExControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult loginExHandle(LoginEx ex) {

        return ApiUtil.getFailResult(ex.getMessage(), ApiUtil.FAIL_BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult authenticationExHandle(AuthenticationEx ex) {

        return ApiUtil.getFailResult(ex.getMessage(), ApiUtil.FAIL_UNAUTHORIZED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult signUpExHandle(SignUpEx ex) {
        if (ex.getSignUpDTO() != null) {
            SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(ex.getSignUpDTO().getUsername());
            ApiUtil.makeFailResult(signUpResponseDTO, ex.getMessage(), ApiUtil.FAIL_BAD_REQUEST);

            return signUpResponseDTO;
        } else {
            return ApiUtil.getFailResult(ex.getMessage(), ApiUtil.FAIL_BAD_REQUEST);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult usernameNotFoundExHandle(UsernameNotFoundException ex) {

        return ApiUtil.getFailResult(ex.getMessage(), ApiUtil.FAIL_BAD_REQUEST);
    }
}