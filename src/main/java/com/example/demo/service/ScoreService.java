package com.example.demo.service;

import com.example.demo.model.Score;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public interface ScoreService {
    ArrayList<Object> getUserScore(String idCard, String year);

    List<Object> getUserScoreList(String applyWorkType, String year);

    void insert(Score score);

    void export(HttpServletResponse response, String applyWorkType, String year) throws Exception;

    void upload(Long id, File file) throws FileNotFoundException;
}
