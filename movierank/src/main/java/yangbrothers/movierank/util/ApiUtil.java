package yangbrothers.movierank.util;

import yangbrothers.movierank.dto.common.CommonResult;

public class ApiUtil {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public static final String SUCCESS_OK = "200";
    public static final String SUCCESS_CREATED = "201";
    public static final String SUCCESS_NO_CONTENT = "204";


    public static final String FAIL_BAD_REQUEST = "400";
    public static final String FAIL_UNAUTHORIZED = "401";
    public static final String FAIL_NOT_FOUND = "404";



    public static void makeSuccessResult(CommonResult successResult, String code) {
        successResult.setResult(ApiUtil.SUCCESS);
        successResult.setCode(code);
    }

    public static void makeFailResult(CommonResult errorResult, String msg, String code) {
        errorResult.setResult(FAIL);
        errorResult.setMsg(msg);
        errorResult.setCode(code);
    }

    public static CommonResult getSuccessResult(String code) {
        CommonResult successResult = new CommonResult();
        makeSuccessResult(successResult, code);

        return successResult;
    }

    public static CommonResult getFailResult(String msg, String code) {
        CommonResult errorResult = new CommonResult();
        makeFailResult(errorResult, msg, code);

        return errorResult;
    }
}