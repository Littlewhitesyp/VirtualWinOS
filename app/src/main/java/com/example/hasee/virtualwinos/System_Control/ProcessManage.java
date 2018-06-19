package com.example.hasee.virtualwinos.System_Control;

import java.util.List;

/**
 * Created by hasee on 2018/5/20.
 */

public class ProcessManage {


    jiuxvListListener mjiuxvListener;
    private List<process> jiuxvList;
    private List<process> zuseList;

    /**
     * 构造函数
     * @param jiuxvList
     * @param zuseList
     */
    public ProcessManage(List<process> jiuxvList, List<process> zuseList) {
        this.jiuxvList = jiuxvList;
        this.zuseList = zuseList;
    }

    /**
     * 向就绪队列添加一个进程
     * @param mprocess
     */
    public void addprocessJiuxv(process mprocess){
        jiuxvList.add(mprocess);
        mjiuxvListener.jiuxvChanged();
    }

    /**
     * 就绪队列减少一个process
     * @param mprocess
     */
    public void removeprocessJiuxv(process mprocess){
        for(int i = 0;i<jiuxvList.size();i++)
            if(mprocess.getPid().equals(jiuxvList.get(i).getPid()))
            {
                jiuxvList.remove(i);
                break;
            }
        mjiuxvListener.jiuxvChanged();
    }

    /**
     * 添加到阻塞队列(阻塞)
     * @param mprocess
     */
    public void addprocessZuse(process mprocess){
        zuseList.add(mprocess);
        removeprocessJiuxv(mprocess);
    }

    /**
     * 唤醒被阻塞的进程
     * @return
     */
    public process awake(int index){
        process temp_process = zuseList.get(index);  //先被阻塞的先被唤醒
        zuseList.remove(index);
        addprocessJiuxv(temp_process);
        return temp_process;
    }

    /**
     * 记录被中断进程的中断现场（保护中断现场）
     * @param newProcess
     */
    public void modifyJiuxv(process newProcess){
        String temp_pid = newProcess.getPid();
        int temp_myprogress = newProcess.getMyprocess();
        for(int i = 0;i<jiuxvList.size();i++)
        {
            process temp_process = jiuxvList.get(i);
            if(temp_process.getPid().equals(temp_pid)){
                temp_process.setMyprocess(temp_myprogress);
                mjiuxvListener.jiuxvChanged();
            }
        }

    }

    /**
     * 将进程移至队列的尾部
     * @param Process
     */
    public void to_tail(process Process){
        String temp_pid = Process.getPid();
        for(int i = 0;i<jiuxvList.size();i++)
        {
            process temp_process = jiuxvList.get(i);
            if(temp_process.getPid().equals(temp_pid)){
                jiuxvList.remove(i);
                jiuxvList.add(temp_process);
                mjiuxvListener.jiuxvChanged();
                break;
            }
        }

    }

    /**
     * 先来先服务
     * @return 应该分配cpu的进程
     */
    public process FCFS(){
        return jiuxvList.get(0);
    }

    /**
     * 短作业优先
     * @return
     */
    public process SJF(){
        int temp_min = Integer.MAX_VALUE;
        int min_loc = 0;    //最短时间的进程的索引
        process temp_process;
        int temp_time;
        for(int i = 0;i<jiuxvList.size();i++){
             temp_process = jiuxvList.get(i);
             temp_time = temp_process.getTotal_time();
             if(temp_time<temp_min) {
                 temp_min = temp_time;
                 min_loc = i;
             }
        }
        return jiuxvList.get(min_loc);
    }

    /**
     * （静态）优先权调度算法
     * @return
     */
    public process priority(){
        int temp_min = Integer.MAX_VALUE;
        int min_loc = 0;    //优先级最高的进程的索引，priority数值越小优先级越高
        process temp_process;
        int temp_priority;
        for(int i = 0;i<jiuxvList.size();i++){
            temp_process = jiuxvList.get(i);
            temp_priority = temp_process.getPriority();
            if(temp_priority<temp_min) {
                temp_min = temp_priority;
                min_loc = i;
            }
        }
        return jiuxvList.get(min_loc);
    }

    /**
     * 高响应比优先算法
     * 响应比Rp = 1+wait_time/total_time;
     * @return
     */
    public process REMethod(){
        double temp_max = Integer.MIN_VALUE;
        int temp_wait_time;
        int temp_total_time;
        double temp_bizhi;
        process temp_process;
        int max_loc = 0;   //最高响应比的索引
        for(int i = 0;i<jiuxvList.size();i++){
            temp_process = jiuxvList.get(i);
            temp_wait_time = temp_process.getWait_time();
            temp_total_time = temp_process.getTotal_time();
            temp_bizhi = (double) temp_wait_time/temp_total_time;
            if(temp_max<temp_bizhi)
            {
                temp_max = temp_bizhi;
                max_loc = i;
            }
        }
        return jiuxvList.get(max_loc);
    }

    /**、
     * 监听就绪队列发生变化的接口
     */
    public interface jiuxvListListener{
        void jiuxvChanged();
    }

    /**
     * 设置监听的接口
     * @param jiuxvListListener
     */
    public void setMjiuxvListener(jiuxvListListener jiuxvListListener){
        mjiuxvListener = jiuxvListListener;
    }

}
