package com.example.demo.service.impl;

import com.example.demo.bo.AdminUserDetails;
import com.example.demo.component.JwtTokenUtil;
import com.example.demo.model.*;
import com.example.demo.repository.ResumeRepository;
import com.example.demo.repository.TrainRepository;
import com.example.demo.repository.UserExtRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.BeanCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * UmsAdminService实现类
 * Created by macro on 2018/4/26.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UserRepository adminMapper;

    @Autowired
    private UserExtRepository userExtRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Override
    public AdminUserDetails getAdminByUsername(String username) {
        List<User> adminList = adminMapper.findByNameLike(username);
        if (adminList != null && adminList.size() > 0) {
            AdminUserDetails adminDetails = new AdminUserDetails(adminList.get(0));
            return adminDetails;
        }
        return null;
    }

    @Override
    public AdminUserDetails getAdminByIdCard(String idCard) {
        List<User> adminList = adminMapper.findByIdCardLike(idCard);
        if (adminList != null && adminList.size() > 0) {
            AdminUserDetails adminDetails = new AdminUserDetails(adminList.get(0));
            return adminDetails;
        }
        return null;
    }

    public User register(User user) {
        user.setCreateTime(new Timestamp(new Date().getTime()));
        user.setIsAdmin(RoleType.ADMIN);
        user.setOldPassword(user.getPassword());
        //查询是否有相同用户名的用户
        //将密码进行加密操作
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashed);
        adminMapper.save(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AdminUserDetails userDetails = (AdminUserDetails) userDetailsService.loadUserByUsername(username);
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
    public User update(User oldUser, User user) {
        for (Resume o : oldUser.getResumes()) {
            resumeRepository.deleteById(o.getId());
            resumeRepository.delete(o);
        }
        for (Train o : oldUser.getTrains()) {
            trainRepository.deleteById(o.getId());
            trainRepository.delete(o);
        }
        if (oldUser.getUserExt() != null) {
            UserExt userExt = oldUser.getUserExt();
            userExt.setUser(null);
            userExtRepository.delete(userExt);
        }
        BeanUtils.copyProperties(user, oldUser, BeanCopy.getNullPropertyNames(user));
        oldUser.getUserExt().setUser(oldUser);
        for (Resume o : oldUser.getResumes()) {
            o.setUser(oldUser);
        }
        for (Train o : oldUser.getTrains()) {
            o.setUser(oldUser);
        }
        oldUser = adminMapper.save(oldUser);
        return oldUser;
    }

    @Override
    public void save(User user) {
        adminMapper.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return adminMapper.findAll(pageable);
    }

    @Override
    public AdminUserDetails getAdminByUserNameOrIdCard(String str) {
        AdminUserDetails adminUserDetails = null;
        adminUserDetails = getAdminByIdCard(str);
        if (adminUserDetails == null) {
            adminUserDetails = getAdminByUsername(str);
        }
        if (adminUserDetails == null) {
            return null;
        }
        return adminUserDetails;
    }

    @Override
    public void delete(User user) {
        adminMapper.delete(user);
    }


    @Override
    public User findUser(String idCard, String name, String mobile) {
        User user = adminMapper.findUser(idCard, name, mobile);
        return user;
    }

}
