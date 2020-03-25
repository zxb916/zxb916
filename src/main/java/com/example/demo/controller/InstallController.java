/**
 * Copyright (C), 2015-2017, 迈道科技有限公司
 * FileName: BookController
 * Author:   Administrator
 * Date:     2017/11/13 17:42
 * Description: 控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.BaseController;
import com.example.demo.common.BaseResult;
import com.example.demo.common.Constants;
import com.example.demo.util.Ftp;
import com.example.demo.util.SSHUtils;
import com.jcraft.jsch.ChannelSftp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈控制器〉
 *
 * @author Administrator
 * @create 2017/11/13
 * @since 1.0.0
 */
@Api(value = "自动化部署安装")
@RestController
public class InstallController extends BaseController {

    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(InstallController.class);


    @ApiOperation(value = "测试接收json字符串")
    @PostMapping(value = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index(@RequestBody String body) {
        System.out.println(body);
        return "Hello World";
    }

    @ApiOperation(value = "测试string传参")
    @GetMapping("/test")
    public String test(@RequestParam("qs") String qs) {
        System.out.println(qs);
        return "Hello World";
    }

    @ApiOperation(value = "自动化安装部署接口")
    @PostMapping("/install")
    public BaseResult install(@RequestBody String str) {
        logger.info(str);
        Map<String, String> object = (Map<String, String>) JSONObject.parse(str);
        String ip = object.get("ip");
        int port = Integer.parseInt(object.get("port"));
        String user = object.get("root");
        String pwd = object.get("pwd");
        try {
            SSHUtils sshUtils = SSHUtils.getSSHUtils(ip, port, user, pwd);
            Ftp ftp = Ftp.getSftpUtil(ip, port, user, pwd);
            String command = "firewall-cmd --state";
            String result = "";
            result = sshUtils.execCommandByJSch(command);
            logger.info(result);
            sshUtils.execCommandByJSch("systemctl disable firewalld");
            sshUtils.execCommandByJSch("systemctl stop firewalld");
            List<String> list = new ArrayList<>();
            ftp.deleteDirectory("/usr/java", list);
            ftp.delete(list);
            ftp.makeDir("/usr/java");
            ChannelSftp channel = ftp.getChannel();
            channel.chmod(777, "/etc/rc.local");
            str = ftp.upload("/usr/java", "src/main/resources/app/jdk-8u121-linux-x64.tar.gz");
            logger.info(str);
            sshUtils.execCommandByJSch("cd /usr/java  && tar -zxvf /usr/java/jdk-8u121-linux-x64.tar.gz");
            ftp.download("/etc", "profile", "src/main/resources/data/profile");
            ftp.modifyFile("src/main/resources/data/profile", "src/main/resources/copy/profile");
            ftp.delete("/etc", "profile");
            ftp.upload("/etc", "src/main/resources/copy/profile");
            logger.info("修改profile文件成功");
            sshUtils.execCommandByShell("source /etc/profile");
            result = sshUtils.execCommandByShell("java -version");
            logger.info(result);
            result = sshUtils.execCommandByJSch("source /etc/profile&&source ~/.bash_profile&&source ~/.bashrc&&java -version");
            System.out.println(result);
            logger.info(str);
        } catch (Exception e) {
            e.printStackTrace();
            return sendResult(Constants.RESPONSE_CODE_500, "服务器异常");
        } finally {
            SSHUtils.release();
            Ftp.release();
        }
        return sendResult(Constants.RESPONSE_CODE_200, "安装成功", new Date());
    }

    public static void main(String[] args) throws Exception {
        String str = "{\n" +
                "\t\n" +
                "\t\"ip\":\"192.168.43.99\",\n" +
                "\t\"port\":22,\n" +
                "\t\"user\":\"root\",\n" +
                "\t\"pwd\":\"123456\"\n" +
                "\t\n" +
                "}";
        Map<String, String> object = (Map<String, String>) JSONObject.parse(str);
        System.out.println(object.toString());
    }


}
