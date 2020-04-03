package com.example.demo.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import java.io.*;

public class ImgBase64 {


    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr      图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        //
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public static void main(String[] args) {

        String code = ImgBase64.getImgStr("src/main/resources/1.jpg");
        System.out.println(code);
    }

}
