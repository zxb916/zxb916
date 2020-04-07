package com.example.demo.controller;

import com.example.demo.common.BaseController;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.model.SignUp;
import com.example.demo.model.User;
import com.example.demo.service.ReviewService;
import com.example.demo.service.SignUpService;
import com.example.demo.service.UserService;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Variant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@Api(value = "审核模块")
@RestController
@RequestMapping("/review")
public class ReviewController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String PATH_LIST = "/list";
    private static final String PATH_CHECK = "/check";
    private static final String PATH_GENERATE_REVIEW_LIST = "/generateReviewList";
    private static final String PATH_EDIT = "/edit";
    private static final String PATH_CREATE_REPORT = "/createReport";


    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private UserService userService;


    @ApiOperation(value = "获取列表")
    @GetMapping(PATH_LIST)
    public BaseResult list(@RequestParam(value = "applyWorkType", required = true) String applyWorkType, @RequestParam(value = "year", required = true) String year) {
        logger.info("获取报名列表");
        if (StringUtils.isEmpty(applyWorkType) || StringUtils.isEmpty(year)) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "申请工种和创建时间不能为空");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "获取成功!", reviewService.getList(applyWorkType, year));
    }

    @ApiOperation(value = "审核")
    @PostMapping(PATH_CHECK)
    public BaseResult check(HttpServletRequest request) {
        try {
            String requestParam = IOUtils.toString(request.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
            Pair<Boolean, String> result = reviewService.checkAndSave(requestParam);
            if (!result.getValue0()) {
                return new BaseResult(Constants.RESPONSE_CODE_200, result.getValue1());
            }
            return new BaseResult(Constants.RESPONSE_CODE_200, result.getValue1());
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new BaseResult(Constants.RESPONSE_CODE_500, "审核失败");
        }
    }

    @ApiOperation(value = "生成报表")
    @PostMapping(PATH_GENERATE_REVIEW_LIST)
    public BaseResult generateReviewList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        reviewService.exportExcel(request, response);
        return new BaseResult(Constants.RESPONSE_CODE_200, "审核成功!");
    }

    @ApiOperation(value = "添加准考证号")
    @PostMapping(PATH_EDIT)
    public BaseResult edit(String idCard, String passCard, String year) {
        logger.info("添加准考证号");
        try {
            User user = userService.getAdminByIdCard(idCard).getUser();
            SignUp signUp = signUpService.findByUserIdAndYear(user.getId(), year);
            signUp.setPassCard(passCard);
            signUpService.save(signUp);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult(Constants.RESPONSE_CODE_500, "逻辑异常!");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "审核成功!");
    }


    @ApiOperation(value = "生成报名表")
    @GetMapping(PATH_CREATE_REPORT)
    public void createReport(@RequestParam("idCard") String idCard, @RequestParam("year") String year, HttpServletResponse response) throws IOException {
        File reportFile = reviewService.build(idCard, year);
        if (reportFile == null) {
            new BaseResult(Constants.RESPONSE_CODE_500, "生成报表失败");
        }
        try {
//            wpsTopdf("C:\\Users\\huaruiview\\Desktop\\'士兵表明表.doc'", "C:\\Users\\huaruiview\\Desktop\\'test.doc'");
            logger.info("转换完成！");
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            // 设置浏览器以下载的方式处理该文件名
            response.setHeader("Content-Disposition", "attachment;filename=".concat(URLEncoder.encode("士兵报名表".concat(".doc"), "UTF-8")));
            IOUtils.write(FileUtils.readFileToByteArray(reportFile), response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getOutputStream().print("格式转化异常");
        }

        // IOUtils.copyLarge(new FileInputStream(reportFile), response.getOutputStream());
        // 删除临时文件
//        FileUtils.deleteQuietly(reportFile);
    }

    public boolean wpsTopdf(String srcFilePath, String pdfFilePath) throws Exception {
        // wps com
        ActiveXComponent pptActiveXComponent = null;
        ActiveXComponent workbook = null;
        // open thred
        ComThread.InitSTA();
        try {
            pptActiveXComponent = new ActiveXComponent("KWPP.Application");
            Variant openParams[] = {new Variant(srcFilePath), new Variant(true), new Variant(true)};
            workbook = pptActiveXComponent.invokeGetComponent("Documents").invokeGetComponent("Open", openParams);
            workbook.invoke("SaveAs", new Variant[]{new Variant(pdfFilePath), new Variant(17)});
        } catch (Exception e) {
            throw e;
        } finally {
            if (workbook != null) {
                workbook.invoke("Close");
                workbook.safeRelease();
            }
            if (pptActiveXComponent != null) {
                pptActiveXComponent.invoke("Quit");
                pptActiveXComponent.safeRelease();
            }
            ComThread.Release();
        }
        return true;
    }


}
