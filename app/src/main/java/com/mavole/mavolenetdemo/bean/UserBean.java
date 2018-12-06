package com.mavole.mavolenetdemo.bean;

import java.util.Date;

public class UserBean {

    public String UserName;

    public String PassWord;

    public String Flag;

    public Date BirtyDay;

    public int Age;

    @Override
    public String toString() {
        return "UserBean{" +
                "UserName='" + UserName + '\'' +
                ", PassWord='" + PassWord + '\'' +
                ", Flag='" + Flag + '\'' +
                ", BirtyDay=" + BirtyDay +
                ", Age=" + Age +
                '}';
    }
}
