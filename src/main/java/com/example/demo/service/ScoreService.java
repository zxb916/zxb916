package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public interface ScoreService {
    ArrayList<Object> getUserScore(String idCard, Date createTime);

    List<Object> getUserScoreList(String alreadyWorkType, Date createTime);

    void insert(String idCard, Long theoryScore, Long operationScore, Long overallScore, String finalResult);

    void export(HttpServletResponse response, String alreadyWorkType, Date createTime) throws Exception;

    void upload(Long id, File file) throws FileNotFoundException;
}
