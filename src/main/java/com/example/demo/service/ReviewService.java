package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.SignUp;
import org.javatuples.Pair;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public interface ReviewService {
    /**
     * 根据工种和年份获取信息
     *
     * @param applyWorkType
     * @param year
     * @return
     */
    List<JSONObject> getList(String applyWorkType, String year);

    Pair<Boolean, String> checkAndSave(String requestParam);

    /**
     * 生成报表
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 生成报名表
     *
     * @param idCard
     * @return
     */
    File build(String idCard, String year);


    void saveAndFlush(SignUp signUp);

    File open(String filePath, String type, boolean visible);
}
