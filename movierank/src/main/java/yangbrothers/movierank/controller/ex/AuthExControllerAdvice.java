package yangbrothers.movierank.controller.ex;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yangbrothers.movierank.api.ApiUtils;
import yangbrothers.movierank.dto.SignUpDTO;
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

        return ApiUtils.getFailResult(ex.getMessage(), ApiUtils.FAIL_BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult authenticationExHandle(AuthenticationEx ex) {

        return ApiUtils.getFailResult(ex.getMessage(), ApiUtils.FAIL_UNAUTHORIZED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult signUpExHandle(SignUpEx ex) {
        if (ex.getSignUpDTO() != null) {
            SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(ex.getSignUpDTO().getUsername());
            ApiUtils.makeFailResult(signUpResponseDTO, ex.getMessage(), ApiUtils.FAIL_BAD_REQUEST);

            return signUpResponseDTO;
        } else {
            return ApiUtils.getFailResult(ex.getMessage(), ApiUtils.FAIL_BAD_REQUEST);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult usernameNotFoundExHandle(UsernameNotFoundException ex) {

        return ApiUtils.getFailResult(ex.getMessage(), ApiUtils.FAIL_BAD_REQUEST);
    }
}