package com.engure.thread;

import com.engure.po.Msg;
import com.engure.po.MyPackage;
import com.engure.util.AllUsersInfo;
import com.engure.util.MyTaskQueue;

/**
 * task maker
 */
public class TasksMaker extends Thread {
    MyTaskQueue<MyPackage> taskQueue;
    AllUsersInfo infos;

    public TasksMaker(MyTaskQueue<MyPackage> taskQueue, AllUsersInfo infos) {
        this.taskQueue = taskQueue;
        this.infos = infos;
    }

    @Override
    public void run() {
        //将任务队列一个一个清空
        System.out.println("task maker running.....");

        while (true) {

            if (!taskQueue.isEmpty()) {
                MyPackage myPackage = taskQueue.removeFirst();
                Msg[] msgs = myPackage.getMsgs();//一个package可能包含多条msg
                infos.refresh(myPackage.getUser().getUserName());

                for (Msg msg : msgs) {
                    if (msg.getFrom().equals(msg.getTo())){//自己不能私发给自己
                        continue;
                    }
                    String targets = msg.getTargets();
                    if (targets.equals("someone")) {//私信
                        infos.getUser(msg.getTo()).addMsg(msg);

                    } else if (targets.equals("all")) {//公开
                        infos.sendMsgToAll(msg);
                    }

                }

            } else {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}