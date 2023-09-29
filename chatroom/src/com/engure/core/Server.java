package com.engure.core;

import com.engure.po.MyPackage;
import com.engure.thread.AcceptThread;
import com.engure.thread.CheckThread;
import com.engure.thread.TasksMaker;
import com.engure.util.AllUsersInfo;
import com.engure.util.MyTaskQueue;
import java.net.ServerSocket;

public class Server {
    static int PORT = 8899;
    MyTaskQueue<MyPackage> taskQueue;//用户发送来的消息
    AllUsersInfo infos;//所有用户的全部数据
    ServerSocket ss;//服务器socket

    AcceptThread acceptThread;//接受socket连接
    CheckThread checkThread;//检查用户状态
    TasksMaker tasksMaker;//处理任务队列

    public Server() {
        taskQueue = new MyTaskQueue<>();
        infos = new AllUsersInfo();
    }

    public void startServer() {
        try {
            ss = new ServerSocket(PORT);//启动serversocket
            acceptThread = new AcceptThread(ss, taskQueue, infos);
            checkThread = new CheckThread(infos);
            tasksMaker = new TasksMaker(taskQueue, infos);
            tasksMaker.start();
            checkThread.start();
            acceptThread.start();
            System.out.println("ss.start");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ss.start failed");
        }

    }

    public static void main(String[] args) {

        new Server().startServer();

    }

}
