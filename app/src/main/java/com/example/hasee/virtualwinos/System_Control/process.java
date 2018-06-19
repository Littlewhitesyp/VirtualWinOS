package com.example.hasee.virtualwinos.System_Control;



/**
 * Created by hasee on 2018/5/20.
 */

public class process {
    final static int PID_LENGTH = 4; //默认的pid 的长度为4

    private String pid;      //进程标识
    private int myprocess,   //进程完成的进度0-total_time
            total_time,      //进程完成总时间,单位s（要求服务时间）
            space;           //进程所需内存空间;单位m

    private int priority;    //进程的优先级

    private int wait_time;   //进程等待时间

    public int getWait_time() {
        return wait_time;
    }

    public void setWait_time(int wait_time) {
        this.wait_time = wait_time;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setMyprocess( int myprocess) {
        this.myprocess = myprocess;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public String getPid() {
        return pid;
    }

    public int getMyprocess() {
        return myprocess;
    }

    public int getTotal_time() {
        return total_time;
    }

    public int getSpace() {
        return space;
    }

    /**
     * 得到一个随机的进程
     * @return
     */
    public static process getRandomProcess(){
        process mprocess = new process();
        mprocess.setPid(getRandom(PID_LENGTH));
        mprocess.setMyprocess(0);
        mprocess.setSpace(Integer.parseInt(getRandom(5,10)));  //占用空间5-10m
        mprocess.setTotal_time(Integer.parseInt(getRandom(3,10)));
        mprocess.setPriority(Integer.parseInt(getRandom()));  //0-9数值越小，优先级越高
        mprocess.setWait_time(0);
        return mprocess;
    }

    /**
     * 根据长度，起始范围得到随机数
     * @param needLength
     */
    static String getRandom(int needLength){
        StringBuffer result = new StringBuffer();
        for(int i =0;i<needLength;i++){
            result.append(getRandom());
        }
        if(result!=null)
            return result.toString();
        else return null;

    }

    /**
     * 得到某一个范围内的随机数
     * @return
     */
    static String getRandom(int start, int end){
        return String.valueOf((int)(Math.random()*(end-start)+start));
    }

    /**
     * 得到0-9之间的任意一个数
     * @return
     */
    static String getRandom(){
        return String.valueOf((int)(Math.random()*9));

    }

}
