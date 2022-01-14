package yangbrothers.movierank.controller.exhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yangbrothers.movierank.dto.common.CommonResult;
import yangbrothers.movierank.util.ApiUtil;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class ValidationControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult methodArgumentNotValidExHandle(MethodArgumentNotValidException ex) {
        HashMap<String, String> validationMsg = new HashMap<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError error : fieldErrors) {
            String message = Arrays.stream(Objects.requireNonNull(error.getCodes()))
                    .map(c -> {
                        Object[] arguments = error.getArguments();
                        try {
                            return messageSource.getMessage(c, arguments, null);
                        } catch (NoSuchMessageException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElse(error.getDefaultMessage());

            validationMsg.put(error.getField(), message);
        }

        return ApiUtil.getFailResult(validationMsg, ApiUtil.FAIL_BAD_REQUEST, null);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult bindExHandle(BindException ex) {
        HashMap<String, String> validationMsg = new HashMap<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError error : fieldErrors) {
            String message = Arrays.stream(Objects.requireNonNull(error.getCodes()))
                    .map(c -> {
                        Object[] arguments = error.getArguments();
                        try {
                            return messageSource.getMessage(c, arguments, null);
                        } catch (NoSuchMessageException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElse(error.getDefaultMessage());

            validationMsg.put(error.getField(), message);
        }

        return ApiUtil.getFailResult(validationMsg, ApiUtil.FAIL_BAD_REQUEST, null);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult constraintViolationExHandle(ConstraintViolationException ex) {
        HashMap<String, String> validationMsg = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : violations) {
            String className = violation.getRootBeanClass().getSimpleName();
            String property = violation.getPropertyPath().toString();
            String msg = violation.getMessage();

            String format = String.format("%s.%s %s", className, property, msg);
            validationMsg.put(property, format);
        }

        return ApiUtil.getFailResult(validationMsg, ApiUtil.FAIL_BAD_REQUEST, null);
    }
}
