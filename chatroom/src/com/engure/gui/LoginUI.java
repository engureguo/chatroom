package com.engure.gui;

import com.engure.po.User;
import com.engure.util.LoginUtil;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    static int X = 100;
    static int Y = 100;
    static int WIDTH = 350;
    static int HEIGHT = 250;

    JButton btnLogin;
    JLabel labelUserName;
    JLabel labelRealName;
    JLabel labelTitle;
    JLabel labelNote;
    JTextField texFldUserName;
    JTextField texFldRealName;

    JPanel panel0, panel01, panel1, panel2, panel3;

    public LoginUI() {
        init();
    }

    public void createComponent() {
        btnLogin = new JButton("login");
        labelUserName = new JLabel("user name");
        labelRealName = new JLabel("real name");
        labelTitle = new JLabel("welcome to chatroom!");
        labelNote = new JLabel("Hi~ o(*￣▽￣*)ブ");
        texFldUserName = new JTextField(15);
        texFldRealName = new JTextField(15);

        panel0 = new JPanel();
        panel01 = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
    }

    public void designLayout() {

        labelNote.setForeground(new Color(255,0,0));

        /////////////////////////

        panel0.add(labelTitle);

        panel01.add(labelNote);

        panel1.add(labelUserName);
        panel1.add(texFldUserName);

        panel2.add(labelRealName);
        panel2.add(texFldRealName);

        panel3.add(btnLogin);

        ///////////////////////

        this.add(panel0);
        this.add(panel01);
        this.add(panel1);
        this.add(panel2);
        this.add(panel3);
    }

    public void init() {
        this.setBounds(X,Y,WIDTH,HEIGHT);
        this.setLayout(new GridLayout(5, 1, 5, 5));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createComponent();
        designLayout();
        addHandler();

        /*this.pack();*/
    }

    User user;

    public User getUser() {
        return user;
    }

    /**
     * 添加处理
     */
    private void addHandler() {
        btnLogin.addActionListener((event)->{
            String realName = texFldRealName.getText();
            String userName = texFldUserName.getText();

            if (userName==null || userName.trim().equals("")) {
                texFldUserName.setText("");
                labelNote.setText("账号非法!");
                /*texFldUserName.set*/
            } else {
                labelNote.setText("Hi~ o(*￣▽￣*)ブ");
                //登录。。。。
                user = new User(userName, realName);
                boolean isOk = LoginUtil.login(user);
                if (isOk) {
                    /*labelNote.setText("登陆成功！");*/
                    this.setVisible(false);

                } else {
                    texFldUserName.setText("");
                    labelNote.setText("登录失败!");
                }

            }


        });
    }

    /*public static void main(String[] args) {
        new MyFrame().setVisible(true);
    }*/

}