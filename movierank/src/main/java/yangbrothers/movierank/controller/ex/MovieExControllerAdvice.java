package yangbrothers.movierank.controller.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yangbrothers.movierank.util.ApiUtil;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.ex.MovieNmNotFoundEx;
import yangbrothers.movierank.ex.MovieNmRequiredEx;

@RestControllerAdvice
public class MovieExControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult movieNmRequiredExHandle(MovieNmRequiredEx ex) {

        return ApiUtil.getFailResult(ex.getMessage(), ApiUtil.FAIL_BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult movieNmNotFoundExHandle(MovieNmNotFoundEx ex) {
        return ApiUtil.getFailResult(ex.getMessage(), ApiUtil.FAIL_NOT_FOUND);
    }
}