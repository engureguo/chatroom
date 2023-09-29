package com.engure.po;

import java.util.Objects;

public class User {
    private String userName;//账号
    private String realName;//真实姓名

    @Override
    public String toString() {
        return realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(realName, user.realName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, realName);
    }

    public User(String userName, String realName) {
        this.userName = userName;
        this.realName = realName;
    }
}
