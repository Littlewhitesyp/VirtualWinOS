package com.example.hasee.virtualwinos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hasee.virtualwinos.R;

import java.util.List;

/**
 * Created by hasee on 2018/5/16.
 */

public class waterfalladapter extends BaseAdapter {

    //头像资源Id
   int [] mlist;
    //上下文
    Context mcontext;
    LayoutInflater mlayoutinflater;

    public waterfalladapter(int [] list,Context context) {
        mlist = list;
        mcontext = context;
        mlayoutinflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return mlist.length;
    }

    @Override
    public Object getItem(int position) {
        return mlist[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null)
        {
            convertView = mlayoutinflater.inflate(R.layout.waterfalllayout,null);
            holder = new ViewHolder();
            holder.image = (ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        holder.image.setImageResource(mlist[position]);

        return convertView;
    }
    class ViewHolder{
        ImageView image;
    }
}
