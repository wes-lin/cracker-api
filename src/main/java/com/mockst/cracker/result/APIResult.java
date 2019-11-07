package com.mockst.cracker.result;

import java.io.Serializable;

/**
 * @author linzhiwei
 * @Description:响应结果
 * @date 2019/4/8 16:14
 */
public class APIResult implements Serializable {

    private String code;
    private String message;
    private Object data;

    public APIResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
