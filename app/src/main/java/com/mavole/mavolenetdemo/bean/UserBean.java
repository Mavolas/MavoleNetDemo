package com.mavole.mavolenetdemo.bean;

public class UserBean {

    public String userId;
    public String photoUrl;
    public String name;
    public String tick;
    public String mobile;
    public String platform;

    @Override
    public String toString() {
        return "UserBean{" +
                "userId='" + userId + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", name='" + name + '\'' +
                ", tick='" + tick + '\'' +
                ", mobile='" + mobile + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
