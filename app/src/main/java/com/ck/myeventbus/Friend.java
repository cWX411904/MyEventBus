package com.ck.myeventbus;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class Friend {
    private String name;
    private String password;

    public Friend(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
