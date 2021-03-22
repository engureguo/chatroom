package com.engure.util;

import com.engure.po.Msg;
import com.engure.po.User;

import java.util.List;

public class MyArrayUtil {

    public static Msg[] listToArray1(List<Msg> list) {
        Msg[] msgs = new Msg[list.size()];
        for (int i = 0; i < list.size(); i++) {
            msgs[i] = list.get(i);
        }
        return msgs;
    }

    public static User[] listToArray2(List<User> list) {
        User[] ss = new User[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ss[i] = list.get(i);
        }
        return ss;
    }

}
