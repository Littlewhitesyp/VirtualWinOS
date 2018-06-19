package com.example.hasee.virtualwinos.Activitys;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.hasee.virtualwinos.Fragments.createFirstUser;
import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.controlScreen.Morieation;
import com.example.hasee.virtualwinos.controlScreen.mySensorEventListener;

public class first_open extends AppCompatActivity {
    Handler mhandler;
    /**
     * 传感器
     */
    SensorManager sensorManager;
    mySensorEventListener msel;

    TextView first_open_welcome ;

    FragmentManager fm;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 下面两句必须放在setContentView的前面
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_first_open);
        /**
         * 句柄
         */
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){

                    case 666:
                        Morieation m = (Morieation) msg.obj;
                        float x = m.getX();
                        /**
                         * 手机左侧朝下
                         */
                        if(x>3)
                        {
                            //if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE)
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                        }
                        /**
                         * 手机右侧朝下
                         */
                        if(x<-3){
                            //if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        }
                        break;
                    case 667:
                        first_open_welcome.setVisibility(View.GONE);
                        Log.e("shabi","Wanghaodong");
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        msel = new mySensorEventListener(mhandler);
        init();

    }

    @Override
    protected void onResume() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(msel,sensor,SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(msel);
        super.onPause();
    }

    /**
     * 初始化
     */
    public void init(){
        first_open_welcome = (TextView)findViewById(R.id.first_open_welcome_text);
        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(first_open_welcome,"alpha",1.0f,0.0f,1.0f,0.0f);
        alphaanim.setDuration(3000);
        alphaanim.setRepeatCount(-1);
        alphaanim.setRepeatMode(ValueAnimator.REVERSE);

        alphaanim.start();


        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        mhandler.postDelayed(new createFirstUserRunnable(),3000);

    }

    class createFirstUserRunnable implements Runnable{
        @Override
        public void run() {
            ft.replace(R.id.first_background,new createFirstUser());
            ft.commit();
            mhandler.sendEmptyMessage(667);
        }
    }

}
