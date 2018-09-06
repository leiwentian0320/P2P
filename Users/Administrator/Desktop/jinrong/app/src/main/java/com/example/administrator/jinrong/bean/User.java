package com.example.administrator.jinrong.bean;

/**
 * Created by shkstart on 2016/11/16 0016.
 */
public class User {
    public int id;//编号
    public String name;//姓名
    public String password;//密码
    public String phone;//手机号
    public String imageUrl;//头像地址
    public boolean isCredit;//是否公安部认证

    public User() {
    }

    public User(int id, String name, String password, String phone, String imageUrl, boolean isCredit) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.isCredit = isCredit;
    }
}
