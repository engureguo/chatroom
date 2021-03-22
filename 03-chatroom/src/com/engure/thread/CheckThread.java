package com.engure.thread;

import com.engure.util.AllUsersInfo;

import java.util.Arrays;

/**
 * 检查用户状态，并进行标记
 */
public class CheckThread extends Thread {
    AllUsersInfo infos;

    public CheckThread(AllUsersInfo infos) {
        this.infos = infos;
    }

    @Override
    public void run() {
        System.out.println("status-checker running.....");

        while (true) {

            /*System.out.println("在线用户："+ Arrays.toString(infos.getAllUsers()));*/
            System.out.println(infos);
            infos.checkStatus();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
