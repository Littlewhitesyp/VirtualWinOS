package com.example.hasee.virtualwinos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hasee.virtualwinos.Activitys.Desktop;
import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.System_Control.process;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by hasee on 2018/5/20.
 */

public class processAdapter extends BaseAdapter {
    List<process> processList;  //数据源
    Context mcontext;           //上下文

    public processAdapter(List<process> processList, Context mcontext) {
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

            /**
            * 填充数据
            */
            process temp_process = processList.get(position);
            holder.pid.setText(temp_process.getPid());
            holder.time.setText(temp_process.getTotal_time()+"");
            holder.space.setText(temp_process.getSpace()+"");
            holder.priority.setText(temp_process.getPriority()+"");
            holder.myprocess.setText(temp_process.getMyprocess()+"");
            holder.wait_time.setText(temp_process.getWait_time()+"");
            double rp = 1+(double)temp_process.getWait_time()/temp_process.getTotal_time();

            /**
            * 保留小数点后两位
            */

            DecimalFormat df = new DecimalFormat("#.00");
            holder.Rp.setText(df.format(rp));
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
