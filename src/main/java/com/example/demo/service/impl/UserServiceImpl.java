package com.example.demo.service.impl;

import com.example.demo.bo.AdminUserDetails;
import com.example.demo.model.RoleType;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * UmsAdminService实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UserRepository adminMapper;


    @Override
    public AdminUserDetails getAdminByUsername(String username) {
        List<User> adminList = adminMapper.findByNameLike(username);
        if (adminList != null && adminList.size() > 0) {
            AdminUserDetails adminDetails = new AdminUserDetails(adminList.get(0));
            return adminDetails;
        }
        return null;
    }

    public User register(User user) {
        user.setCreateTime(new Timestamp(new Date().getTime()));
        user.setIsAdmin(RoleType.ADMIN);
        //查询是否有相同用户名的用户
        //将密码进行加密操作
        String md5Password = passwordEncoder.encode(user.getPassword());
        user.setPassword(md5Password);
        adminMapper.save(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, passwordEncoder.encode(password));
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }


    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canRefresh(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public Optional<User> getItem(Long id) {
        return adminMapper.findById(id);
    }

    @Override
    public int update(Long id, User user) {
        return 0;
    }


    @Override
    public int delete(Long id) {
        adminMapper.deleteById(id);
        return 0;
    }

    @Override
    public void save(User user) {
        adminMapper.save(user);
    }
}
