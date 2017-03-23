package com.mobisoft.mbswebplugin.proxy.Cache;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Author：Created by fan.xd on 2017/2/10.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class Client {
//    public static final String IP_ADDR = "localhost";//服务器地址
    public static final String IP_ADDR = "127.0.0.1";//服务器地址
    public static final int PORT = 8182;//服务器端口号
    public static final String TAG = "缓存，manifest";

    public static void main(String[] args) {
        System.out.println("客户端启动...");
        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");
        Log.i(TAG, "args参数:" + args[0]);
        Socket socket;
        try {// 创建一个Socket对象，并指定服务端的IP及端口号
            socket = new Socket(IP_ADDR, PORT);
            // 创建一个InputStream用户读取要发送的文件。
            ByteArrayInputStream stringInputStream = new ByteArrayInputStream(
                    args[0].getBytes());
//            InputStream inputStream = new FileInputStream("e://a.txt");
            // 获取Socket的OutputStream对象用于发送数据。
            OutputStream outputStream = socket.getOutputStream();
            // 创建一个byte类型的buffer字节数组，用于存放读取的本地文件
            byte buffer[] = new byte[1024];
            int temp = 0;
            // 循环读取文件
            while ((temp = stringInputStream.read(buffer)) != -1) {
                // 把数据写入到OuputStream对象中
                outputStream.write(buffer, 0, temp);
            }
            // 发送读取的数据到服务端
            outputStream.flush();

            Log.i(TAG, "args参数:" + args[0]);
//            stringInputStream.close();
            /** 或创建一个报文，使用BufferedWriter写入,看你的需求 **/
//          String socketData = "[2143213;21343fjks;213]";
//          BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                  socket.getOutputStream()));
//          writer.write(socketData.replace("\n", " ") + "\n");
//          writer.flush();
            /************************************************/
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
