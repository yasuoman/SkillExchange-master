package com.example.aoge.skillexchange;

/**
 * Created by dell on 2019/3/22.
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;
    private String time;
    private String rorn;

    public Msg(String content, int type, String time, String rorn) {
        this.content = content;
        this.type = type;
        this.time = time;
        this.rorn = rorn;
    }


    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public String getTheTime() {
        return time;
    }

    public String getRorn() {
        return rorn;
    }
}