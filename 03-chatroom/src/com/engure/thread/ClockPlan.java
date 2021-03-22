package com.engure.thread;

import com.engure.constant.MyMsgType;
import com.engure.constant.MyNet;
import com.engure.gui.ChatRoom;
import com.engure.po.Msg;
import com.engure.po.MyPackage;
import com.engure.po.User;
import com.engure.util.MyArrayUtil;
import com.engure.util.MySocket;
import com.engure.util.MyTaskQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * 每隔 300ms 请求一次，将任务队列中的消息发送给服务端，
 * 同时从服务端获取数据【消息和在线用户】对UI进行更新
 */
public class ClockPlan extends Thread {
    MyTaskQueue<Msg> taskQueue;//任务队列
    User user;//当前用户
    ChatRoom chatRoom;//可见性

    public ClockPlan(MyTaskQueue<Msg> tq, User user, ChatRoom chatRoom) {
        taskQueue = tq;
        this.user = user;
        this.chatRoom = chatRoom;
    }

    @Override
    public void run() {

        while(true) {

            MySocket mySocket = null;

            try{
                Socket socket = new Socket(MyNet.HOST, MyNet.PORT);
                mySocket = new MySocket(socket);

                //获取缓冲区内容
                Msg[] msgs = MyArrayUtil.listToArray1(taskQueue.getAll());
                MyPackage myPackage = new MyPackage(2, user, msgs, null, null);
                String s = new ObjectMapper().writeValueAsString(myPackage);
                //发送消息
                mySocket.sendMsg(s);

                //接收消息
                MyPackage myPackage2 = mySocket.recvMsg();
                System.out.println(myPackage2);//打印接收的package

                //解析package，更新用户列表
                User[] users = myPackage2.getUsers();
                if (beforeRefresh(users, chatRoom.getUsers())) {//列表有改变时才刷新。
                    chatRoom.refreshUsers(users);
                }
                //解析package，更新消息列表
                Msg[] msgs1 = myPackage2.getMsgs();
                for (Msg msg : msgs1) {
                    chatRoom.addMsg(
                            chatRoom.getUsers().get(msg.getFrom()).getRealName() //realName of msg's sender
                            , msg.getTex()  // msg text
                            , msg.getTargets().equals("all")? MyMsgType.RECV_FROM_ALL:MyMsgType.RECV_FROM_SB); // msg type
                }

                //有时登陆无用户列表

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                /**
                 * 关闭socket
                 */
                if (mySocket!=null) {
                    mySocket.close();
                }

            }

            /**
             * 300ms请求一次
             */
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 比较两个集合异同
     * @param users
     * @param usersSet
     * @return 是否刷新
     */
    private boolean beforeRefresh(User[] users, HashMap<String, User> usersSet) {
        //数量不同时需要更新
        if ((users.length+1) != usersSet.size()) {
            return true;
        }
        Set<String> sets = usersSet.keySet();
        for (int i = 1; i < users.length; i++) {
            User u = users[i];
            if (!sets.contains(u.getUserName())) {//只用比较 username
                //有新内容
                return true;
            }
        }
        //不更新
        return false;
    }

}