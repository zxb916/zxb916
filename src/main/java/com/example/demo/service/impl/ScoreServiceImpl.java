package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.UserController;
import com.example.demo.model.Score;
import com.example.demo.model.SignUp;
import com.example.demo.model.User;
import com.example.demo.model.UserExt;
import com.example.demo.repository.ScoreRepository;
import com.example.demo.repository.SignUpRepository;
import com.example.demo.repository.UserExtRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ScoreService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private SignUpRepository signUpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserExtRepository userExtRepository;


    @Override
    public ArrayList<Object> getUserScore(String idCard, Date createTime) {
        ArrayList<Object> result = new ArrayList<>();
//        Optional<User> byId = userRepository.findById(1L);
        User user = userRepository.findByIdCardLike(idCard).get(0);
        List<SignUp> signUps = signUpRepository.getList(user.getId(), createTime);
        for (SignUp signUp : signUps) {
            JSONObject object = new JSONObject();
            Score userScore = scoreRepository.getUserScore(signUp.getId());
            object.put("userName", user.getUserName());
            object.put("alreadyWorkType", signUp.getAlreadyWorkType());
            object.put("alreadySkillRank", signUp.getAlreadySkillRank());
            object.put("theoryScore", userScore.getTheoryScore());
            object.put("operationScore", userScore.getOperationScore());
            object.put("overallScore", userScore.getOverallScore());
            object.put("finalResult", userScore.getFinalResult());
            object.put("certificateNo", userScore.getCertificateNo());
            result.add(object);
        }
        return result;
    }

    @Override
    public List<Object> getUserScoreList(String alreadyWorkType, Date createTime) {
        ArrayList<Object> result = new ArrayList<>();
//        List<SignUp> signUpList = signUpRepository.getSignUpList(alreadyWorkType, createTime);
        List<SignUp> signUpList = signUpRepository.getSignUpList(alreadyWorkType);
        for (SignUp signUp : signUpList) {
            JSONObject object = new JSONObject();
            Score score = scoreRepository.getScore(signUp.getId());
            User user = userRepository.findById(signUp.getUser().getId()).get();
            UserExt userExt = userExtRepository.getOne(user.getId());
            object.put("userId", signUp.getUser().getId());
            object.put("userName", user.getUserName());
            object.put("idCard", user.getIdCard());
            object.put("soldierId", user.getSoldierId());
            object.put("alreadyWorkType", signUp.getAlreadyWorkType());
            object.put("alreadySkillRank", signUp.getAlreadySkillRank());
            object.put("Deptno", user.getDeptNo());
            object.put("armedRank", user.getArmedRank());
            object.put("passCard", userExt.getPostCode());
            object.put("theoryScore", score.getTheoryScore());
            object.put("operationScore", score.getOperationScore());
            object.put("overallScore", score.getOverallScore());
            object.put("finalResult", score.getFinalResult());
            object.put("certificateNo", score.getCertificateNo());
            object.put("createTime", score.getCreate_time());
            result.add(object);
        }
        return result;
    }

    @Override
    public void insert(String idCard, Long theoryScore, Long operationScore, Long overallScore, String finalResult) {
        User user = userRepository.findByIdCardLike(idCard).get(0);
        SignUp signUp = signUpRepository.getOne(user.getId());
        Score score = scoreRepository.getScore(signUp.getId());
        score.setTheoryScore(theoryScore);
        score.setOperationScore(operationScore);
        score.setOverallScore(overallScore);
        score.setFinalResult(finalResult);
        scoreRepository.save(score);
    }

    @Override
    public void export(HttpServletResponse response, String alreadyWorkType, Date createTime) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("学生成绩表");
        createTitle(workbook, sheet);
        List<Object> userScoreList = getUserScoreList(alreadyWorkType, createTime);
        HSSFRow row = sheet.createRow(userScoreList.size() + 1);
        int count = 1;
        for (Object object : userScoreList) {
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            row.setHeightInPoints(18);
            row.createCell(0).setCellValue(count);
            row.createCell(1).setCellValue(jsonObject.getString("userName"));
            row.createCell(2).setCellValue(jsonObject.getString("idCard"));
            row.createCell(3).setCellValue(jsonObject.getString("soldierId"));
            row.createCell(4).setCellValue(jsonObject.getString("alreadyWorkType"));
            row.createCell(5).setCellValue(jsonObject.getString("theoryScore"));
            row.createCell(6).setCellValue(jsonObject.getString("operationScore"));
            row.createCell(7).setCellValue(jsonObject.getString("overallScore"));
            row.createCell(8).setCellValue(jsonObject.getString("finalResult"));
            row.createCell(9).setCellValue(jsonObject.getString("certificateNo"));
            count++;
        }
        String fileName = "成绩表.xls";
        //生成excel文件
        buildExcelFile(fileName, workbook);

        //浏览器下载excel
        buildExcelDocument(fileName, workbook, response);
    }


    @Override
    public void upload(Long id, File file) {
        if (null == file) {
            logger.info("没有传文件或获取不到文件");
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            UserExt userExt = userExtRepository.getOne(id);
            userExt.setProfilePhoto(fileOutputStream.toString());
            userExtRepository.save(userExt);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }


    //创建表头
    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        //合并单元格
        //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
        CellRangeAddress region1 = new CellRangeAddress(0, 1, 0, 0);
        sheet.addMergedRegion(region1);
        HSSFRow row = sheet.createRow(0);
        //设置行高
        row.setHeightInPoints(18);
        //设置列宽度
        sheet.setColumnWidth(0, 30 * 256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("身份证号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("士兵证号");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("鉴定工种");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("理论成绩");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("操作成绩");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("综合成绩");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("评定结果");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("证书编号");
        cell.setCellStyle(style);
    }


    //生成excel文件
    public void buildExcelFile(String filename, HSSFWorkbook workbook) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    public void buildExcelDocument(String filename, HSSFWorkbook workbook, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


}

