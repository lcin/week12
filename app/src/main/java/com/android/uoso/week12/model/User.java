package com.android.uoso.week12.model;

import java.io.Serializable;

/**
 * 用户类
 */
public class User implements Serializable{
    private String username;
    private String password;
    private String realName;
    private int age;
    private String sex;

    public User() {
    }

    public User(String username, String password, String realName, int age, String sex) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.age = age;
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
