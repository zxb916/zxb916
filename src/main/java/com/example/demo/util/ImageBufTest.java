package com.example.demo.util;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/4.
 */
public class ImageBufTest {

    //图片到byte数组
    public byte[] image2byte(String path) throws IOException {
        byte[] data = null;
        FileImageInputStream input = null;
        input = new FileImageInputStream(new File(path));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int numBytesRead = 0;
        while ((numBytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, numBytesRead);
        }
        data = output.toByteArray();
        output.close();
        input.close();
        return data;
    }
    //byte数组到图片
    public void byte2image(byte[] data,String path) throws IOException {
        if(data.length<3||path.equals("")) return;
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
        imageOutput.write(data, 0, data.length);
        imageOutput.close();
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        //先模拟一个图形byte[]
        byte[] b =new ImageBufTest().image2byte("D:/test/2.jpg");
        //byte[] b1 =new ImageBufTest().image2byte("D:/test/2.jpg");
        //byte[] b2 =new ImageBufTest().image2byte("D:/test/1.jpg");
        new ImageBufTest().byte2image(b, "./WebRoot/appuploadFiles/uploadImgs/c.jpg");
        //new ImageBufTest().byte2image(b1, "./WebRoot/appuploadFiles/uploadImgs/a.jpg");
        //new ImageBufTest().byte2image(b2, "./WebRoot/appuploadFiles/uploadImgs/ac.jpg");
        //存为文件
//		Buff2Image.buff2Image(b, "D:/test/b.jpg");
//		//通过URL读取图片获取bytes
//		byte[] b2=Image2buff.image2ByteFromUrl("http://avatar.csdn.net/3/5/F/1_huang9012.jpg");
//		Buff2Image.buff2Image(b2, "D:/test/a.jpg");
        System.out.println("Success!");
    }

}

