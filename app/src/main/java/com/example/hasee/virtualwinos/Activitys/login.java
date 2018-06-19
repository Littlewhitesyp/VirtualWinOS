package com.example.hasee.virtualwinos.Activitys;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hasee.virtualwinos.Adapter.userListAdapter;
import com.example.hasee.virtualwinos.CircleImage.CircleImageView;
import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.controlScreen.Morieation;
import com.example.hasee.virtualwinos.controlScreen.mySensorEventListener;
import com.example.hasee.virtualwinos.database.User;
import com.example.hasee.virtualwinos.database.openHelper;
import com.example.hasee.virtualwinos.database.usersDao;

import java.util.List;

public class login extends AppCompatActivity {
    //即将登陆的用户的头像
    CircleImageView loging_avatar;

    /**
     * 用户列表显示
     */
    ListView userLists;
    RelativeLayout login_background;
    TextView user_name_view;
    EditText password;
    String Loging_user_name,  //即将登陆的用户名和密码
           password_str;

    TextInputLayout password_layout;

    /**
     * 传感器Manager
     */
    SensorManager mSensorManager;
    /**
     * 实现SensorEventListener接口的类
     */
    mySensorEventListener msel;

    Handler mhandler;
    SharedPreferences.Editor editor;

    usersDao musersDao;
    openHelper mopenHelper;

    int mscreen_width,mscreen_height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        setContentView(R.layout.activity_login);
        init();
        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);

        /**
         * 根据重力改变屏幕方向
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
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        /**
         * 自定义的SensorEventListener,在mhandler实例化之后实例化
         */
        msel = new mySensorEventListener(mhandler);

    }

    /**
     * 注册加速度传感器舰艇
     */
    @Override
    protected void onResume() {
        Sensor msenor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(msel,msenor,SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    /**
     * 取消加速度传感器监听
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(msel);
        super.onPause();
    }

    /**
     * 初始化
     */
    public void init(){
        /**
         * 得到手机屏幕的宽和高（像素）
         */
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mscreen_width = displayMetrics.widthPixels;
        mscreen_height = displayMetrics.heightPixels;

        login_background = (RelativeLayout)findViewById(R.id.login_background);

        /**
         * 即将登陆的用户的头像
         */
        loging_avatar = (CircleImageView)findViewById(R.id.loging_avatar);
        password = (EditText)findViewById(R.id.mpassword);
        user_name_view = (TextView)findViewById(R.id.user_name);
        userLists = (ListView)findViewById(R.id.userlists);


        password_layout = (TextInputLayout)findViewById(R.id.password_layout);

         editor = getSharedPreferences("SystemState",MODE_PRIVATE).edit();
        SharedPreferences read = getSharedPreferences("SystemState",MODE_PRIVATE);
        int loginid = read.getInt("lockpaper",0);
        if(loginid!=0)
            login_background.setBackgroundResource(loginid);

        /**
         * 实例化openHelper
         */
        mopenHelper = new openHelper(this,"users",null,1);
        musersDao = new usersDao(mopenHelper);

         final List<Object> userlists = musersDao.query();
         User user = (User) userlists.get(0);
         loging_avatar.setImageResource(user.getAvatar());
         user_name_view.setText(user.getAccount());
        // TODO: 2018/5/9 测试
        for(int i = 0;i<userlists.size();i++){
            User user1 = (User) userlists.get(i);
               Log.e("a/p/a/n",user1.getAccount()+"/"+user1.getPassword()+"/"+user1.getAvatar()+"/"+user1.getNickname());

        }
        /**
         * 在登陆界面显示多个用户，供选择去登陆
         */
        userListAdapter madapter = new userListAdapter(userlists,getLayoutInflater());
        userLists.setAdapter(madapter);
        userLists.getLayoutParams().width = mscreen_width/4;
        /**
         * 点击用户列表中的用户
         */
        userLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) userlists.get(position);
                /**
                 * 点击用户后播放的动画
                 */
                ObjectAnimator scaleXanim = ObjectAnimator.ofFloat(loging_avatar,"scaleX",0.5f,1.0f);
                ObjectAnimator scaleYanim = ObjectAnimator.ofFloat(loging_avatar,"scaleY",0.5f,1.0f);
                ObjectAnimator translate = ObjectAnimator.ofFloat(loging_avatar,"translationX",-20,20,0,-20,20,0);
                AnimatorSet set = new AnimatorSet();
                set.setDuration(100);
                set.playTogether(scaleXanim,scaleYanim,translate);
                set.start();

                loging_avatar.setImageResource(user.getAvatar());
                user_name_view.setText(user.getAccount());

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 password_layout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 登陆按钮
     * @param v
     */
    public void login(View v){
        Loging_user_name = user_name_view.getText().toString();
        password_str = password.getText().toString();
        if(musersDao.login(Loging_user_name,password_str)){  //登陆成功
            Intent mintent = new Intent(login.this,Desktop.class);
            startActivity(mintent);
            /**
             * 记录登陆状态
             */
            editor.putInt("log",1);  //1表示已有人登陆
            editor.putInt("first_open",1); //1表示第一次进入系统
            editor.putString("account_loged",Loging_user_name);  //记录登陆的用户的账号
            editor.commit();         //不能忘记这句话
            finish();
        }else {
            password_layout.setErrorEnabled(true);
            password_layout.setError("密码错误");
        }
    }
}
