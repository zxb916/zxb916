package com.example.demo.util;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;


public class SSHUtils {

    private static final String ENCODING = "UTF-8";
    private static final int timeout = 60 * 60 * 1000;
    private static ThreadLocal<SSHUtils> sftpLocal = new ThreadLocal<>();

    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(SSHUtils.class);
    private Session sshSession;

    private SSHUtils(String ip, int port, String user, String pwd) throws JSchException {
        JSch jsch = new JSch();
        sshSession = jsch.getSession(user, ip, port);
        sshSession.setPassword(pwd);
        sshSession.setConfig("StrictHostKeyChecking", "no");  // 第一次访问服务器时不用输入yes
        sshSession.setTimeout(timeout);
        sshSession.connect();
        logger.info("连接sshUtil成功!");
    }

    public static SSHUtils getSSHUtils(String host, int port, String userName, String pwd) throws JSchException {
        //获取本地线程
        SSHUtils sshUtils = sftpLocal.get();
        if (null == sshUtils) {
            //将新连接防止本地线程，实现并发处理
            sftpLocal.set(new SSHUtils(host, port, userName, pwd));
        }
        return sftpLocal.get();
    }


    /**
     * 释放本地线程存储的sftp客户端
     */
    public static void release() {
        if (null != sftpLocal.get()) {
            sftpLocal.get().closeChannel();
            logger.info("关闭连接" + sftpLocal.get().sshSession);
            sftpLocal.set(null);

        }
    }

    /**
     * 关闭通道
     */
    public void closeChannel() {
        if (null != sshSession) {
            try {
                sshSession.disconnect();
            } catch (Exception e) {
                logger.error("sshUtil关闭 session异常:", e);
            }
        }
    }

    /**
     * 执行单条命令 多条命令用 封号; 与&& 或|| 链接
     * （封号不管前一条命令是否执行成功 与 必须前一条命令成功 或 前一条命令执行不成功）
     *
     * @param command 执行命令
     * @return 返回结果
     * @throws IOException
     * @throws JSchException
     */
    public String execCommandByJSch(String command) throws IOException, JSchException {
        ChannelExec channelExec = (ChannelExec) sshSession.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String result = IOUtils.toString(in, ENCODING);
        channelExec.disconnect();
        return result;
    }

    /**
     * shell管道本身就是交互模式的。要想停止，有两种方式：
     * 一、人为的发送一个exit命令，告诉程序本次交互结束
     * 二、使用字节流中的available方法，来获取数据的总大小，然后循环去读。
     * 为了避免阻塞
     */
    public String execCommandByShell(String command) throws IOException, JSchException {
        StringBuilder results = new StringBuilder();
        //2.尝试解决 远程ssh只能执行一句命令的情况
        ChannelShell channelShell = (ChannelShell) sshSession.openChannel("shell");
        InputStream inputStream = channelShell.getInputStream();
        //从远端到达的数据  都能从这个流读取到
        channelShell.setPty(true);
        channelShell.connect();
        OutputStream outputStream = channelShell.getOutputStream();
        //写入该流的数据  都将发送到远程端
        // 使用PrintWriter 就是为了使用println 这个方法
        // 好处就是不需要每次手动给字符加\n
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(command);
        printWriter.println("exit");
        //为了结束本次交互
        printWriter.flush();//把缓冲区的数据强行输出
        byte[] tmp = new byte[1024];
        while (true) {
            while (inputStream.available() > 0) {
                int i = inputStream.read(tmp, 0, 1024);
                if (i < 0) break;
                String s = new String(tmp, 0, i);
                if (s.contains("--More--")) {
                    outputStream.write((" ").getBytes());
                    outputStream.flush();
                }
                results.append(s);
                System.out.println(s);
            }
            if (channelShell.isClosed()) {
                System.out.println("exit-status:" + channelShell.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {

            }
        }
        outputStream.close();
        inputStream.close();
        channelShell.disconnect();
        System.out.println("DONE");
        return results.toString();
    }


}
