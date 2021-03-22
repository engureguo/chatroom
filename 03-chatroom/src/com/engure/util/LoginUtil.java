package com.engure.util;

import com.engure.constant.MyNet;
import com.engure.po.MyPackage;
import com.engure.po.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.Socket;

public class LoginUtil {

    /**
     * 登录服务端。使用 socket发送身份信息进行验证
     * @return 是否登录成功
     */
    public static boolean login(User user) {
        boolean isOk = false;
        MySocket mySocket = null;

        try {
            Socket socket = new Socket(MyNet.HOST, MyNet.PORT);
            mySocket = new MySocket(socket);
            String s = new ObjectMapper().
                    writeValueAsString(new MyPackage(1, user, null, null, null));
            //发送身份信息
            mySocket.sendMsg(s);
            //接收登录信息
            MyPackage myPackage = mySocket.recvMsg();

            if (myPackage.getMsg()!=null && myPackage.getMsg().equals("checked")) {
                isOk = true;
            } else {
                isOk = false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (mySocket!=null) {
                mySocket.close();//关闭socket
            }
        }

        return isOk;
    }

}
