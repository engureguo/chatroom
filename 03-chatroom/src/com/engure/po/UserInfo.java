package com.engure.po;

import com.engure.constant.MyStatus;
import com.engure.util.MyArrayUtil;
import com.engure.util.MyTaskQueue;

import java.util.Date;

public class UserInfo {
    User user;//账号、姓名
    Date lastAccess;//上次何时登录
    MyStatus status;//是否在线
    MyTaskQueue<Msg> taskQueue;//收到的消息

    public UserInfo() {
    }

    /**
     * 用户第一次登录时，激活信息
     * @param user
     */
    public UserInfo(User user) {
        this.user = user;
        lastAccess = new Date(System.currentTimeMillis());
        taskQueue = new MyTaskQueue<>();
        status = MyStatus.ONLINE;
    }

    public void refreshDate() {
        lastAccess.setTime(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user=" + user +
                ", lastAccess=" + lastAccess +
                ", status='" + status + '\'' +
                ", taskQueue=" + taskQueue +
                "}\n";
    }

    /**
     * 获取此用户的所有信息
     */
    public Msg[] getAllMsg() {
        return MyArrayUtil.listToArray1(taskQueue.getAll());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 上次交互时间
     * @return
     */
    public Date getLastAccess() {
        return lastAccess;
    }

    public MyStatus getStatus() {
        return status;
    }

    /**
     * 设置用户的状态
     */
    public void setStatus(MyStatus status) {
        this.status = status;
    }

    /**
     * 向此用户的消息队列中添加ms
     */
    public synchronized void addMsg(Msg msg) {
        taskQueue.addToLast(msg);
    }

}
