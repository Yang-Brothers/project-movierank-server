package yangbrothers.movierank.controller.exhandler;

import com.mchange.util.AlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yangbrothers.movierank.dto.SignUpResponseDTO;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.ex.AuthenticationEx;
import yangbrothers.movierank.ex.MovieNmNotFoundEx;
import yangbrothers.movierank.ex.SignUpEx;
import yangbrothers.movierank.util.ApiUtil;

import java.util.InputMismatchException;

@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult inputMisMatchExHandle(InputMismatchException ex) {

        return ApiUtil.getFailResult(null, ApiUtil.FAIL_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult authenticationExHandle(AuthenticationEx ex) {

        return ApiUtil.getFailResult(null, ApiUtil.FAIL_UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult alreadyExistsExHandle(AlreadyExistsException ex) {

        return ApiUtil.getFailResult(null, ApiUtil.FAIL_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult signUpExHandle(SignUpEx ex) {
        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(ex.getSignUpDTO().getUsername());
        ApiUtil.makeFailResult(signUpResponseDTO, ApiUtil.FAIL_BAD_REQUEST, ex.getMessage(), null);

        return signUpResponseDTO;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult usernameNotFoundExHandle(UsernameNotFoundException ex) {

        return ApiUtil.getFailResult(null, ApiUtil.FAIL_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult movieNmNotFoundExHandle(MovieNmNotFoundEx ex) {

        return ApiUtil.getFailResult(null, ApiUtil.FAIL_NOT_FOUND, ex.getMessage());
    }
}