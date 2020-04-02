package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public interface ScoreService {
    ArrayList<Object> getUserScore(String idCard, String year);

    List<Object> getUserScoreList(String applyWorkType, String year);

    void insert(String idCard, Long theoryScore, Long operationScore, Long overallScore, String finalResult, String creatTime);

    void export(HttpServletResponse response, String alreadyWorkType, String createTime) throws Exception;

    void upload(Long id, File file) throws FileNotFoundException;
}
