package com.example.aoge.skillexchange;

import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2019/3/21.
 */

public class UserInformation implements Serializable {
    public static int permission = 0;
    public static String userinformation = null;
    public static String uName = null;
    public static String gedr = null;
    public static String loan = null;
    public static String ucan = null;
    public static String uwant = null;
    public static String head = null;

    public static int firstShow = 0;

    public static SimpleAdapter adapter;

    public static String ph = null;
    public static String context = null;

    public static String HOST = "169.254.26.233";//服务器地址
    public static int PORT = 8800;//连接端口号
    public static Socket socket = null;
    public static BufferedReader in = null;
    public static PrintWriter out = null;
    public static List<Map<String, Object>> historyList = new ArrayList<Map<String, Object>>();

    private String userName;

    private String email;

    private String password;

    private String gender = "male";

    private String location ="null";

    private String can="null";

    private String want="null";

    private String headPicture;

    public UserInformation(){
        super();
    }

    public UserInformation(String username,String email,String gender,
                           String location, String can,String want,
                           String headpicture){
        super();
        this.userName = username;
        this.email = email;
        this.gender = gender;
        this.location = location;
        this.can = can;
        this.want = want;
        this.headPicture = headpicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }

    public String getWant() {
        return want;
    }

    public void setWant(String want) {
        this.want = want;
    }

    public String getheadPicture() {
        return headPicture;
    }

    public void setheadPicture(String headPicture) {
        this.headPicture = headPicture;
    }
}
