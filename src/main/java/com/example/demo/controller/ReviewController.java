package com.example.demo.controller;

import com.example.demo.common.BaseController;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.model.SignUp;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

@Api(value = "审核模块")
@RestController
@RequestMapping("/review")
public class ReviewController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private static final String PATH_LIST = "/list";
    private static final String PATH_CHECK = "/check";
    private static final String PATH_GENERATE_REVIEW_LIST = "/generateReviewList";
    private static final String PATH_EDIT = "/edit";
    private static final String PATH_CREATE_REPORT = "/createReport";


    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取列表")
    @GetMapping(PATH_LIST)
    public BaseResult list(String alreadyWorkType, String createTime) {
        return new BaseResult(Constants.RESPONSE_CODE_200, "获取成功!", reviewService.getList(alreadyWorkType, createTime));
    }

    @ApiOperation(value = "审核")
    @PostMapping(PATH_CHECK)
    public BaseResult check(HttpServletRequest request) {
        try {
            String requestParam = IOUtils.toString(request.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
            Pair<Boolean, String> result = reviewService.checkAndSave(requestParam);
            if (!result.getValue0()) {
                return new BaseResult(Constants.RESPONSE_CODE_500, result.getValue1());
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
    public BaseResult edit(String idCard, String passCard) {
        User user = userService.getAdminByIdCard(idCard).getUser();
        SignUp signup = reviewRepository.findByUserId(user.getId());
        signup.setPassCard(passCard);
        reviewRepository.saveAndFlush(signup);
        return new BaseResult(Constants.RESPONSE_CODE_200, "审核成功!");
    }


    @ApiOperation(value = "生成报名表")
    @GetMapping(PATH_CREATE_REPORT)
    public BaseResult createReport(@RequestParam("idCard") String idCard, @RequestParam("createTime") String createTime, HttpServletResponse response) throws IOException {
        File reportFile = reviewService.build(idCard,createTime);
        if (reportFile == null) {
            return new BaseResult(Constants.RESPONSE_CODE_500,"生成报表失败");
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        // 设置浏览器以下载的方式处理该文件名
        response.setHeader("Content-Disposition", "attachment;filename=".concat(URLEncoder.encode("士兵报名表".concat(".doc"), "UTF-8")));
        IOUtils.write(FileUtils.readFileToByteArray(reportFile), response.getOutputStream());
        // IOUtils.copyLarge(new FileInputStream(reportFile), response.getOutputStream());
        // 删除临时文件
//        FileUtils.deleteQuietly(reportFile);
        return new BaseResult(Constants.RESPONSE_CODE_200,"生成报表成功");
    }

}
