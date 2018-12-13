package com.mavole.mavolenetdemo.bean;

import java.util.Date;

public class UserBean {

    public String key;

    public String phone;

    public String name;

    public String passwd;

    public String text;

    public String img;

    public String other;

    public String other2;

    public Date createTime;

    @Override
    public String toString() {
        return "UserBean{" +
                "key='" + key + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", text='" + text + '\'' +
                ", img='" + img + '\'' +
                ", other='" + other + '\'' +
                ", other2='" + other2 + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
