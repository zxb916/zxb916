package com.example.demo.controller;


import com.example.demo.bo.SignUser;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.component.JwtTokenUtil;
import com.example.demo.model.Attach;
import com.example.demo.model.SignUp;
import com.example.demo.model.User;
import com.example.demo.service.AttachService;
import com.example.demo.service.SignUpService;
import com.example.demo.service.UserExtService;
import com.example.demo.service.UserService;
import com.example.demo.util.UploadFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(value = "报名")
@RestController
@RequestMapping("/sign")
public class SignUpController {

    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SignUpService signUpService;
    @Autowired
    private UserExtService userExtService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private AttachService attachService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;


    @ApiOperation(value = "新增报名")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult register(@RequestBody SignUser signUpUser) {
        logger.info("新增修改报名");
        try {
            User user = signUpUser.getUser();
            SignUp signUp = signUpUser.getSignUp();
            if (user.getId() == null) {
                return new BaseResult(Constants.RESPONSE_CODE_404, "用户id不能为空");
            }
            userService.update(user);
            signUp.setUser(user);
            signUpService.save(signUp);
        } catch (Exception e) {
            return new BaseResult(Constants.RESPONSE_CODE_500, "逻辑异常");
        }
        return new BaseResult(Constants.RESPONSE_CODE_200, "报名成功");
    }

    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public BaseResult upload(MultipartFile uploadfile) throws IOException {
        String imagePath = null;
        try {
            imagePath = UploadFileUtils.upload(uploadfile);
            if (imagePath == null) {
                return new BaseResult(Constants.RESPONSE_CODE_404, "头像不能为空", null);
            }
            imagePath = "http://192.168.3.55:9090/static/" + imagePath;
            Attach attach = new Attach();
            attach.setPath(imagePath);
            attach.setUuid(imagePath.substring(0, imagePath.length() - 4));
            attachService.save(attach);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult(Constants.RESPONSE_CODE_500, "头像上传失败", null);
        }
//        logger.debug(SignUpController.class.getClassLoader().getResource("static/wgb.png").getPath());
        return new BaseResult(Constants.RESPONSE_CODE_200, "头像上传成功", imagePath);
    }

}
