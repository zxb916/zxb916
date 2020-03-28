package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.bo.AdminUserDetails;
import com.example.demo.common.BaseController;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.component.JwtTokenUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(value = "用户")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok(new BaseResult(Constants.RESPONSE_CODE_200, "注册成功"));
    }


    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @ApiOperation(value = "登录")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult login(@RequestBody User user) {
        logger.info("登录" + user.toString());
        String token = "";
        try {
            //学员登录
            if (!StringUtils.isEmpty(user.getIdCard()) && !StringUtils.isEmpty(user.getPassword())) {
                token = userService.login(user.getIdCard(), user.getPassword());
            } else if (!StringUtils.isEmpty(user.getUserName()) && !StringUtils.isEmpty(user.getPassword())) {
                token = userService.login(user.getUserName(), user.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult(Constants.RESPONSE_CODE_403, "用户名密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new BaseResult(Constants.RESPONSE_CODE_200, "登录成功", tokenMap);
    }


    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    @ResponseBody
    public Object refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = userService.refreshToken(token);
        if (refreshToken == null) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "刷新token失败");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new BaseResult(Constants.RESPONSE_CODE_200, "sucess", tokenMap);
    }


    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public Object changePassword(@RequestBody String str) {
        logger.info("修改密码", str);

        try {
            Map<String, String> object = (Map<String, String>) JSONObject.parse(str);
            String idCard = object.get("idCard");
            String oldPassword = object.get("oldPassword");
            String newPassword = object.get("newPassword");
            AdminUserDetails adminUserDetails = userService.getAdminByIdCard(idCard);
            if (adminUserDetails == null) {
                return new UsernameNotFoundException("用户不存在");
            }
            User user = adminUserDetails.getUser();
            if (!BCrypt.checkpw(oldPassword, user.getPassword()))
                return new UsernameNotFoundException("原始密码错误");
            user.setPassword(newPassword);
        } catch (Exception e) {

        }
        return ResponseEntity.ok(new BaseResult(Constants.RESPONSE_CODE_200, "注册成功"));
    }


}
