package com.example.demo.service.impl;

import com.example.demo.model.Attach;
import com.example.demo.repository.AttachRepository;
import com.example.demo.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachServiceImpl implements AttachService {

    @Autowired
    private AttachRepository attachRepository;

    @Override
    public Attach save(Attach attach) {
        return attachRepository.save(attach);
    }
}
