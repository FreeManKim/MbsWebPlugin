package com.mobisoft.mbswebplugin.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Author：Created by fan.xd on 2016/11/25.
 * Email：fang.xd@mobisoft.com.cn
 * Description：文件64位字符编码的转换
 * decoderBase64File
 * encodeBase64File
 */

public class Base64Util {
    /**
     * decoderBase64File:(将base64字符解码保存文件)
     * @param base64Code 编码后的字串
     * @param savePath  文件保存路径
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
//byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }
    /**
     * encodeBase64File:(将文件转成base64 字符串)
     * @param path 文件路径
     * @return
     * @throws Exception
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }


}
