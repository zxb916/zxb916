package com.example.demo.controller;


import com.example.demo.bo.AdminUserDetails;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.component.JwtTokenUtil;
import com.example.demo.model.SignUp;
import com.example.demo.service.SignUpService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public BaseResult register(@RequestBody SignUp signUp, HttpServletRequest request) {
        AdminUserDetails adminUserDetails = jwtTokenUtil.getAdminByToken(request.getHeader(tokenHeader).substring(this.tokenHead.length()));
        signUp.setUser(adminUserDetails.getUser());
        signUpService.save(signUp);
        return new BaseResult(Constants.RESPONSE_CODE_200, "报名成功");
    }
}
