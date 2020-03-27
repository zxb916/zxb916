package com.example.demo.controller;


import com.example.demo.bo.AdminUserDetails;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) throws Exception {
        //学员登录
        if (StringUtils.isEmpty(authenticationRequest.getIdCard()) && StringUtils.isEmpty(authenticationRequest.getPassword())) {
            authenticate(authenticationRequest.getIdCard(), authenticationRequest.getPassword());
        } else if (StringUtils.isEmpty(authenticationRequest.getUserName()) && StringUtils.isEmpty(authenticationRequest.getPassword())) {
            authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        }
        final AdminUserDetails userDetails = userService.getAdminByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new BaseResult(Constants.RESPONSE_CODE_200, "获取token成功", token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        userService.save(user);
        return ResponseEntity.ok(new BaseResult(Constants.RESPONSE_CODE_200, "注册成功"));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}
