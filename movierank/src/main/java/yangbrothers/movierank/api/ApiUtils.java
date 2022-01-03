package yangbrothers.movierank.api;

import yangbrothers.movierank.dto.TokenDTO;
import yangbrothers.movierank.dto.common.CommonResult;

public class ApiUtils {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public static final String AUTHORIZATION_SUCCESS = "200";
    public static final String SIGNUP_SUCCESS = "201";
    public static final String LOGIN_FAIL = "400";
    public static final String AUTHORIZATION_FAIL = "401";
    public static final String SIGNUP_FAIL = "402";

    public static void makeSuccessResult(CommonResult successResult, String code) {
        successResult.setResult(ApiUtils.SUCCESS);
        successResult.setCode(code);
    }

    public static void makeFailResult(CommonResult errorResult, String msg, String code) {
        errorResult.setResult(FAIL);
        errorResult.setMsg(msg);
        errorResult.setCode(code);
    }
}
