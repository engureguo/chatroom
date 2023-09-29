package com.engure.po;

import java.util.Arrays;

/**
 * 数据包，客户端和服务端沟通的载体
 *
 * type=1
 *      user
 * type=2
 *      msgs
 * type=3
 *      msg、users
 *
 */
public class MyPackage {

    Integer type;//包类型，登录信息包1、发送消息信息包2、服务端消息包3
    User user;//登录信息包
    Msg[] msgs;//来往消息
    String msg;//服务器响应信息， ok/noway
    User[] users;//在线用户列表

    public MyPackage() {
    }

    public MyPackage(Integer type, User user, Msg[] msgs, String msg, User[] users) {
        this.type = type;
        this.user = user;
        this.msgs = msgs;
        this.msg = msg;
        this.users = users;
    }

    @Override
    public String toString() {
        return "Package{" +
                "type=" + type +
                ", user=" + user +
                ", msgs=" + Arrays.toString(msgs) +
                ", msg='" + msg + '\'' +
                ", users=" + Arrays.toString(users) +
                '}';
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Msg[] getMsgs() {
        return msgs;
    }

    public void setMsgs(Msg[] msgs) {
        this.msgs = msgs;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
