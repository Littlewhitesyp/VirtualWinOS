package com.example.hasee.virtualwinos.System_Control;

/**
 * Created by hasee on 2018/5/22.
 */

public class memeory {
    String pid;  //所属进程的pid
    int space;   //占用空间
    int states;  //是否可用(0可以使用1不可使用)

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public void setStates(int states) {
        this.states = states;
    }

    public String getPid() {
        return pid;
    }

    public int getSpace() {
        return space;
    }

    public int getStates() {
        return states;
    }
}
