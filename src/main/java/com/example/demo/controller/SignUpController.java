package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.component.JwtTokenUtil;
import com.example.demo.model.SignUp;
import com.example.demo.model.User;
import com.example.demo.service.SignUpService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "报名")
@RestController
@RequestMapping("/sign")
public class SignUpController {

    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SignUpService signUpService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;


    @ApiOperation(value = "新增报名")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult register(@RequestBody String str) {

        try {
            Map<String, Object> object = (Map<String, Object>) JSONObject.parse(str);
            User user = (User) object.get("user");
            SignUp signUp = (SignUp) object.get("signUp");
            userService.update(user);
            signUpService.save(signUp);
        } catch (Exception e) {
            logger.error("数据库操作异常");
            e.printStackTrace();
            return new BaseResult(Constants.RESPONSE_CODE_500, "数据库操作异常");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "新增或修改报名成功");
    }


}
