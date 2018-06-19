package com.example.hasee.virtualwinos.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.System_Control.FileSystem;

import java.io.File;
import java.util.List;

/**
 * Created by hasee on 2018/5/13.
 */

public class fileadapter extends BaseAdapter {
    List<File> mfiles;
    LayoutInflater minflater;

    public fileadapter(List<File> files,LayoutInflater inflater){
        mfiles = files;
        minflater = inflater;
    }
    @Override
    public int getCount() {
        return mfiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mfiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = minflater.inflate(R.layout.file_dir,null);
            holder = new ViewHolder();
            holder.file_icon = (ImageView)convertView.findViewById(R.id.file_icon);
            holder.file_name = (TextView) convertView.findViewById(R.id.file_name);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();
            String filename = mfiles.get(position).getName();
            holder.file_name.setText(filename);
            holder.file_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            File file = mfiles.get(position);
                String file_type = FileSystem.getfiletype(file);
                Log.e("file_type",file_type);
                switch (file_type){
                    case "txt":
                        holder.file_icon.setImageResource(R.drawable.txt_icon);
                        break;
                    case "dir":
                        holder.file_icon.setImageResource(R.drawable.dir_icon);
                        break;
                    default:
                      break;
                }



        return convertView;
    }

    class ViewHolder{
        ImageView file_icon;
        TextView file_name;
    }

}
