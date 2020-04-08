package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/4/25.
 */
public class UploadFileUtils {
    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);


    public static String upload(MultipartFile uploadfile, String dirPath) throws NoSuchAlgorithmException, IOException {
        if (null == uploadfile) {
            logger.info("没有传文件或获取不到文件");
            return null;
        }
        // 解决中文问题，liunx下中文路径，图片显示问题
        String fileName = UUIDGenerator.generateUUID() + ".jpg";
        String uploadWholeFilePath = dirPath + fileName;
        logger.info("上传的文件及目录为：" + uploadWholeFilePath);
        File dest = new File(uploadWholeFilePath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        new ImageBufTest().byte2image(uploadfile.getBytes(), uploadWholeFilePath);
        logger.info("入库数据为：" + fileName);
        return fileName;
    }


}
