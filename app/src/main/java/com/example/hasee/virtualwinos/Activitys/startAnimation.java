package com.example.hasee.virtualwinos.Activitys;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.System_Control.FileSystem;
import com.example.hasee.virtualwinos.System_Control.system_state;
import com.example.hasee.virtualwinos.audioPlay.MyMediaPlayer;
import com.example.hasee.virtualwinos.controlScreen.Morieation;
import com.example.hasee.virtualwinos.controlScreen.mySensorEventListener;

import java.io.File;
import java.io.FileNotFoundException;

public class startAnimation extends AppCompatActivity {
    /**
     * 四个ImageView
     */
    ImageView mred,mgreen,mblue,myellow;
    /**
     * 开机欢迎文字
     */
    TextView welcomeText;
    /**
     * 屏幕的宽和高
     */
    float m_screen_width,m_screen_height;

    Handler mhandler;

    mySensorEventListener msel;
    SensorManager sensorManager;

    DisplayMetrics mdisplayMetrics;

     final int block_width = 70;

    Thread tlogin; //转入登陆界面的线程

    /**
     *播放音频
     */
    MediaPlayer mediaPlayer;

    RelativeLayout start_anim_background;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        setContentView(R.layout.activity_start_animation);
        /**
         * 得到手机的宽和高（像素）
         */
        mdisplayMetrics = getResources().getDisplayMetrics();
        m_screen_height = mdisplayMetrics.heightPixels;
        m_screen_width = mdisplayMetrics.widthPixels;
        init();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
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

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                        }
                        /**
                         * 手机右侧朝下
                         */
                        if(x<-3){

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        msel = new mySensorEventListener(mhandler);

        Log.e("宽和高：","屏幕宽\\高："+m_screen_width+"\\"+m_screen_height);
        /**
         * 播放动画
         */
        ObjectAnimator alphaanim1 = ObjectAnimator.ofFloat(welcomeText,"alpha",0.0f,0.0f);
        alphaanim1.setDuration(5);
        alphaanim1.start();
        startAnimation_RED(mred,RED_path(),welcomeText);
        startAnimation(mgreen,GREEN_path());
        startAnimation(mblue,BLUE_path());
        startAnimation(myellow,YELLOW_path());
        mediaPlayer = MyMediaPlayer.getMediaplay(this,R.raw.windows_logon);
    }

    /**
     * 初始化四个view
     */
    public void init(){

        start_anim_background = (RelativeLayout)findViewById(R.id.start_anim_background);

        mred = (ImageView)findViewById(R.id.mred);
        mgreen = (ImageView)findViewById(R.id.mgreen);
        mblue = (ImageView)findViewById(R.id.mblue);
        myellow = (ImageView)findViewById(R.id.myellow);
        /**
         * 动态设置textView
         */
        welcomeText = (TextView)findViewById(R.id.welcomeText);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) welcomeText.getLayoutParams();
        params.bottomMargin=(int)(m_screen_height/5);
        welcomeText.setLayoutParams(params);
    }

    /**
     *
     * 红色的动画
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void startAnimation_RED(View v,Path path,View v1){

        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(v1, "alpha", 0.0f, 1.0f,0.0f,1.0f);
        ObjectAnimator alphaanim1 = ObjectAnimator.ofFloat(v,"scaleX",0.0f,1.0f);
        ObjectAnimator alphaanim2 = ObjectAnimator.ofFloat(v,"scaleY",0.0f,1.0f);
        alphaanim.setRepeatCount(-1);
        alphaanim.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator TranslateXanim = ObjectAnimator.ofFloat(v,v.X,v.Y,path);
        //ObjectAnimator TranslateYanim = ObjectAnimator.ofFloat(v,"translationY",0,m_screen_height/2-35);
        AnimatorSet set = new AnimatorSet();
        AnimatorSet set1 = new AnimatorSet();
        //alphaanim.setRepeatCount(-1);
        //TranslateXanim.setRepeatCount(-1);
        //TranslateYanim.setRepeatCount(-1);
        //alphaanim.setRepeatMode(ValueAnimator.REVERSE);
        //TranslateXanim.setRepeatMode(ValueAnimator.REVERSE);
        //TranslateYanim.setRepeatMode(ValueAnimator.REVERSE);

        set.setDuration(3000);

        /**
         * 偶然发现可以这样写，太方便了，功能很强大
         */
        set.play(TranslateXanim).with(alphaanim1).with(alphaanim2).before(alphaanim);
        set.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void startAnimation(View v,Path path){
        ObjectAnimator alphaanim1 = ObjectAnimator.ofFloat(v,"scaleX",0.0f,1.0f);
        ObjectAnimator alphaanim2 = ObjectAnimator.ofFloat(v,"scaleY",0.0f,1.0f);
        ObjectAnimator TranslateXanim = ObjectAnimator.ofFloat(v,v.X,v.Y,path);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(3000);
        set.playTogether(TranslateXanim,alphaanim1,alphaanim2);
        set.start();
    }

    /**
     * 注册传感器监听
     */
    @Override
    protected void onResume() {
        Sensor msensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(msel,msensor,SensorManager.SENSOR_DELAY_UI);

        tlogin=new Thread(new mylogin());
        tlogin.start();

        /**
         * 播放音频
         */
        mediaPlayer.start();

        /**
         * 加载硬盘
         */
        FileSystem fileSystem = new FileSystem(this);
        fileSystem.createDir("C"+File.separator);
        super.onResume();
    }

    /**
     * 取消监听
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(msel);
        //中断一个线程
        tlogin.interrupt();
        tlogin = null;

        /**
         * 停止播放音频（开机动画）
         */
        mediaPlayer.stop();

        super.onPause();

    }

    /**
     * 红色路径
     * @return
     */
    public Path RED_path(){
        Path path = new Path();
        path.moveTo(m_screen_width-70,0);
        path.quadTo(m_screen_width,m_screen_height/2,m_screen_width/2+block_width/2,m_screen_height/2-block_width/2);
        return path;
    }

    /**
     * 绿色的路径
     * @return
     */
    public Path GREEN_path(){
        Path path = new Path();
        path.moveTo(0,0);
        path.quadTo(m_screen_width/2,0,m_screen_width/2-block_width/2,m_screen_height/2-block_width/2);
        return path;
    }

    /**
     * 蓝色路径
     * @return
     */
    public Path BLUE_path(){
        Path path = new Path();
        path.moveTo(0,m_screen_height-block_width);
        path.quadTo(0,m_screen_height/2,m_screen_width/2-block_width/2,m_screen_height/2+block_width/2);
        return path;
    }

    /**
     * 黄色路径
     * @return
     */
    public Path YELLOW_path(){
        Path path = new Path();
        path.moveTo(m_screen_width-block_width,m_screen_height-block_width);
        path.quadTo(m_screen_width/2,m_screen_height,m_screen_width/2+block_width/2,m_screen_height/2+block_width/2);
        return path;
    }

    /**
     * 进入锁屏界面的线程
     */
    class mylogin implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(9000); //暂停9秒，进入锁屏界面
                SharedPreferences read = getSharedPreferences("SystemState",MODE_PRIVATE);
                int first_open_state = read.getInt("first_open",0);
                if(first_open_state==0)        //第一次进入系统
                {
                    Intent mintent = new Intent(startAnimation.this,first_open.class);
                    startActivity(mintent);
                }else {
                    Intent mintent = new Intent(startAnimation.this, lock.class);
                    startActivity(mintent);
                }
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
