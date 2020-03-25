package com.example.demo.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Ftp {

    //打印log日志
    private static final Logger logger = LoggerFactory.getLogger(Ftp.class);

    private static Date last_push_date = null;

    private Session sshSession;

    private ChannelSftp channel;

    private static ThreadLocal<Ftp> sftpLocal = new ThreadLocal<>();

    public ChannelSftp getChannel() {
        return channel;
    }

    private Ftp(String host, int port, String username, String password) throws Exception {
        JSch jsch = new JSch();
        jsch.getSession(username, host, port);
        //根据用户名，密码，端口号获取session
        sshSession = jsch.getSession(username, host, port);
        sshSession.setPassword(password);
        //修改服务器/etc/ssh/sshd_config 中 GSSAPIAuthentication的值yes为no，解决用户不能远程登录
        sshSession.setConfig("userauth.gssapi-with-mic", "no");

        //为session对象设置properties,第一次访问服务器时不用输入yes
        sshSession.setConfig("StrictHostKeyChecking", "no");
        sshSession.connect();
        //获取sftp通道
        channel = (ChannelSftp) sshSession.openChannel("sftp");
        channel.connect();
        logger.info("连接ftp成功!");
    }

    /**
     * 是否已连接
     *
     * @return
     */
    private boolean isConnected() {
        return null != channel && channel.isConnected();
    }

    /**
     * 获取本地线程存储的sftp客户端
     *
     * @return
     * @throws Exception
     */
    public static Ftp getSftpUtil(String host, int port, String username, String password) throws Exception {
        //获取本地线程
        Ftp sftpUtil = sftpLocal.get();
        if (null == sftpUtil || !sftpUtil.isConnected()) {
            //将新连接防止本地线程，实现并发处理
            sftpLocal.set(new Ftp(host, port, username, password));
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
     *
     * @throws Exception
     */
    public void closeChannel() {
        if (null != channel) {
            try {
                channel.disconnect();
            } catch (Exception e) {
                logger.error("关闭SFTP通道发生异常:", e);
            }
        }
        if (null != sshSession) {
            try {
                sshSession.disconnect();
            } catch (Exception e) {
                logger.error("SFTP关闭 session异常:", e);
            }
        }
    }


    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     */
    public String upload(String directory, String uploadFile) {
        try {
            if (!directory.equals("")) {
                channel.cd(directory);
            }
            File file = new File(uploadFile);
            channel.put(new FileInputStream(file), file.getName());
            System.out.println("上传完成");
            return "传输成功";
        } catch (Exception e) {
            e.printStackTrace();
            channel.disconnect();
            try {
                channel.getSession().disconnect();
            } catch (JSchException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return "传输失败，请检查配置！";
        }
    }


    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public void download(String directory, String downloadFile, String saveFile) {
        try {
            channel.cd(directory);
            File file = new File(saveFile);
            channel.get(downloadFile, new FileOutputStream(file));
            System.out.println("download ok");
        } catch (Exception e) {
            channel.disconnect();
            try {
                channel.getSession().disconnect();
            } catch (JSchException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }


    /**
     * 删除文件
     *
     * @param direct 要删除文件所在目录
     * @param direct 文件
     */
    public void delete(String direct, String file) {
        try {
            channel.cd(direct);
            channel.rm(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param list 要删除文件所在目录
     */
    public void delete(List<String> list) {
        try {
            for (int i = list.size() - 1; i >= 0; i--) {
                channel.rmdir(list.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件件
     *
     * @param path 要删除文件路劲
     */
    public void deleteDirectory(String path, List<String> list) {
        try {
            Vector<ChannelSftp.LsEntry> vector = listFiles(path);
            list.add(path);
            for (int i = 0; i < vector.size(); i++) {
                if (vector.get(i).getFilename().equals(".") || vector.get(i).getFilename().equals("..")) {
                    vector.remove(i);
                    i--;
                    continue;
                }
                try {
                    channel.rm(path + "/" + vector.get(i).getFilename());
                    vector.remove(i);
                    i--;
                } catch (Exception e) {
                    logger.info("目录文件" + path);
                }
            }
            vector.forEach(o -> this.deleteDirectory(path + "/" + o.getFilename(), list));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void makeDir(String path) throws SftpException {
        channel.mkdir(path);

    }


    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory)
            throws SftpException {
        return channel.ls(directory);
    }


    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException, JSchException {
        try {
            channel.cd(directory);
        } catch (SftpException e) {
            logger.warn("directory is not exist");
            channel.mkdir(directory);
            channel.cd(directory);
        }
        channel.put(input, sftpFileName);
        logger.info("file:{" + sftpFileName + "} is upload successful");
    }


    public void modifyFile(String orginPath, String savePath) throws IOException {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(orginPath);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String contentLine;
            List<String> arr1 = new ArrayList<>();
            while ((contentLine = br.readLine()) != null) {
                //读取每一行，并输出
//                System.out.println(contentLine);
                //将每一行追加到arr1
                arr1.add(contentLine);
            }
            arr1.add(arr1.size(), "export JAVA_HOME=/usr/java/jdk1.8.0_121");
            arr1.add(arr1.size(), "export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar");
            arr1.add(arr1.size(), "export PATH=$JAVA_HOME/bin:$PATH");
            fw = new FileWriter(savePath);
            bw = new BufferedWriter(fw);
            for (String o : arr1) {
                bw.write(o + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bw.flush();
            fw.close();
            bw.close();
            fr.close();
            br.close();
        }
    }


    public static void main(String[] args) throws Exception {
        Ftp ftp = new Ftp("192.168.43.99", 22, "root", "123456");
        ftp.upload("/root", "D:\\abc.jar");
        ftp.closeChannel();
    }
}
