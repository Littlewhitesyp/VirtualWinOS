package com.example.hasee.virtualwinos.Activitys;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;

import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.audioPlay.MyMediaPlayer;
import com.example.hasee.virtualwinos.controlScreen.Morieation;
import com.example.hasee.virtualwinos.controlScreen.mySensorEventListener;

import static java.security.AccessController.getContext;

public class lock extends AppCompatActivity {
    Handler mhandler;

    /**
     * 日期和时间
     */
    TextClock mdate,mtime;

    RelativeLayout mlock_background;
    LinearLayout shutdown_anim;

    SensorManager sensorManager;
    mySensorEventListener msel;

    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 下面两句必须放在setContentView的前面
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_lock);

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
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        init();

    }

    /**
     * 电源键的点击事件
     * @param v
     */
    public void power_button(View v){
        PopupMenu powerMenu = new PopupMenu(this,v);
        powerMenu.getMenuInflater().inflate(R.menu.power_menu_lock,powerMenu.getMenu());
        powerMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.restart)
                    //重启
                    restart();
                else if(item.getItemId()==R.id.shutdown)
                /**
                 * 关机
                 */
                    shutdown();
                mhandler.postDelayed(new finish(),3100);
                return true;
            }
        });
        powerMenu.show();
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

        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(mlock_background,"alpha",1.0f,0.0f);
        alphaanim.setDuration(3000);
        alphaanim.start();
        shutdown_anim.setVisibility(View.VISIBLE);
        MediaPlayer mediaPlayer = MyMediaPlayer.getMediaplay(this,R.raw.windows_shutdown);
        mediaPlayer.start();

    }


    class finish implements Runnable{
        @Override
        public void run() {
            finish();
        }
    }

    class restart implements Runnable{
        @Override
        public void run() {
            Intent mintent = new Intent(lock.this,startAnimation.class);
            startActivity(mintent);
           finish();
        }
    }

    @Override
    protected void onResume() {
        /**
         * 注册传感器监听
         */
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(msel,sensor,SensorManager.SENSOR_DELAY_UI);
        super.onResume();

        /**
         * 记录os系统状态（这里是开机状态：已开机,进入到锁屏界面所以代表已开机）
         */
        SharedPreferences.Editor editor = getSharedPreferences("SystemState",MODE_PRIVATE).edit();
        editor.putInt("open",1);   //1代表已开机，0代表未开机
        editor.commit();
    }

    @Override
    protected void onPause() {
        /**
         * 取消传感器监听
         */
        sensorManager.unregisterListener(msel);
        super.onPause();
    }

    public void init(){
        editor = getSharedPreferences("SystemState",MODE_PRIVATE).edit();
        SharedPreferences read = getSharedPreferences("SystemState",MODE_PRIVATE);
        int lockpaperId = read.getInt("lockpaper",0);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        msel = new mySensorEventListener(mhandler);

        shutdown_anim = (LinearLayout)findViewById(R.id.shutdown_anim);
        mlock_background = (RelativeLayout)findViewById(R.id.mlock_background);

        if(lockpaperId!=0)
            mlock_background.setBackgroundResource(lockpaperId);


    }

    public void mblock_back_click(View v){
        Intent mintent = new Intent(this,login.class);
        startActivity(mintent);
        finish();
    }
}
