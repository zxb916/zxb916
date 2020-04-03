package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.BaseController;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.service.ScoreService;
import io.micrometer.core.instrument.util.IOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Api(value = "查询成绩信息")
@RestController
@RequestMapping("/score")
public class ScoreSearchComtroller extends BaseController {

    @Autowired
    private ScoreService scoreService;
    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    //学生端成绩查询
    @ApiOperation(value = "学生端根据身份证号和创建时间获取学生成绩")
    @GetMapping(value = "/select")
    public BaseResult select(@RequestParam(value = "idCard") String idCard, @RequestParam(value = "year") String year) {
        if (StringUtils.isEmpty(idCard) || StringUtils.isEmpty(year)) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "身份证号和创建时间不能为空");
        }
        ArrayList<Object> userScore = scoreService.getUserScore(idCard, year);
        return new BaseResult(Constants.RESPONSE_CODE_200, "获取成功", userScore);
    }

    //教师端成绩查询
    @ApiOperation(value = "教师端根据工种和创建时间获取学生成绩")
    @GetMapping(value = "/adminSelect")
    public BaseResult selectList(@RequestParam String applyWorkType, @RequestParam String year) {
        if (StringUtils.isEmpty(applyWorkType) || StringUtils.isEmpty(year)) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "申请工种和创建时间不能为空");
        }
        List<Object> userScore = selectScore(applyWorkType, year);
        return new BaseResult(Constants.RESPONSE_CODE_200, "获取成功", userScore);
    }

    //教师端录入成绩
    @ApiOperation(value = "教师端根据录入学生成绩")

    @PostMapping(value = "/add")
    public BaseResult add(HttpServletRequest request) {
        //TODO token 校验
//        String token = request.getHeader("token");
        try {
            String str = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
            JSONArray objects = JSONArray.parseArray(str);
            for (Object object : objects) {
                JSONObject score = JSONObject.parseObject(object.toString());
                String idCard = score.getString("idCard");
                Long theoryScore = Long.parseLong(score.getString("theoryScore"));
                Long operationScore = Long.parseLong(score.getString("operationScore"));
                Long overallScore = Long.parseLong(score.getString("overallScore"));
                String finalResult = score.getString("finalResult");
                String creatTime = score.getString("creatTime");
                scoreService.insert(idCard, theoryScore, operationScore, overallScore, finalResult, creatTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "插入成绩成功");
    }

    //成绩导出
    @ApiOperation(value = "导出学生成绩")
    @PostMapping(value = "/export")
    public BaseResult export(HttpServletResponse response, String applyWorkType, String year) throws Exception {
        if (StringUtils.isEmpty(applyWorkType) || StringUtils.isEmpty(year)) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "申请工种和创建时间不能为空");
        }
        scoreService.export(response, applyWorkType, year);
        return new BaseResult(Constants.RESPONSE_CODE_200, "导出成绩成功");
    }

    //上传附件
    @ApiOperation(value = "上传附件")
    @PostMapping(value = "/upload")
    public BaseResult upload(Long id, File file) throws Exception {
        DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
        if (Integer.valueOf(df.format(file.length() / 1024)) > 512) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "文件过大，请重新上传");
        }
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();
        scoreService.upload(id, file);
        return new BaseResult(Constants.RESPONSE_CODE_200, "上传附件成功");
    }


    //教师端查询到的成绩
    private List<Object> selectScore(@RequestParam(value = "applyWorkType", required = true) String applyWorkType, @RequestParam(value = "year", required = true) String year) {
        List<Object> userScore = scoreService.getUserScoreList(applyWorkType, year);
        return userScore;
    }
}
