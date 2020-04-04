package com.example.demo.service;

import com.example.demo.model.SignUp;

import java.util.List;

/**
 * 后台管理员Service
 * Created by zxb on 2020-04-02
 */
public interface SignUpService {

    /**
     * 新增
     */
    SignUp save(SignUp signUp);

    /**
     * 根据用户id获取报名列表
     *
     * @param user_id
     * @return
     */
    List<SignUp> findByUserId(Long user_id);

    /**
     * 获取报名信息
     *
     * @param user_id
     * @param year
     * @return
     */
    SignUp findByUserIdAndYear(Long user_id, String year);

}
