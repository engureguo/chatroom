package com.engure.util;

import com.engure.constant.MyStatus;
import com.engure.po.Msg;
import com.engure.po.User;
import com.engure.po.UserInfo;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * checkThread => checkStatus, getAllUsers
 * TaskMaker => refresh, getUser, sendMsgToAll
 * SocketHandlerThread => isExists, isOnline, addUser, setUser, getSbsMsg, getAllUser
 */
public class AllUsersInfo {

    HashMap<String, UserInfo> infos;//所有用户的全部信息

    public AllUsersInfo() {
        infos = new HashMap<>();
    }

    //更新用户最近登录时间 lastAccess
    public void refresh(String username){
        synchronized (infos){
            infos.get(username).refreshDate();
        }
    }

    @Override
    public String toString() {
        return "{" + infos + '}';
    }

    /**
     * 获取用户
     */
    public UserInfo getUser(String username) {
        UserInfo userInfo;
        synchronized (infos){
            userInfo = infos.get(username);
        }
        return userInfo;
    }

    //用户首次登录
    public void addUser(User user) {
        synchronized (infos){
            infos.put(user.getUserName(), new UserInfo(user));
        }
    }

    //获取xxx所有消息
    public Msg[] getSbsMsg(String username){
        Msg[] msgs;
        synchronized (infos) {
            msgs = infos.get(username).getAllMsg();
        }
        return msgs;
    }

    //获取在线用户
    public User[] getAllUsers() {
        LinkedList<User> linkedList = new LinkedList<>();
        synchronized (infos) {
            for (String s : infos.keySet()) {
                if (infos.get(s).getStatus() == MyStatus.ONLINE) {
                    linkedList.add(infos.get(s).getUser());
                }
            }
        }
        return MyArrayUtil.listToArray2(linkedList);
    }

    //判断用户是否存在
    public boolean isExist(String username) {
        boolean b;
        synchronized (infos) {
            b = infos.containsKey(username);
        }
        return b;
    }

    //判断用户是否在线
    public boolean isOnline(String username) {
        boolean b;
        synchronized (infos) {
            b = infos.get(username).getStatus().equals(MyStatus.ONLINE);
        }
        return b;
    }

    /**
     * CheckThread 检查并更改用户状态
     */
    public void checkStatus() {
        synchronized (infos) {
            for (String s : infos.keySet()) {
                long lastAccess = infos.get(s).getLastAccess().getTime();
                long now = System.currentTimeMillis();
                //2s没有响应则表示掉线
                if (Math.abs(now - lastAccess) > 1000*2) {
                    infos.get(s).setStatus(MyStatus.OFFLINE);
                } else {
                    infos.get(s).setStatus(MyStatus.ONLINE);
                }
            }
        }
    }

    /**
     * 发送消息给所有人
     * @param msg
     */
    public void sendMsgToAll(Msg msg) {
        synchronized (infos) {
            for (String s : infos.keySet()) {
                if (msg.getFrom().equals(s)){//自己不能公开发给自己
                    continue;
                }
                infos.get(s).addMsg(msg);
            }
        }
    }

    /**
     * 更新用户信息 user
     * @param user
     */
    public void setUser(User user) {
        synchronized (infos) {
            infos.get(user.getUserName()).setUser(user);
        }
    }

}
