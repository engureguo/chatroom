package com.engure.thread;

import com.engure.po.MyPackage;
import com.engure.util.AllUsersInfo;
import com.engure.util.MyTaskQueue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 接受连接的线程
 */
public class AcceptThread extends Thread {
    ServerSocket ss;
    MyTaskQueue<MyPackage> task_queue;
    AllUsersInfo infos;

    public AcceptThread(ServerSocket ss, MyTaskQueue<MyPackage> tq, AllUsersInfo infos) {
        task_queue = tq;
        this.ss = ss;
        this.infos = infos;
    }

    @Override
    public void run() {
        System.out.println("accepter running.....");

        while (true) {

            try {
                Socket accept = ss.accept();
                new SocketHandlerThread(task_queue, accept, infos).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}