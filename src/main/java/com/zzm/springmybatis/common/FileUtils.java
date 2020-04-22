package com.zzm.springmybatis.common;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * @author zzm
 * @data 2020/3/11 9:37
 */
public class FileUtils {

    public static void downloadFile(HttpServletResponse response, String filePath, String fileName) throws UnsupportedEncodingException {
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),"iso-8859-1"));
        byte[] buffer = new byte[1024];
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
//            is = new ByteArrayInputStream(file);
            bis = new BufferedInputStream(new FileInputStream(filePath));
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] uploadFile(MultipartFile file) {
        byte[] fileByte = null;
        try {
            //读取文件内容
            InputStream inputStream = file.getInputStream();
            fileByte = new byte[(int) file.getSize()];
            inputStream.read(fileByte);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileByte;
    }
}
