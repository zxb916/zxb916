package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.bo.AdminUserDetails;
import com.example.demo.common.BaseController;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.component.JwtTokenUtil;
import com.example.demo.model.SignUp;
import com.example.demo.model.User;
import com.example.demo.service.SignUpService;
import com.example.demo.service.UserService;
import com.example.demo.util.BeanCopy;
import com.example.demo.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Api(value = "用户")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {


    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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

    @Autowired
    private SignUpService signUpService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(@RequestBody User user) {
        logger.info("注册" + user);
        userService.register(user);
        return ResponseEntity.ok(new BaseResult(Constants.RESPONSE_CODE_200, "注册成功"));
    }


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
            if (token == null) {
                return new BaseResult(Constants.RESPONSE_CODE_403, "用户名密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new BaseResult(Constants.RESPONSE_CODE_200, "登录成功", tokenMap);
    }


    @ApiOperation(value = "刷新token")
    @GetMapping(value = "/token/refresh")
    public BaseResult refreshToken(HttpServletRequest request) {
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
    @PostMapping(value = "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResult changePassword(@RequestBody String str, HttpServletRequest request) {
        logger.info("修改密码", str);
        AdminUserDetails adminUserDetails = jwtTokenUtil.getAdminByToken(request.getHeader(tokenHeader).substring(this.tokenHead.length()));
        try {
            Map<String, String> object = (Map<String, String>) JSONObject.parse(str);
            String oldPassword = object.get("oldPassword");
            String newPassword = object.get("newPassword");
            User user = adminUserDetails.getUser();
            if (!BCrypt.checkpw(oldPassword, user.getPassword()))
                return new BaseResult(Constants.RESPONSE_CODE_404, "原始密码错误");
            user.setOldPassword(newPassword);
            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            user.setPassword(hashed);
            userService.save(user);
        } catch (Exception e) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "数据库异常");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "修改密码成功");
    }

    @ApiOperation(value = "重置密码")
    @GetMapping(value = "/resetPassword")
    public BaseResult refreshToken(@RequestParam("idCard") String idCard) {
        logger.info("重置密码" + idCard);
        if (idCard == null) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "idCard不能为null");
        }
        AdminUserDetails adminUserDetails = userService.getAdminByIdCard(idCard);
        if (adminUserDetails == null) {
            return new BaseResult(Constants.RESPONSE_CODE_404, "该用户不存在");
        }
        User user = adminUserDetails.getUser();
        String hashed = BCrypt.hashpw("123456", BCrypt.gensalt(12));
        user.setPassword(hashed);
        userService.save(user);
        return new BaseResult(Constants.RESPONSE_CODE_200, "重置密码成功");
    }


    @ApiOperation(value = "获取用户密码")
    @GetMapping(value = "/getPassword")
    public BaseResult getPassword(@RequestParam("idCard") String idCard) {
        logger.info("获取用户密码" + idCard);
        if (idCard == null) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "idCard不能为null");
        }
        AdminUserDetails adminUserDetails = userService.getAdminByIdCard(idCard);
        if (adminUserDetails == null) {
            return new BaseResult(Constants.RESPONSE_CODE_404, "该用户不存在");
        }
        User user = adminUserDetails.getUser();
        return new BaseResult(Constants.RESPONSE_CODE_200, "success", user.getOldPassword());
    }


    @ApiOperation(value = "管理员查询基本信息")
    @GetMapping(value = "/select")
    public BaseResult select(@RequestParam("userName") String userName) {
        logger.info("查询学员基本信息" + userName);
        User user = null;
        try {
            AdminUserDetails adminUserDetails = userService.getAdminByUserNameOrIdCard(userName);
            if (adminUserDetails == null) {
                return new BaseResult(Constants.RESPONSE_CODE_404, "用户不存在");
            }
            user = adminUserDetails.getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "ok", user);
    }

    @ApiOperation(value = "查询学员基本信息")
    @GetMapping(value = "/getOne")
    public BaseResult getOne(@RequestParam(value = "idCard", required = false) String idCard, @RequestParam(value = "year") String year, HttpServletRequest request) {
        logger.info("查询学员基本信息 idCard=" + idCard + "  year=" + year);
        User user = null;
        Map<String, Object> map = new HashMap<>();
        try {
            if (!StringUtils.isEmpty(idCard)) {
                AdminUserDetails adminUserDetails = userService.getAdminByIdCard(idCard);
                if (adminUserDetails == null) {
                    return new BaseResult(Constants.RESPONSE_CODE_404, "用户不存在");
                }
                user = adminUserDetails.getUser();
            } else {
                logger.info(request.getHeader(tokenHeader));
                AdminUserDetails adminUserDetails = jwtTokenUtil.getAdminByToken(request.getHeader(tokenHeader).substring(this.tokenHead.length()));
                user = adminUserDetails.getUser();
            }
            SignUp signUp = signUpService.findByUserIdAndYear(user.getId(), year);
            map.put("user", user);
            map.put("signUp", signUp);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult(Constants.RESPONSE_CODE_500, "服务器异常");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "ok", map);
    }


    @ApiOperation(value = "学员基本信息修改")
    @PostMapping(value = "/edit")
    public BaseResult edit(@RequestBody User user, HttpServletRequest request) {
        logger.info("学员基本信息修改" + user);
        AdminUserDetails adminUserDetails = jwtTokenUtil.getAdminByToken(request.getHeader(tokenHeader).substring(this.tokenHead.length()));
        try {
            BeanUtils.copyProperties(user, adminUserDetails.getUser(), BeanCopy.getNullPropertyNames(user));
            userService.save(adminUserDetails.getUser());
        } catch (Exception e) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "数据库异常");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "修改信息成功");
    }


    //根据编号查询
    @ApiOperation(value = "根据ID查询用户信息")
    @PostMapping(value = "/findById")
    User findById(@RequestParam(value = "id") Long id) {
        Optional<User> user = userService.getItem(id);
        return user.get();
    }

    @ApiOperation(value = "查询所有")
    @PostMapping(value = "/findAll")
        //currentPage 当前页，默认为0，如果传1的话是查的第二页数据
        //pageSize 每页数据条数
    Page<User> findAll(@RequestParam(value = "currentPage") int currentPage, @RequestParam(value = "pageSize") int pageSize) {
        Pageable pageable = PageUtil.getPageable(currentPage, pageSize, "datetime");
        return userService.findAll(pageable);
    }

    @ApiOperation(value = "删除用户")
    @PostMapping(value = "/delete")
    public BaseResult deleteUser(@RequestBody User user) {
        AdminUserDetails adminUserDetails = userService.getAdminByUserNameOrIdCard(user.getIdCard());
        user = adminUserDetails.getUser();
        if (user.getId() == null) {
            return new BaseResult(Constants.RESPONSE_CODE_200, "用户不存在");
        }
        userService.delete(user);
        return new BaseResult(Constants.RESPONSE_CODE_200, "删除用户成功");

    }


}
