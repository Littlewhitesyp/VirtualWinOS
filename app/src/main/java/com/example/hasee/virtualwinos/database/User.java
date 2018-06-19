package com.example.hasee.virtualwinos.database;

/**
 * Created by hasee on 2018/5/8.
 */

public class User {
    String account;
    String nickname;
    int avatar;  //头像的id
    String password;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getPassword() {
        return password;
    }
}
