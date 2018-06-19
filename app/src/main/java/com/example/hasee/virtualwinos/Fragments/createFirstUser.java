package com.example.hasee.virtualwinos.Fragments;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.hasee.virtualwinos.Activitys.startAnimation;
import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.audioPlay.MyMediaPlayer;
import com.example.hasee.virtualwinos.database.User;
import com.example.hasee.virtualwinos.database.openHelper;
import com.example.hasee.virtualwinos.database.usersDao;




public class createFirstUser extends Fragment {
    EditText maccount_edit,password_edit;
    TextView finish_button,errorHint;
    openHelper mopenHelper;
    String maccount,mpassword;
    LinearLayout linearLayout;

    RelativeLayout create_first_background;

    SharedPreferences.Editor editor;
    Handler mhandler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_first_user,null);
        init(view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }


    private void init(View v) {

        create_first_background = (RelativeLayout)v.findViewById(R.id.create_first_background);
        editor = getActivity().getSharedPreferences("SystemState", Context.MODE_PRIVATE).edit();
        linearLayout = (LinearLayout)v.findViewById(R.id.input_user_info);


       maccount_edit = (EditText)v.findViewById(R.id.name);
       password_edit = (EditText)v.findViewById(R.id.password);
        finish_button = (TextView)v.findViewById(R.id.finish);
        errorHint = (TextView)v.findViewById(R.id.errorhint);

        mopenHelper = new openHelper(getContext(),"users",null,1);
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                maccount = maccount_edit.getText().toString();
                mpassword = password_edit.getText().toString();
                if(maccount.equals("")||mpassword.equals(""))
                    show_errorHint("账户名和登陆密码不能为空");
                else{
                    user.setAccount(maccount);
                    user.setPassword(mpassword);
                    user.setAvatar(R.drawable.defaultavatar);
                    user.setNickname("Admin");
                    usersDao userdao = new usersDao(mopenHelper);
                    if(userdao.insert(user)){   //增加信息成功
                        linearLayout.setVisibility(View.GONE);
                        v.setVisibility(View.GONE);
                        editor.putInt("first_open",1);  //已经不是第一次进入系统。
                        editor.commit();
                        restart();
                    }
                    else show_errorHint("该用户已存在");      //增加信息失败
                }

            }
        });

    }

    /**
     * 错误信息的显示逻辑
     */
    void show_errorHint(String errinfo){
        errorHint.setVisibility(View.VISIBLE);
        errorHint.setText(errinfo);
        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(errorHint,"alpha",1.0f,0.0f);
        alphaanim.setDuration(3000);
        alphaanim.start();
    }
    public void restart(){
        shutdown();
        mhandler.postDelayed(new restart(),3100);
    }

    public void shutdown(){
        // TODO: 2018/5/8 释放资源
        editor.putInt("open",0);
        editor.putInt("log",0);
        editor.commit();
        show_errorHint("配置完成，正在重启");
        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(create_first_background,"alpha",1.0f,0.0f);
        alphaanim.setDuration(3000);
        alphaanim.start();
        MediaPlayer mediaPlayer = MyMediaPlayer.getMediaplay(getContext(),R.raw.windows_shutdown);
        mediaPlayer.start();

    }

    class restart implements Runnable{
        @Override
        public void run() {
            Intent mintent = new Intent(getActivity(),startAnimation.class);
            startActivity(mintent);
            getActivity().finish();
        }
    }
}
