package com.example.demo.common;

/**
 * @author melo
 * @date 2017/3/30
 */
public class BaseResult {

    private int code;
    private String message;

    public BaseResult() {
    }

    private Object data;

    /**
     * 日志对象
     */

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    public BaseResult validateFailed(String message) {
        this.code = Constants.RESPONSE_CODE_404;
        this.message = message;
        return this;
    }

    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    public BaseResult unauthorized(String message) {
        this.code = Constants.RESPONSE_CODE_401;
        this.message = "暂未登录或token已经过期";
        this.data = message;
        return this;
    }


    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    public BaseResult forbidden(String message) {
        this.code = Constants.RESPONSE_CODE_403;
        this.message = "没有相关权限";
        this.data = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
