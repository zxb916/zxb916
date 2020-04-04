package com.example.demo.service.impl;

import com.example.demo.model.UserExt;
import com.example.demo.repository.UserExtRepository;
import com.example.demo.service.UserExtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UmsAdminService实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UserExtServiceImpl implements UserExtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserExtServiceImpl.class);

    @Autowired
    private UserExtRepository userExtRepository;


    @Override
    public UserExt register(UserExt userExt) {
        return userExtRepository.saveAndFlush(userExt);
    }
}
