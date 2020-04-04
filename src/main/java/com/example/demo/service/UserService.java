package com.example.demo.service;

import com.example.demo.bo.AdminUserDetails;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * 根据用户id_card登录
     */

    AdminUserDetails getAdminByIdCard(String idCard);

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
    void update(User user);

    /**
     * 新增用户
     *
     * @param user
     */
    void save(User user);

    /**
     * 分页查询用户信息
     */
    Page<User> findAll(Pageable pageable);

    /**
     * 根据idCard或userName 查询用户
     */
    AdminUserDetails getAdminByUserNameOrIdCard(String str);

    /**
     * 删除用户
     *
     * @param user
     */
    void delete(User user);

}
