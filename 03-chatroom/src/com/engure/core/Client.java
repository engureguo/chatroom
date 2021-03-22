package com.engure.core;

import com.engure.gui.ChatRoom;
import com.engure.gui.LoginUI;
import com.engure.po.Msg;
import com.engure.po.User;
import com.engure.thread.ClockPlan;
import com.engure.util.MyTaskQueue;

public class Client {

    ChatRoom chatRoom;//将要发送的信息放入taskQueue
    volatile LoginUI loginUI;//可见性

    User user;
    ClockPlan plan;//发送缓冲区的msg，更新UI
    MyTaskQueue<Msg> taskQueue;

    public Client() {
        loginUI = new LoginUI();
        chatRoom = new ChatRoom();
        taskQueue = new MyTaskQueue<>();
    }

    /**
     * 开启客户端
     */
    private void startClient() {
        loginUI.setVisible(true);

        while (loginUI.isVisible()) {
            //空转。直到登陆成功
        }
        user = loginUI.getUser();

        /*loginUI.dispose();//销毁登陆窗体*/

        plan = new ClockPlan(taskQueue, user, chatRoom);
        chatRoom.setInfos(taskQueue, user, plan);
        chatRoom.setVisible(true);

    }

    public static void main(String[] args) {

        /*new LoginUI().setVisible(true);*/
        /*new ChatRoom().setVisible(true);*/

        new Client().startClient();

    }


}
