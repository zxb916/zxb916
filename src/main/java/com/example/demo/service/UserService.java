package com.example.demo.service;

import com.example.demo.bo.AdminUserDetails;
import com.example.demo.model.User;

import java.util.Optional;

/**
 * 后台管理员Service
 * Created by macro on 2018/4/26.
 */
public interface UserService {
    /**
     * 根据用户名获取后台管理员
     */
    AdminUserDetails getAdminByUsername(String username);

    /**
     * 注册功能
     */
    User register(User user);

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 刷新token的功能
     *
     * @param oldToken 旧的token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户id获取用户
     *
     * @return
     */
    Optional<User> getItem(Long id);


    /**
     * 修改指定用户信息
     */
    int update(Long id, User user);

    /**
     * 删除指定用户
     */
    int delete(Long id);

    /**
     * 新用户注册
     */
    void save(User user);

}
