package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Resume;
import com.example.demo.model.SignUp;
import com.example.demo.model.Train;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.SignUpRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReviewService;
import com.example.demo.util.ImgBase64;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Value("${web.upload-path}")
    private String uploadPath;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignUpRepository signUpRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    private static final String PATH_DOC_TEMPLATE = "/";

    private Configuration ftlConfig = null;

    public ReviewServiceImpl() {
        ftlConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        ftlConfig.setDefaultEncoding("utf-8");
    }

    @Override
    public List<JSONObject> getList(String applyWorkType, String year) {
        List<SignUp> signupList = null;
        if (applyWorkType.equals("全部")) {
            signupList = reviewRepository.getListAll(year);
        } else {
            signupList = reviewRepository.getList(applyWorkType, year);
        }
        List<JSONObject> result = new ArrayList<>();
        for (SignUp signup : signupList) {
            Optional<User> opt = userRepository.findById(signup.getUserId());
            User user = opt.get();
            JSONObject obj = new JSONObject();
            obj.put("userName", user.getUserName());
            obj.put("idCard", user.getIdCard());
            obj.put("soldierId", user.getSoldierId());
            obj.put("applyWorkType", signup.getApplyWorkType());
            obj.put("applySkillRank", signup.getApplySkillRank());
            obj.put("review", signup.getReview());
            obj.put("passCard", signup.getPassCard());
            result.add(obj);
        }
        return result;
    }

    @Override
    public void checkAndSave(String idCard, String reviewOption, Integer review) {

        User user = userRepository.findByIdCardLike(idCard).get(0);
        SignUp signup = reviewRepository.findByUserId(user.getId());
        signup.setReviewOption(reviewOption);
        signup.setReview(review);
        reviewRepository.save(signup);
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
        for (SignUp signup : reviewList) {
            JSONObject result = new JSONObject();
            User user = userRepository.findById(signup.getUserId()).get();
            result.put("userName", user.getUserName());
            result.put("sex", user.getUserExt().getSex());
            result.put("deptNo", user.getDeptNo());
            result.put("joinTime", user.getJoinTime());
            result.put("armedRank", user.getArmedRank());
            result.put("soldierId", user.getSoldierId());
            result.put("idCard", user.getIdCard());
            result.put("degree", user.getUserExt().getDegree());
            result.put("applySkillRank", signup.getApplySkillRank());
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


    @Override
    public File build(String idCard, String year) {
        User user = userRepository.findByIdCardLike(idCard).get(0);
        SignUp signup = null;
        List<SignUp> signUps = signUpRepository.findByUserId(user.getId());
        for (SignUp sign : signUps) {
            if (StringUtils.startsWith(sign.getCreateTime(), year)) {
                signup = sign;
            }
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("name", "solider");
        resultMap.put("applyWorkType", signup.getApplyWorkType());
        resultMap.put("userName", user.getUserName());
        resultMap.put("createTime", signup.getCreateTime().split(" ")[0]);
        resultMap.put("userName", user.getUserName());
        resultMap.put("sex", user.getUserExt().getSex());
        resultMap.put("profilePhoto", StringUtils.isEmpty(user.getUserExt().getProfilePhoto()) ? "" : ImgBase64.getImgStr(uploadPath + user.getUserExt().getProfilePhoto().split("/")[user.getUserExt().getProfilePhoto().split("/").length - 1]));
        resultMap.put("idCard", user.getIdCard());
        resultMap.put("birthday", user.getUserExt().getBirthday() != null ? sdf1.format(user.getUserExt().getBirthday()) : "");
        resultMap.put("soldierId", user.getSoldierId());
        resultMap.put("degree", user.getUserExt().getDegree() == null ? "" : user.getUserExt().getDegree());
        resultMap.put("deptno", user.getDeptNo());
        resultMap.put("mobile", user.getMobile());
        resultMap.put("mailingAddress", user.getMailingAddress());
        resultMap.put("postCode", user.getUserExt().getPostCode());
        resultMap.put("alreadyWorkType", signup.getAlreadyWorkType() == null ? "" : signup.getAlreadyWorkType());
        resultMap.put("alreadySkillRank", signup.getApplySkillRank() == null ? "" : signup.getApplySkillRank());
        resultMap.put("alreadyCertificateNo", signup.getAlreadyCertificateNo() == null ? "" : signup.getAlreadyCertificateNo());
        resultMap.put("alreadyIssueDate", signup.getAlreadyIssueDate() == null ? "" : sdf1.format(signup.getAlreadyIssueDate()));
        resultMap.put("applyWorkType", signup.getApplyWorkType());
        resultMap.put("applySkillRank", signup.getApplySkillRank());
        if (signup.getChoice().equals("0")) {
            resultMap.put("choice", "理论知识√   操作技能□   综合评审□");
        } else if (signup.getChoice().equals("0,1")) {
            resultMap.put("choice", "理论知识√   操作技能√   综合评审□");
        } else if (signup.getChoice().equals("0,1,2")) {
            resultMap.put("choice", "理论知识√   操作技能√   综合评审√");
        } else if (signup.getChoice().equals("1,2")) {
            resultMap.put("choice", "理论知识□   操作技能√   综合评审√");
        } else if (signup.getChoice().equals("1")) {
            resultMap.put("choice", "理论知识□   操作技能√   综合评审□");
        } else if (signup.getChoice().equals("2")) {
            resultMap.put("choice", "理论知识□   操作技能□   综合评审√");
        }
        int index = 1;
        for (Resume resume : user.getResumes()) {
            resultMap.put("startTime" + index, resume.getStartTime());
            resultMap.put("endTime" + index, resume.getEndTime());
            resultMap.put("unit" + index, resume.getUnit());
            resultMap.put("majorName" + index, resume.getMajorName());
            index++;
        }
        for (int i = user.getResumes().size() + 1; i <= 4; i++) {
            resultMap.put("startTime" + i, "");
            resultMap.put("endTime" + i, "");
            resultMap.put("unit" + i, "");
            resultMap.put("majorName" + i, "");
        }
        index = 1;
        for (Train train : user.getTrains()) {
            resultMap.put("startTime" + index, train.getStartTime());
            resultMap.put("endTime" + index, train.getEndTime());
            resultMap.put("unit" + index, train.getUnit());
            resultMap.put("majorName" + index, train.getMajorName());
            resultMap.put("count" + index, train.getCount() == null ? "" : train.getCount().toString());
            index++;
        }
        for (int i = user.getTrains().size() + 1; i <= 4; i++) {
            resultMap.put("startTime" + i, "");
            resultMap.put("endTime" + i, "");
            resultMap.put("unit" + i, "");
            resultMap.put("majorName" + i, "");
            resultMap.put("count" + i, "");
        }
        return buildDoc(resultMap, "solider.ftl");
    }

    @Override
    public void saveAndFlush(SignUp signUp) {
        reviewRepository.save(signUp);
    }

    @SuppressWarnings("deprecation")
    public File buildDoc(Map<String, String> reportMap, String templateName) {
        Template template = null;
        Writer writer = null;
        File docFile = null;
        try {
            ftlConfig.setClassForTemplateLoading(this.getClass(), PATH_DOC_TEMPLATE);
            template = ftlConfig.getTemplate(templateName, "utf-8");
            docFile = new File(String.valueOf(reportMap.get("name")).concat(".doc"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile), "utf-8"));
            template.process(reportMap, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return docFile;
    }

    public File open(String filePath, String type, boolean visible) {
        String s = null;
        try {
            ActiveXComponent app = new ActiveXComponent(type);
            app.setProperty("Visible", new Variant(visible));
            Dispatch documents = app.getProperty("Documents").toDispatch();
            Dispatch document = Dispatch.call(documents, "Open", filePath).toDispatch();
            System.out.println("JacobUtil.class : 打开文档   " + filePath);
            try {
                s = (new File(filePath)).getParent() + ".pdf";
                Dispatch.call(document, "SaveAs", s, new Variant(17));
                System.out.println("生成的pdf文件位置：" + s);
            } catch (Exception e) {
                System.err.println("JacobUtil.class : 文档转换为pdf失败！");
                e.printStackTrace();
            }
            try {
                Dispatch.call(document, "Close", false);

                if (app != null) {
                    app.invoke("Quit", new Variant[]{});
                    app = null;
                }
                System.out.println("JacobUtil.class : 文档关闭  " + filePath);
            } catch (Exception e) {
                System.err.println("关闭ActiveXComponent异常");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("JacobUtil.class : 打开文档失败 " + filePath);
            e.printStackTrace();
        }
        return new File(s);
    }
}
