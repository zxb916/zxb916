package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.SignUp;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReviewService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<JSONObject> getList(String alreadyWorkType, String createTime) {
        List<SignUp> SignUpList = reviewRepository.getList(alreadyWorkType, createTime);
        List<JSONObject> result = new ArrayList<>();
        for (SignUp SignUp : SignUpList) {
            Optional<User> opt = userRepository.findById(SignUp.getUserId());
            User user = opt.get();
            JSONObject obj = new JSONObject();
            obj.put("userName", user.getUserName());
            obj.put("idCard", user.getIdCard());
            obj.put("soldierId", user.getSoldierId());
            obj.put("applyWorkType", SignUp.getApplyWorkType());
            obj.put("applySkillRank", SignUp.getApplySkillRank());
            obj.put("check", SignUp.getReview());
            obj.put("passCard", SignUp.getPassCard());
            result.add(obj);
        }
        return result;
    }

    @Override
    public Pair<Boolean, String> checkAndSave(String requestParam) {
        Pair<Boolean, String> result = new Pair<Boolean, String>(false, "未知");
        JSONObject param = JSONObject.parseObject(requestParam);
        String idCard = param.get("idCard").toString();
        String reviewOption = param.get("reviewOption").toString();
        String auditOpinion = param.get("auditOpinion").toString();
        int businessCheck = Integer.parseInt(param.get("businessCheck").toString());
        int soldiersCheck = Integer.parseInt(param.get("soldiersCheck").toString());
        int check = Integer.parseInt(param.get("check").toString());
        if (StringUtils.isEmpty(idCard)) {
            return result.setAt1("身份证不能为空");
        }
        if (StringUtils.isEmpty(param.get("reviewOption").toString())) {
            return result.setAt1("审核意见不能为空");
        }
        if (StringUtils.isEmpty(param.get("auditOpinion").toString())) {
            return result.setAt1("团级以上单位资格审查意见不能为空");
        }
        if (StringUtils.isEmpty(param.get("businessCheck").toString())) {
            return result.setAt1("业务主管审核不能为空");
        }
        if (StringUtils.isEmpty(param.get("soldiersCheck").toString())) {
            return result.setAt1("兵员和文职人员审核不能为空");
        }
        if (StringUtils.isEmpty(param.get("review").toString())) {
            return result.setAt1("审核不能为空");
        }
        User user = userRepository.findByIdCardLike(idCard).get(0);
        SignUp SignUp = reviewRepository.findByUserId(user.getId());
        SignUp.setReviewoption(reviewOption);
        SignUp.setAuditOpinion(auditOpinion);
        SignUp.setBusinessCheck(businessCheck);
        SignUp.setSoldiersCheck(soldiersCheck);
        SignUp.setReview(check);
        reviewRepository.saveAndFlush(SignUp);
        return result.setAt0(false).setAt1("审核成功");
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("审核通过报名信息表");
        createTitle(workbook, sheet);
        List<JSONObject> resultList = generate(request);
        HSSFRow row = sheet.createRow(resultList.size() + 1);
        row.setHeightInPoints(18);
        int count = 1;
        for (JSONObject result : resultList) {
            row.createCell(0).setCellValue(count);
            row.createCell(1).setCellValue(result.get("userName").toString());
            row.createCell(2).setCellValue(result.get("sex").toString());
            row.createCell(3).setCellValue(result.get("deptNo").toString());
            row.createCell(4).setCellValue(result.get("joinTime").toString());
            row.createCell(5).setCellValue(result.get("armedRank").toString());
            row.createCell(6).setCellValue(result.get("soldierId").toString());
            row.createCell(7).setCellValue(result.get("idCard").toString());
            row.createCell(8).setCellValue(result.get("degree").toString());
            row.createCell(9).setCellValue(result.get("applySkillRank").toString());
            count++;
        }
        String fileName = "审核通过报名信息表.xls";

        //生成excel文件
        buildExcelFile(fileName, workbook);

        //浏览器下载excel
        buildExcelDocument(fileName, workbook, response);
    }

    private List<JSONObject> generate(HttpServletRequest request) throws IOException {
        List<JSONObject> resultlist = new ArrayList<>();
        String requestParam = IOUtils.toString(request.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
        JSONObject param = JSONObject.parseObject(requestParam);
        String applyWorkType = param.get("applyWorkType").toString();
        String createTime = param.get("createTime").toString();
        List<SignUp> reviewList = reviewRepository.getReviewList(applyWorkType, createTime);
        for (SignUp SignUp : reviewList) {
            JSONObject result = new JSONObject();
            User user = userRepository.findById(SignUp.getUserId()).get();
            result.put("userName", user.getUserName());
            result.put("sex", user.getUserExt().getSex());
            result.put("deptNo", user.getDeptno());
            result.put("joinTime", user.getJoinTime());
            result.put("armedRank", user.getArmedRank());
            result.put("soldierId", user.getSoldierId());
            result.put("idCard", user.getIdCard());
            result.put("degree", user.getUserExt().getDegree());
            result.put("applySkillRank", SignUp.getApplySkillRank());
            resultlist.add(result);
        }
        return resultlist;
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
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("性别");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("部别");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("入伍年月");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("军衔级别");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("士兵证号");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("身份证号");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("文化程度");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("政治面貌");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("申报专业等级");
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
