package com.engure.po;

/**
 * 一条信息，a给b发送消息tex or a群发消息tex
 */
public class Msg {

    String from;//发送者
    String to;//私发或者群发
    String targets;//私发/群发, someone/all
    String tex;//发送的文本

    @Override
    public String toString() {
        return "Msg{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", targets='" + targets + '\'' +
                ", tex='" + tex + '\'' +
                '}';
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public String getTex() {
        return tex;
    }

    public void setTex(String tex) {
        this.tex = tex;
    }

    public Msg() {
    }

    public Msg(String from, String to, String targets, String tex) {
        this.from = from;
        this.to = to;
        this.targets = targets;
        this.tex = tex;
    }
}
