package com.example.demo.common;

/**
 * @author melo
 * @date 2017/3/30
 */
public interface Constants {


    /**
     * 正常
     */
    int RESPONSE_CODE_200 = 200;
    /**
     * 参数错误
     */
    int RESPONSE_CODE_400 = 400;

    int RESPONSE_CODE_404 = 404;

    int RESPONSE_CODE_405 = 405;

    int RESPONSE_CODE_401 = 401;

    int RESPONSE_CODE_403 = 403;

    int RESPONSE_CODE_406 = 406;
    /**
     * 服务器未知错误
     */
    int RESPONSE_CODE_500 = 500;

    String RESPONSE_MESSAGE_200 = "success";

    String RESPONSE_MESSAGE_400 = "parameter error";

    String RESPONSE_MESSAGE_404 = "resource not found";

    String RESPONSE_MESSAGE_403 = "not inner reward";

    String RESPONSE_MESSAGE_405 = "No resource method found for POST";

    String RESPONSE_MESSAGE_415 = "Unsupported Media Type";

    String RESPONSE_MESSAGE_401 = "Unauthorized";

    String RESPONSE_MESSAGE_500 = "server error";

    String RESPONSE_MESSAGE_201 = "part success";

    String RESPONSE_MESSAGE_406 = "duplicate parameter";
    // end res

}
