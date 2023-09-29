package com.engure.gui;

import com.engure.constant.MyMsgType;
import com.engure.po.Msg;
import com.engure.po.User;
import com.engure.thread.ClockPlan;
import com.engure.util.MyTaskQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ChatRoom extends JFrame{
    static int ROWS = 2;
    static int COLS = 1;
    static int HGAP = 10;
    static int VGAP = 8;
    static int X = 100;
    static int Y = 100;
    static int WIDTH = 450;
    static int HEIGHT = 650;

    JPanel panelinner, panel1, panel2;
    JScrollPane scrollPane;
    JPanel pNorth, pSouth;
    JComboBox<User> comboBox;
    JTextArea textArea;
    JButton btnSend;

    /////////////
    MyTaskQueue<Msg> taskQueue;
    User user;
    ClockPlan plan;

    /////////////

    /**
     * 信息传递
     * @param taskQueue
     * @param user
     * @param plan
     */
    public void setInfos(MyTaskQueue<Msg> taskQueue, User user, ClockPlan plan) {
        this.taskQueue = taskQueue;
        this.user = user;
        this.plan = plan;
        plan.start();
    }

    //create component
    public void createComponent() {

        panelinner = new JPanel();//放置JLabel组件
        panel1 = new JPanel();
        panel2 = new JPanel();
        scrollPane = new JScrollPane();//滑动面板，聊天记录

        pNorth = new JPanel();
        pSouth = new JPanel();

        comboBox = new JComboBox<>();//聊天室成员
        textArea = new JTextArea();//待发送的消息
        btnSend = new JButton("发送");//发送按钮

        comboBox.addItem(new User("","所有人"));
        /*scrollPane.setMaximumSize();
        scrollPane.setMinimumSize();*/
    }

    //design layout
    public void designUI() {

        panel1.setBackground(new Color(110, 90, 70));
        panel2.setBackground(new Color(125, 125, 125));

        ///////////// panel1 —— scrollpane

        //panelinner 垂直滑动
        scrollPane.setViewportView(panelinner);
        panelinner.setLayout(new GridLayout(0, 1));

        //scrollpane放在 panel1中央
        panel1.setLayout(new BorderLayout());
        panel1.add(scrollPane, BorderLayout.CENTER);

        ///////////// panel2 —— borderlayout

        panel2.setLayout(new BorderLayout());

        FlowLayout fln = new FlowLayout();
        fln.setAlignment(FlowLayout.LEFT);//【发送至+list】 —— 居左
        pNorth.setLayout(fln);
        pNorth.add(new JLabel("发送给："));
        pNorth.add(comboBox);
        panel2.add(pNorth, BorderLayout.NORTH);

        textArea.setFont(new Font(null, Font.PLAIN, 18));//改变字号
        panel2.add(textArea, BorderLayout.CENTER);

        FlowLayout fls = new FlowLayout();
        fls.setAlignment(FlowLayout.RIGHT);//【发送】 —— 居右
        pSouth.setLayout(fls);
        pSouth.add(btnSend);
        panel2.add(pSouth, BorderLayout.SOUTH);

        ///////////// frame

        this.add(panel1);
        this.add(panel2);

    }

    //add handlers
    private void addHandler() {

        btnSend.addActionListener(new MsgSendHandler());

    }

    public void test() {
        for (int i = 0; i < 10; i++) {
            addMsg("engure", "checkchecknow~~~", 0);
        }

        User[] us = new User[3];
        us[0] = new User("engure", "国小将");
        us[1] = new User("mary", "卖鱼");
        us[2] = new User("booler", "不嫩");
        refreshUsers(us);

    }

    //init UI
    public ChatRoom() {
        this.setBounds(X,Y,WIDTH,HEIGHT);
        this.setLayout(new GridLayout(ROWS, COLS, HGAP,VGAP));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createComponent();
        setting();
        designUI();
        addHandler();
        /*test();*/

    }

    /**
     * 组件相关设置
     */
    private void setting() {
        comboBox.setEditable(false);//下拉框不可编辑
        textArea.setLineWrap(true);//自动折行

    }

    /**
     * 向消息面板中添加消息 （名字、消息、类型）
     * 只做显示
     * @param username
     * @param msg
     * @param type 类型，自己群发/私发，别人群发/私发
     */
    public void addMsg(String username, String msg, int type) {
        JLabel labelName = new JLabel();
        if (type == MyMsgType.SEND_TO_SB) {
            labelName.setText("私发给["+username+"]");
        } else if (type == MyMsgType.SEND_TO_ALL) {
            labelName.setText("我");
        } else if (type == MyMsgType.RECV_FROM_SB) {
            labelName.setText("来自["+username+"]的私信");
        } else if (type == MyMsgType.RECV_FROM_ALL) {
            labelName.setText(username);
        } else {
            labelName.setText("error in ChatRoom.addMsg()");
        }
        JLabel labelMsg = new JLabel(msg);

        labelName.setForeground(new Color(57,223,153));
        labelName.setFont(new Font(null, Font.PLAIN, 25));
        labelMsg.setFont(new Font(null, Font.PLAIN, 18));

        panelinner.add(labelName);
        panelinner.add(labelMsg);

        //update ui
        panel1.updateUI();
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
    }

    /**
     * 更新用户列表
     * @param users
     */
    public void refreshUsers(User[] users) {
        comboBox.removeAllItems();
        comboBox.addItem(new User("","所有人"));//0位置永远是 所有人
        for (User user : users) {
            comboBox.addItem(user);
        }
    }

    /**
     * 获取下拉列表所有用户，包含 “所有人”item
     */
    public HashMap<String, User> getUsers() {
        int c = comboBox.getItemCount();
        HashMap<String, User> map = new HashMap<>();
        for (int i = 0; i < c; i++) {
            User u = comboBox.getItemAt(i);
            map.put(u.getUserName(), u);
        }
        return map;
    }

    /**
     * 消息发送事件处理
     * 将消息放入任务队列，同时 update ui
     */
    class MsgSendHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            User selectedItem = (User) comboBox.getSelectedItem();
            int selectedIndex = comboBox.getSelectedIndex();

            Msg m = new Msg(user.getUserName(), null, null, null);

            String tex = textArea.getText();
            String msg = (tex == null ? "" : tex);
            textArea.setText("");
            if (selectedIndex == 0) {
                m.setTargets("all");
            } else {
                m.setTargets("someone");
                m.setTo(selectedItem.getUserName());
            }
            m.setTex(msg);


            if (msg.equals("")){
                return;
            } else {
                taskQueue.addToLast(m);

                //更新UI
                if (selectedIndex == 0) {//public
                    addMsg(user.getRealName(), msg, MyMsgType.SEND_TO_ALL);
                } else {//给xxx的私信
                    addMsg(selectedItem.getRealName(), msg, MyMsgType.SEND_TO_SB);
                }
            }

        }
    }

}
