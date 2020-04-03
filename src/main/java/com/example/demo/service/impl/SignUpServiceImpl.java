package com.example.demo.service.impl;

import com.example.demo.model.SignUp;
import com.example.demo.repository.SignUpRepository;
import com.example.demo.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private SignUpRepository signUpRepository;

    @Override
    public SignUp save(SignUp signUp) {
        return signUpRepository.save(signUp);
    }

    @Override
    public List<SignUp> findByUserId(Long user_id) {
        return signUpRepository.findByUserId(user_id);
    }
}
