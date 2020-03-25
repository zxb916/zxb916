package com.example.demo.common;


/**
 * @author wangq
 */
public abstract class BaseController {

    final public static String OK = "OK";


    public BaseResult sendResult(int code, String message) {
        return new BaseResult(code, message);
    }

    public BaseResult sendResult(int code, String message, Object data) {
        return new BaseResult(code, message, data);
    }


    public boolean isNotOK(String check) {
        return !OK.equals(check);
    }
}
