package com.example.hasee.virtualwinos.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hasee.virtualwinos.CircleImage.CircleImageView;
import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.database.User;

import java.util.List;

/**
 * Created by hasee on 2018/5/9.
 */

public class userListAdapter extends BaseAdapter {
    List<Object> muserLists;
    LayoutInflater minflater;

    public  userListAdapter(List<Object> userList,LayoutInflater inflater){
        muserLists = userList;
        minflater = inflater;
    }
    @Override
    public int getCount() {
        return muserLists.size();
    }

    @Override
    public Object getItem(int position) {
        return muserLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
        convertView = minflater.inflate(R.layout.userlistadapter,null);
        holder = new ViewHolder();
        holder.account = (TextView)convertView.findViewById(R.id.account);
            holder.avatar = (CircleImageView)convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);

        }else
            holder = (ViewHolder) convertView.getTag();
        User user = (User) muserLists.get(position);
        holder.avatar.setImageResource(user.getAvatar());
        holder.account.setText(user.getAccount());
        return convertView;
    }
    class ViewHolder{
        CircleImageView avatar;
        TextView account;
    }
}
