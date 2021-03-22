package com.engure.thread;

import com.engure.po.MyPackage;
import com.engure.util.AllUsersInfo;
import com.engure.util.MySocket;
import com.engure.util.MyTaskQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.Socket;

/**
 * 处理指定连接的线程
 * accept()后得到socket，每个socket有一个线程来处理
 */
public class SocketHandlerThread extends Thread {
    MyTaskQueue<MyPackage> taskQueue;//加入任务队列
    AllUsersInfo infos;//处理登录请求
    MySocket mySocket;//当前socket

    public SocketHandlerThread(MyTaskQueue<MyPackage> tq, Socket so, AllUsersInfo infos) {
        mySocket = new MySocket(so);
        taskQueue = tq;
        this.infos = infos;
    }

    @Override
    public void run() {
        System.out.println("a handler......");

        //1.接受用户发的数据
        MyPackage myPackage = mySocket.recvMsg();
        //构建待发的数据包
        MyPackage packageSend = new MyPackage(3, null, null, null, null);
        //当前用户userName
        String userName = myPackage.getUser().getUserName();

        if (myPackage.getType() == 1) {
            /*用户登录
             1.如果第一次登录则 checked
             2.如果是二次登录
                  1.如果在线，则登陆失败 failed
                  2.如果离线，则登陆成功 checked*/
            if (!infos.isExist(userName)) {//创建用户信息
                infos.addUser(myPackage.getUser());
                packageSend.setMsg("checked");
            } else if (!infos.isOnline(userName)){//二次登录
                infos.refresh(userName);
                infos.setUser(myPackage.getUser());
                packageSend.setMsg("checked");
                packageSend.setMsgs(infos.getSbsMsg(userName));
            } else if (infos.isOnline(userName)){//登陆失败
                packageSend.setMsg("failed");
            } else {
                System.out.println("type=1, error...");
            }

        } else if (myPackage.getType() == 2){//发送消息
            infos.refresh(userName);
            taskQueue.addToLast(myPackage);
            packageSend.setMsgs(infos.getSbsMsg(userName));
        }

        //携带在线用户
        packageSend.setUsers(infos.getAllUsers());

        //2.返回用户需要的信息
        String s;
        try {
            s = new ObjectMapper().writeValueAsString(packageSend);
        } catch (Exception e) {
            e.printStackTrace();
            s="{}";
        }
        mySocket.sendMsg(s);

        //3.关闭连接
        try {
            //保证发送成功
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /**
             * 关闭
             */
            if (mySocket!=null) {
                mySocket.close();
            }

        }

        System.out.println("断开了与"+userName+"的连接。。。。");
    }

}
