package com.engure.util;

import com.engure.po.MyPackage;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 资源统一管理
 */
public class MySocket {

    private Socket socket;
    private OutputStream os;//Closing the returned OutputStream will close the associated socket.
    private InputStream is;//Closing the returned InputStream will close the associated socket

    public MySocket(Socket socket) {
        this.socket = socket;
        try {
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过此socket发送消息
     * @param s  json字符串
     */
    public void sendMsg(String s) {

        try {
            os.write(s.getBytes());
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过此 socket接收消息
     * @return
     */
    public MyPackage recvMsg() {

        ByteArrayOutputStream baos = null;
        MyPackage myPackage = null;

        try {
            baos = new ByteArrayOutputStream();
            byte[] bs=new byte[256];
            int len;
            while ((len = is.read(bs)) != -1) {
                baos.write(bs, 0, len);
            }
            String s = baos.toString("utf-8");
            myPackage = new ObjectMapper().readValue(s, MyPackage.class);
        } catch (Exception e) {
            e.printStackTrace();
            myPackage = new MyPackage();
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return myPackage;
    }


    /**
     * 关闭资源
     */
    public void close() {
        try {
            if (is!=null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (os!=null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket!=null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
