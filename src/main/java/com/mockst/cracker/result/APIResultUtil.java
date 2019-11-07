package com.mockst.cracker.result;

/**
 * @author linzhiwei
 * @Description:
 * @date 2019/4/11 19:22
 */
public class APIResultUtil {

    public static APIResult returnSuccessResult(Object data) {
        return new APIResult(APIConstant.SUCCESS, "", data);
    }

    public static APIResult responseBusinessFailedResult(String message, Object data) {
        return new APIResult(APIConstant.BUSINESS_PROCESSING_FAILED, message, data);
    }

    public static APIResult responseBusinessFailedResult(String code, String message, Object data) {
        return new APIResult(code, message, data);
    }

    public static APIResult responseBusinessFailedResult(String code, String message) {
        return new APIResult(code, message, null);
    }

    public static APIResult responseBusinessFailedResult(String message) {
        return new APIResult(APIConstant.BUSINESS_PROCESSING_FAILED, message, null);
    }
}
