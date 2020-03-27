package com.example.demo.controller;


import com.example.demo.common.BaseController;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "用户模块")
@RestController("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @ApiOperation(value = "登录")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult login(@RequestBody User user) {
        logger.info("登录" + user.toString());
        String token = "";
        try {
            //学员登录
            if (StringUtils.isEmpty(user.getIdCard()) && StringUtils.isEmpty(user.getPassword())) {
                token = userService.login(user.getIdCard(), user.getPassword());
            } else if (StringUtils.isEmpty(user.getUserName()) && StringUtils.isEmpty(user.getPassword())) {
                token = userService.login(user.getUserName(), user.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new BaseResult(Constants.RESPONSE_CODE_403, "用户名密码错误");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "登录成功", token);
    }


}
