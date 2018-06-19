package com.example.hasee.virtualwinos.System_Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.hasee.virtualwinos.R.drawable.min;

/**
 * Created by hasee on 2018/5/22.
 */

public class memoryManager {
    memoryChangedListener listener;

    /**
     * 记录内存使用的列表
     */
    List<memeory> memoryLists;


    //总内存大小
    int total_memory;


    /**
     * 可以使用的内存
     */
    int available_memory;




    /**
     * 构造函数
     * @param memoryLists
     */
    public memoryManager(List<memeory> memoryLists,int total_memory) {
        this.memoryLists = memoryLists;
        this.total_memory = total_memory;
    }

    public int getAvailable_memory() {
        return available_memory;
    }

    public void setAvailable_memory(int available_memory) {

        this.available_memory = available_memory;
    }

    /**
     * 分配内存的动作
     * @param mprocess 需要分配内存的进程
     * @param index
     */
    public void fenpei(process mprocess,int index){
        memeory temp_memory = new memeory();
        temp_memory.setPid(mprocess.getPid());
        temp_memory.setSpace(mprocess.getSpace());
        temp_memory.setStates(1);  //代表控件不空闲
        //分配之前的空闲内存块
        memeory old_memory = memoryLists.get(index);
        int old_memory_space = old_memory.getSpace();
        if(old_memory_space==mprocess.getSpace())
            memoryLists.remove(index);
        else old_memory.setSpace(old_memory_space-mprocess.getSpace());
        memoryLists.add(index,temp_memory);
        available_memory-=mprocess.getSpace();
        listener.memorychanged();
    }

    /**
     * 需要回收内存的进程
     * @param mprocess
     */
    public void huishou(process mprocess){
        int index = getindex(mprocess);
        int temp_old_space = mprocess.getSpace();
        if(index!=Integer.MAX_VALUE) {
            memeory temp_memory = memoryLists.get(index);
            int temp_space = temp_memory.getSpace();


            /**
             * 合并相邻空闲区
             */
            int for_index = index - 1;


            if (for_index >= 0 && memoryLists.get(for_index).getStates() == 0) {
                temp_memory.setSpace(temp_space + memoryLists.get(for_index).getSpace());
                memoryLists.remove(for_index);
            }
            //需要重新计算index，因为前面可能会改变memoryLists;
            index = getindex(mprocess);
            int beh_index = index + 1;
            int temp_memorylength = memoryLists.size();
            temp_space = temp_memory.getSpace();
            if (beh_index < temp_memorylength && memoryLists.get(beh_index).getStates() == 0) {
                temp_memory.setSpace(temp_space + memoryLists.get(beh_index).getSpace());
                memoryLists.remove(beh_index);
            }

            /**
             * 回收资源（把空间设为空闲）
             */
            temp_memory.setStates(0);
            temp_memory.setPid("0");
            available_memory+=temp_old_space;
            listener.memorychanged();


        }
    }

    /**
     * 得到进程分配的内存的索引
     * @param mprocess
     * @return
     */
    private int getindex(process mprocess){

        String temp_pid = mprocess.getPid();

        for(int i = 0; i<memoryLists.size();i++){
           if(memoryLists.get(i).getPid().equals(temp_pid))
               return i;
        }
        return Integer.MAX_VALUE;
    }

    /**
     * 首次适应算法
     * @return
     */
    public int FF(process mprocess){

        int temp_space = mprocess.getSpace();
        memeory temp_memory;
        for(int i = 0 ; i < memoryLists.size() ; i ++){
            temp_memory = memoryLists.get(i);
            if(temp_memory.states==0&&temp_memory.getSpace()>=temp_space)
                return i;
        }
        return Integer.MAX_VALUE;
    }

    /**
     * 循环首次适应算法
     * @param mprocess
     * @return
     */
    public int NF(process mprocess,int last_available_index){
        int temp_space = mprocess.getSpace();
        memeory temp_memory;
        int j = 0;
        for(int i = last_available_index ;  ; i ++){

            if(j>=memoryLists.size())
                return Integer.MAX_VALUE;
            temp_memory = memoryLists.get(i%memoryLists.size());
            if(temp_memory.states==0&&temp_memory.getSpace()>=temp_space)
                return i%memoryLists.size();
            j++;
        }
    }

    /**
     * 最佳适应算法（满足条件的最小的分区进行分配）
     * @param mprocess
     * @return
     */
    public int BF(process mprocess){

        memeory temp_memory;

        int temp_space = mprocess.getSpace();

        int temp_min = Integer.MAX_VALUE;

        int min_index = 0;

        ArrayList<Integer> fitindexList = new ArrayList<>();
        for(int i = 0;i<memoryLists.size();i++){
                temp_memory = memoryLists.get(i);
                if(temp_memory.getStates()==0&&temp_memory.getSpace()>=temp_space)
                    fitindexList.add(i);
        }
        if(fitindexList.size()==0) return Integer.MAX_VALUE;
        for(int i = 0;i<fitindexList.size();i++){
            temp_space = memoryLists.get(fitindexList.get(i)).getSpace();
            if(temp_space<temp_min)
            {
                temp_min =  temp_space;
                min_index = fitindexList.get(i);
            }
        }
        return min_index;
    }

    /**
     * 最坏适应算法（满足条件的最大的分区进行分配）
     * @param mprocess
     * @return
     */
    public int WF(process mprocess){
        memeory temp_memory;

        int temp_space = mprocess.getSpace();

        int temp_max = Integer.MIN_VALUE;

        int max_index = 0;

        ArrayList<Integer> fitindexList = new ArrayList<>();
        for(int i = 0;i<memoryLists.size();i++){
            temp_memory = memoryLists.get(i);
            if(temp_memory.getStates()==0&&temp_memory.getSpace()>=temp_space)
                fitindexList.add(i);
        }
        if(fitindexList.size()==0) return Integer.MAX_VALUE;
        for(int i = 0;i<fitindexList.size();i++){
            temp_space = memoryLists.get(fitindexList.get(i)).getSpace();
            if(temp_space>temp_max)
            {
                temp_max =  temp_space;
                max_index = fitindexList.get(i);
            }
        }
        return max_index;
    }

    /**
     * 监听内存队列变化
     */
    public interface memoryChangedListener{
        void memorychanged();
    }

    /**
     * 设置监听的函数
     * @param listener
     */
    public void setmemoryChangedListener(memoryChangedListener listener){
        this.listener = listener;
    }

}
