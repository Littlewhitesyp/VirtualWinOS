package com.example.hasee.virtualwinos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.System_Control.process;

import java.util.List;

/**
 * Created by hasee on 2018/5/21.
 */

public class zuse_processAdapter extends BaseAdapter {
    List<process> processList;
    Context mcontext;

    public zuse_processAdapter(List<process> processList, Context mcontext) {
        this.processList = processList;
        this.mcontext = mcontext;
    }

    @Override
    public int getCount() {
        return processList.size();
    }

    @Override
    public Object getItem(int position) {
        return processList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.process_adapter,null);
            holder = new ViewHolder();
            holder.pid = (TextView) convertView.findViewById(R.id.pid);
            holder.time = (TextView)convertView.findViewById(R.id.total_time);
            holder.space = (TextView)convertView.findViewById(R.id.space);
            holder.priority = (TextView)convertView.findViewById(R.id.priority);
            holder.myprocess = (TextView)convertView.findViewById(R.id.myprocess_textview);
            holder.wait_time = (TextView)convertView.findViewById(R.id.wait_textview);
            holder.Rp = (TextView)convertView.findViewById(R.id.Rp_textview);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        process temp_process = processList.get(position);
        holder.pid.setText(temp_process.getPid());

        holder.time.setVisibility(View.GONE);
        holder.space.setVisibility(View.GONE);   //不显示所占空间
        holder.priority.setVisibility(View.GONE);//不显示优先级
        holder.myprocess.setVisibility(View.GONE); //不显示进度
        holder.wait_time.setVisibility(View.GONE); //不显示等待时间
        holder.Rp.setVisibility(View.GONE);  //不显示响应比
        return convertView;
    }

    /**
     * 方便复用的类
     */
    class ViewHolder{

        TextView pid,    //进程pid
                time,   //进程总时间
                space,  //进程空间
                priority, //进程的优先级
                myprocess, //进程已完成的时间，单位s
                wait_time, //进程等待时间
                Rp;        //响应比
    }
}
