package com.example.hasee.virtualwinos.System_Control;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.hasee.virtualwinos.Activitys.Desktop;
import com.example.hasee.virtualwinos.Activitys.first_open;
import com.example.hasee.virtualwinos.Activitys.lock;
import com.example.hasee.virtualwinos.Activitys.startAnimation;
import com.example.hasee.virtualwinos.R;

public class system_state extends AppCompatActivity {
    /**
     * openState开机状态，0表示未开机，1表示已开机
     * logState 登陆状态，0表示未登陆，1表示已登陆
     * first_open_state,0表示第一次进入系统，1表示相反
     */
    int openState,logState;
    Intent mintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 下面两句必须放在setContentView的前面
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_system_state);
        init();
    }

    /**
     * 初始化
     */
    public void init(){

        SharedPreferences systemstates = getSharedPreferences("SystemState",MODE_PRIVATE);
        openState=systemstates.getInt("open",0);
        logState = systemstates.getInt("log",0);
            if(openState==1)  //已开机
            {
                if(logState==1) {  //已有用户登陆
                    mintent = new Intent(system_state.this, Desktop.class);
                    startActivity(mintent);
                }else {           //无用户登陆
                    mintent = new Intent(system_state.this, lock.class);
                    startActivity(mintent);
                }
            }else {   //未开机
                mintent = new Intent(system_state.this, startAnimation.class);
                startActivity(mintent);
            }


        finish();
    }
}
