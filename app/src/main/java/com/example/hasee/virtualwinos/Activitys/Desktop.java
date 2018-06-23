package com.example.hasee.virtualwinos.Activitys;

import android.animation.ObjectAnimator;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;

import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.virtualwinos.Adapter.fileadapter;
import com.example.hasee.virtualwinos.Adapter.processAdapter;
import com.example.hasee.virtualwinos.Adapter.waterfalladapter;
import com.example.hasee.virtualwinos.Adapter.zuse_processAdapter;
import com.example.hasee.virtualwinos.R;
import com.example.hasee.virtualwinos.System_Control.FileSystem;
import com.example.hasee.virtualwinos.System_Control.InetAddressManager;
import com.example.hasee.virtualwinos.System_Control.ProcessManage;
import com.example.hasee.virtualwinos.System_Control.memeory;
import com.example.hasee.virtualwinos.System_Control.memoryManager;
import com.example.hasee.virtualwinos.System_Control.mydialogManager;
import com.example.hasee.virtualwinos.System_Control.process;
import com.example.hasee.virtualwinos.audioPlay.MyMediaPlayer;
import com.example.hasee.virtualwinos.controlScreen.Morieation;
import com.example.hasee.virtualwinos.controlScreen.mySensorEventListener;
import com.example.hasee.virtualwinos.database.User;
import com.example.hasee.virtualwinos.database.openHelper;
import com.example.hasee.virtualwinos.database.usersDao;
import com.example.hasee.virtualwinos.staggedGridView.StaggeredGridView;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.Policy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static android.R.attr.bitmap;
import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.id;
import static android.media.CamcorderProfile.get;
import static android.os.Build.VERSION_CODES.M;


public class Desktop extends AppCompatActivity{

    /**
     * 各种算法的标志位
     */
    final static int FCFSFLAG = 0;  //先来先服务
    final static int SJFFLAG = 1;   //短作业优先
    final static int RRFLAG = 2;    //时间片轮转算法
    final static int PREFLAG = 3;   //优先权调度算法
    final static int REFLAG = 4;    //高响应比算法
    int current_process_aligo_FLAG = 0; //默认是先来先服务算法

    /**
     *时间片轮转调度算法的时间片长度为2
     */
    final int RR_TIME_LENGTH = 2;

    /**
     * 优先权调度算法的抢占和非抢占
     */
    final int GRABFLAG = 5;  //抢占式
    final int NOGRABFLAG = 6;//非抢占式
    int current_GRABFLAG = 6;  //当前的默认方式是非抢占式

    final static int FFFLAG = 7;  //首次适应算法
    final static int NFFLAG = 8;  //循环首次适应算法
    final static int BFFLAG = 9;  //最好适应算法
    final static int WFFLAG = 0;  //最坏适应算法

    int current_memory_aligo = FFFLAG; //默认是首次适应算法

    /**
     * 壁纸资源id
     */
    final int[] wallpaper = {
                             R.drawable.img0,
                             R.drawable.img1,
                             R.drawable.img2,
                             R.drawable.img3,
                             R.drawable.img4
    };
    /**
     * 头像资源id
     */
    final int[] avatarList = {
                             R.drawable.defaultavatar,
                             R.drawable.avatar_boy1,
                             R.drawable.avatar_boy2,
                             R.drawable.avatar_boy3,
                             R.drawable.avatar_boy4,
                             R.drawable.avatar_boy5,
                             R.drawable.avatar_girl1,
                             R.drawable.avatar_girl2,
                             R.drawable.avatar_girl3,
                             R.drawable.avatar_girl4,
                             R.drawable.photo10,
                             R.drawable.photo2,
                             R.drawable.photo3,
                             R.drawable.photo4,
                             R.drawable.photo5,
                             R.drawable.photo6,
                             R.drawable.photo71,
                             R.drawable.photo8,
                             R.drawable.photo9
    };

    /**
     * 自定义的对话框管理器
     */
    mydialogManager mdialogManager;
    /**
     * 屏幕的宽和高
     */
    int mscreen_width,mscreen_height;

    /**
     * 最外层的RelativeLayout
     */
     RelativeLayout outest_relativeLayout;
    /**
     * 传感器
     */
    SensorManager sensorManager;
    mySensorEventListener msel;
    LinearLayout tools_bar;
    Handler mhandler;
    /**
     * windows键选项对话框及组件
     */
    Dialog WindowsOptions;
    ImageView power;


    /**
     * 隐藏工具栏线程
     */
    Runnable hide;

    LinearLayout shut_down_progressbar;

    SharedPreferences.Editor editor;
    SharedPreferences read;

    /**
     *
     * 控制面板的窗口以及组件
     */
    Dialog control_panel_dialog;
    ImageView mmin,mmax,mclose,back;   //最小化，最大化，关闭
    TextView users_account_bn;

    ImageView control_panel;

    LinearLayout first,second;

    TextView change_avatr_bn,   //更改头像的按钮
            change_password_bn; //更改密码的按钮

    ImageView mavatar;
    TextView maccount;

    Stack<View> control_panel_stack;

    LinearLayout chage_pingju;

    EditText account,password;

    /**
     * 改变凭据完成按钮
     */
    TextView change_pingju_finish_bn;

    /**
     * 更改其他账户的信息界面
     */
    GridView users;
    TextView change_other_users_bn;
    LinearLayout change_other_users;
    TextView addUser;
    final int addUserFlag = 0;
    final int changepingjuGlag = 1;
    int flag = 0;
    String oldaccount;
    /**
     * 用户信息列表
     */
    List<HashMap<String,Object>> userlist;
    List<Object> userlists;

    SimpleAdapter adapter;
    /**
     * 外观和个性化界面
     */
    LinearLayout appearance_GUI;
    /**
     * 外观和个性化按钮
     */
    TextView appearance_bn;
    //返回按钮
    ImageView appearance_back;
    GridView waterfall_Pic_choose;

    /**
     *网络和Internet
     */
    ImageView InternetManage; //icon
    TextView Internet_bn;     //button
    LinearLayout ipaddress;   //网络管理界面
    TextView linkstate,       //本地连接连接状态
             benjiIp;         //本机IPv4地址

    /**
     * 我的电脑的窗口及组件
     */
    final int C_pan_size = 10240; //c盘容量为/*(1048576B=1M)*/(10kb)
    ProgressBar C_pan_used_progressbar;
    TextView C_pan_used_text;
    Dialog mycomputer;
    LinearLayout c_pan,d_pan;
    ImageView
            mmin_computer, //最小化
            mmax_computer,   //最大化
            mcomputer,       //我的电脑图标
            mclose_computer; //关闭
    EditText Nav;
    GridView C_container;
    LinearLayout C_container_GUI;
    LinearLayout mycomputer_first;
    File[] files;  //文件夹下的所有内容
    List<File> filesList;
    fileadapter madapter;
    ImageView mycomputer_back;
    TextView mmenu;
    FileSystem fileSystem;  //文件操作类
    /**
     * 新建对话框及其组件
     */
    Dialog newCreate_dialog;
    TextView create_dir,create_file;

    /**
     * 记事本对话框
     */
    Dialog noteBook_dialog;
    EditText file_tetx;
    TextView file_menu;
    ImageView max_note,
              min_note,
              close_note;
    ImageView notebookicon;
    TextView file_path_noteBook;

    /**
     * 重命名对话框
     */
    Dialog mdialog;
    TextView old_name,finish_bn;
    EditText new_name;

    /**
     * 数据库操作
     */
    usersDao udao;
    openHelper oHelper;
    User user;  //当前登陆的用户

    /**
     * 更改头像的界面
     */
    LinearLayout change_avatar_GUI;
    StaggeredGridView change_avatar_gridview;

    /**
     * 任务管理器窗口
     */
    ImageView managetaskIcon;  //任务管理器桌面图标
    Dialog ManageTask;
    ImageView mk_min,  //最小化
              mk_close; //关闭

    List<process> jiuxvList;
    List<process> zuseList;

    //增加process的按钮
    ImageView add_bn;

    processAdapter mprocessadapter_jiuxv;  //就绪队列的适配器
        zuse_processAdapter mprocessadapter_zuse;    //阻塞队列的适配器


    ListView jiuxv_listview, //就绪队列listview
             zuse_Listview;  //阻塞队列listview

    Spinner process_aligo,  //进程调度算法选择
            grab_or_not ,   //是否抢占的选择
            memory_aligo_choose; //内存分配算法选择

    ProcessManage processManage;
    TextView current_process_pid;
    TextView current_process_myprocess;

    process current_exe_process; //当前运行的进程

    volatile int cpu_num = 1;  //处理机数目,使用volatile，同时只有一个线程可以修改这个值

    ProgressBar current_process_finishprocess;
    /**
     * 控制进程的句柄
     */
    Handler process_handler;

    Thread process_choose_thread; //模拟进程调度的线程
    Thread process_exe_thred;  //模拟进程运行的线程
    Boolean zhongdaun_FLAG = false;  //当前运行进程是否中断
    process newAddProcess;  //按下按钮时新增的进程
    Boolean zuse_FLAG = false;  //当前运行进程是否被阻塞
    /**
     *内存管理
     */
    LinearLayout memory;
    final int TOTAL_MEMORY = 100;  //内存大小默认为100兆

    List<memeory> memoryLists;
    memoryManager memorymanager;
    int available_last_index = 0;  //初始化为0
    TextView memory_decription;


    /**
     * 远程桌面配置对话框
     *
     */
    ImageView remoteDeskTopIcon;  //远程桌面图标

    Dialog remoteSetDialog;  //远程桌面配置对话框
    EditText remoteIp;       //远程电脑Ip
    Button  remoteOkbutton;  //确定按钮
    Socket socketRequest;    //请求远程卓面的socket
    ServerSocket serverSocketResponse;  //回应远程桌面的serverSocket(Port:6666)
    FloatingActionButton linkanim;
    ObjectAnimator rotateAnim;
    LinearLayout remote_desktop_set_first;

    /**
     * 远程桌面展示对话框
     */
    Dialog remote_desktop;        //远程桌面对话框
    ImageView remote_min,         //最小化
              remote_max,         //最大化
              remote_close,       //关闭
              remote_desktop_show;//展示远程桌面
    TextView remoteIpTextview;
    ServerSocket serverSocket;    //接受传输画面的serverSocket(Port:6667)
    Socket socket;                //发送传输画面的socket
    Bitmap reveiveBitmap;
    Thread receiveThread;
    volatile boolean receiveRunnableCanRun = true;
    /**
     * 传输的Bitmap
     */
    Bitmap sendBitmap;
    Thread mCaptureThread,       //截屏线程
           msendThread;          //发送线程
    CaptureThread capturethread;
    sendThread sendthread;
    MediaProjectionManager mediaProjectionManager;
    final static int REQUEST_PROJECTION = 123;
    /**
     * 请求选择对话框
     */
    Dialog chooseForRequestDialog;
    TextView requestText;  //请求说明
    Button requestOkBn,    //允许
           requestNoBn;    //拒绝



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        setContentView(R.layout.activity_desktop);

        /**
         * 得到手机的宽和高（像素）
         */
        DisplayMetrics mdisplayMetrics = getResources().getDisplayMetrics();
        mscreen_height = mdisplayMetrics.heightPixels;
        mscreen_width = mdisplayMetrics.widthPixels;
        mdialogManager = new mydialogManager(mscreen_width,mscreen_height);
        fileSystem  = new FileSystem(this);
        init();
        /**
         * 传感器监听句柄
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

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                        }
                        /**
                         * 手机右侧朝下
                         */
                        if(x<-3){

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        }
                        break;

                    case 123:
                        String result = (String) msg.obj;
                        switch (result){
                            default:
                                rotateAnim.end();
                                linkanim.setImageResource(R.drawable.failure);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mhandler.sendEmptyMessage(567);
                                    }
                                }).start();
                                break;
                            case "OK":
                                rotateAnim.end();
                                linkanim.setImageResource(R.drawable.success);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mhandler.sendEmptyMessage(456);

                                    }
                                }).start();





                                break;
                            case "NO":
                                mhandler.sendEmptyMessage(678);

                                break;

                        }
                        break;
                    /**
                     * 显示接收到的画面
                     */
                    case 234:
                        try{

                            remote_desktop_show.setImageBitmap(reveiveBitmap);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    /**
                     * 接收到请求后选择同意或者拒绝的对话框
                     */
                    case 345:
                        if(chooseForRequestDialog==null)
                            createChooseForRequestDialog();
                        chooseForRequestDialog.show();
                        break;
                    case 456:
                        remoteSetDialog.dismiss();
                        remoteSetDialog.cancel();
                        //cancle后remoteSetDialog并没有变为null
                        remoteSetDialog = null;
                        if(remoteDeskTopIcon.getTag()==null)
                            createTuoPan(remoteDeskTopIcon);
                        if(remote_desktop==null){
                            createRemoteDesktopDialog();
                            remoteIpTextview.setText(InetAddressManager.ServerIp);
                            remote_desktop.show();
                            Window win = remote_desktop.getWindow();
                            WindowManager.LayoutParams lp = win.getAttributes();
                            lp.width = mscreen_width*3/4;
                            lp.height = mscreen_height*3/4;
                            win.setAttributes(lp);
                        }else {
                            if(!remote_desktop.isShowing())
                                remote_desktop.show();
                        }


                        break;
                    case 567:
                        remoteSetDialog.dismiss();
                        remoteSetDialog.cancel();
                        remoteSetDialog = null;
                        Toast.makeText(Desktop.this, "远程计算机拒绝了您的请求", Toast.LENGTH_SHORT).show();

                        break;

                    case 678:
                        rotateAnim.end();
                        linkanim.setImageResource(R.drawable.failure);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mhandler.sendEmptyMessage(567);
                            }
                        }).start();
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        /**
         * 初始化传感器及监听
         */
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        msel = new mySensorEventListener(mhandler);

        /**
         * 构造windows菜单对话框
         */


        pop_windows_options();


        process_handler = new Handler(){
            int temp_myprocess;
            process temp_process;
            double baifenbi = 0;
            @Override
            public void handleMessage(Message msg) {


                switch (msg.what){
                    /**
                     * 恢复中断前的现场
                     */
                    case 123:
                       temp_process = (process) msg.obj;
                        current_process_pid.setText(temp_process.getPid());
                        current_process_finishprocess.setMax(temp_process.getTotal_time());
                        current_process_finishprocess.setProgress(temp_process.getMyprocess());
                        temp_myprocess = temp_process.getMyprocess();
                        if(temp_myprocess==0){
                            current_process_myprocess.setText("0.0%");
                        }else {

                            baifenbi = (double)temp_myprocess/temp_process.getTotal_time()*100;
                            /**
                             * 保留小数点后一位
                             */
                            DecimalFormat df1 = new DecimalFormat("#.0");

                            current_process_myprocess.setText(df1.format(baifenbi)+"%");
                        }
                        break;
                    case 234:
                        temp_myprocess = (int) msg.obj;
                        current_process_finishprocess.setProgress(temp_myprocess);
                        current_exe_process.setMyprocess(temp_myprocess);

                        baifenbi = (double)temp_myprocess/current_exe_process.getTotal_time()*100;
                        /**
                         * 保留小数点后一位
                         */
                        DecimalFormat df = new DecimalFormat("#.0");

                        current_process_myprocess.setText(df.format(baifenbi)+"%");

                        /**
                         * 更新进程当前完成的时间
                         */
                        processManage.modifyJiuxv(current_exe_process);
                        break;
                    /**
                     * 正常结束（运行完毕）
                     */
                    case 345:

                        processManage.removeprocessJiuxv(current_exe_process);
                        if(process_exe_thred!=null){
                            process_exe_thred.interrupt();
                            process_exe_thred = null;
                        }

                        memorymanager.huishou(current_exe_process);

                        current_exe_process = null;

                        cpu_num = 1;  //释放资源
                        /**
                         * 清零
                         */
                        current_process_pid.setText("");
                        current_process_finishprocess.setProgress(0);
                        current_process_myprocess.setText("0.0%");

                        break;
                    /**
                     * 被中断
                     */
                    case 456:

                        /**
                         * 该种情况不需要也不能释放cpu(cpu_num=1),
                         * 因为需要指定新添加的为当前运行的进程，
                         * 如果显示释放cpu(cpu_num=1),则会从就绪队列中选择一个进程，
                         * 这是我们不希望看到的
                         */
                        processManage.modifyJiuxv(current_exe_process);

                        /**
                         * 清零
                         */
                        current_process_pid.setText("");
                        current_process_finishprocess.setProgress(0);

                        current_exe_process = null;
                        current_exe_process = newAddProcess;
                        process_exe_thred = new Thread(process_exe);
                        zhongdaun_FLAG = false;  //停止中断
                        process_exe_thred.start();
                        break;
                    /**
                     * 时间片用完（时间片轮转调度算法中）
                     */
                    case 567:
                        processManage.modifyJiuxv(current_exe_process);
                        processManage.to_tail(current_exe_process);
                        /**
                         * 清零
                         */
                        current_process_pid.setText("");
                        current_process_finishprocess.setProgress(0);

                        current_exe_process = null;
                        cpu_num = 1;

                        break;
                    /**
                     * 维护进程的等待时间
                     */
                    case 789:
                        mprocessadapter_jiuxv.notifyDataSetChanged();
                        break;
                    case 890:
                        processManage.modifyJiuxv(current_exe_process);

                        /**
                         * 清零
                         */
                        current_process_pid.setText("");
                        current_process_finishprocess.setProgress(0);
                        processManage.addprocessZuse(current_exe_process);
                        current_exe_process = null;
                        zuse_FLAG = false;
                        zhongdaun_FLAG = false;
                        /**
                         * 释放cpu
                         */
                        cpu_num = 1;

                        break;
                    default:
                        break;
                }


                super.handleMessage(msg);
            }
        };
        if(process_choose_thread==null)
            process_choose_thread =new Thread(process_choose);
            process_choose_thread.start();

        new Thread(cal_waitTime).start();
    }

    /**
     * 初始化
     */
    public void init(){

        jiuxvList = new ArrayList<>();
        zuseList = new ArrayList<>();

        editor = getSharedPreferences("SystemState",MODE_PRIVATE).edit();
        read = getSharedPreferences("SystemState",MODE_PRIVATE);

        tools_bar = (LinearLayout)findViewById(R.id.tools_bar);
        outest_relativeLayout = (RelativeLayout)findViewById(R.id.desk_top);
        shut_down_progressbar = (LinearLayout) findViewById(R.id.shutdown_progressbar);

        mcomputer = (ImageView)findViewById(R.id.computer_icon);

        control_panel = (ImageView)findViewById(R.id.control_panel);

        managetaskIcon = (ImageView)findViewById(R.id.manage_task);


        /**
         * 实例化sqliteOPenHelper对象
         */
        oHelper = new openHelper(this,"users",null,1);
        user = new User();
        udao = new usersDao(oHelper);
        String account = read.getString("account_loged","");
        user = udao.query(account);

        control_panel_stack = new Stack<>();

        int wallpaperid = read.getInt("wallpaper",0);
        if(wallpaperid!=0)
        outest_relativeLayout.setBackgroundResource(wallpaperid);

        /**
         * 任务管理器点击响应事件
         */
        managetaskIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ManageTask==null){
                    create_manageTask();
                    createTuoPan(managetaskIcon);
                    ((ImageView)managetaskIcon.getTag()).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!ManageTask.isShowing())
                                ManageTask.show();
                        }
                    });
                }

                if(!ManageTask.isShowing())
                    ManageTask.show();
                    Window window = ManageTask.getWindow();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.width = mscreen_width;
                    lp.height = mscreen_height;
                    window.setAttributes(lp);

            }
        });

        /**
         * 远程桌面图标及事件响应
         */
        remoteDeskTopIcon = (ImageView)findViewById(R.id.remoteDeskTop);
        remoteDeskTopIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remoteSetDialog==null)
                    createRemoteSetDialog();
                remoteSetDialog.show();
            }
        });

        new Thread(responseRunnable).start();

        if(receiveThread==null){
            receiveThread = new Thread(receiveRunnable);

            receiveThread.start();
        }
        receiveRunnableCanRun = true;
    }

    @Override
    protected void onResume() {
        /**
         * 注册监听
         */
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(msel,sensor,SensorManager.SENSOR_DELAY_UI);
        hide = new hide_tools_bar();
        super.onResume();

        /**
         * 加载文件系统
         */
        fileSystem.getFiles("C");
        createnew_Create_dialog();
    }

    @Override
    protected void onPause() {
        /**
         * 取消监听
         */
        sensorManager.unregisterListener(msel);
        super.onPause();


    }

    /**
     * 打开我的电脑按钮
    * @param v
     */
    public void mycomputer(View v){
        if(v.getTag()==null)    //还没有托盘
            createTuoPan((ImageView) v);
        if(mycomputer==null)  //还没有创建我的电脑的窗口
        {
            create_mycomputer_window();

            mycomputer.show();
            /**
             * 窗口默认大小为屏幕长宽的一半
             */
            Window window = mycomputer.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = mscreen_width/4*3;
            lp.height = mscreen_height/4*3;
            window.setAttributes(lp);



        }else {
            if(!mycomputer.isShowing()){
                mycomputer.show();
                /**
                 * 窗口默认大小为屏幕长宽的3/4
                 */
                Window window = mycomputer.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = mscreen_width/4*3;
                lp.height = mscreen_height/4*3;
                window.setAttributes(lp);
            }
        }
        /**
         * 我的电脑托盘点击事件
         */
        ((ImageView)mcomputer.getTag()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycomputer.show();
            }
        });
    }

    /**
     * windows键
     * @param v
     */
    public void open_window(View v){
        WindowsOptions.show();
        Window dialogWindow = WindowsOptions.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.LEFT|Gravity.BOTTOM);

        lp.width = mscreen_width/4;
        lp.height = mscreen_height/2;
        lp.x = 0;
        lp.y = 10;
        lp.alpha = 0.4f;   //透明度
        dialogWindow.setAttributes(lp);
    }

    /**
     * 工具栏的显示逻辑(点击桌面空白处)
     */
    public void show_Tools_bar_Onclick(View v){
         show_Tools_bar();
        /**
         * 点击桌面空白处，隐藏windows菜单
         */
        if(WindowsOptions.isShowing())
             WindowsOptions.dismiss();
    }



    /**
     * 隐藏工具栏的线程
     */
    class hide_tools_bar implements Runnable{
        @Override
        public void run() {
                if(tools_bar.getVisibility()==View.VISIBLE)
                show_Tools_bar();
        }
    }

    /**
     * 工具栏的显示逻辑
     */
    public void show_Tools_bar(){
        if(tools_bar.getVisibility()==View.VISIBLE)
            tools_bar.setVisibility(View.INVISIBLE);
        else
        {
            tools_bar.setVisibility(View.VISIBLE);
            /**
             * 5秒之后隐藏
             */
            mhandler.postDelayed(hide,5000);
        }
    }

    /**
     * windows键菜单
     */
    void pop_windows_options(){
        View mview = getLayoutInflater().inflate(R.layout.windows_options,null);
        power = (ImageView) mview.findViewById(R.id.mpower);
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        mbuilder.setView(mview);
        WindowsOptions = mbuilder.create();
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               PopupMenu powerMenu = new PopupMenu(Desktop.this,power);
                powerMenu.getMenuInflater().inflate(R.menu.power_menu,powerMenu.getMenu());

                powerMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.restart:
                                /**
                                 * 重启
                                 */
                                restart();
                                break;
                            case R.id.shutdown:
                                /**
                                 * 关机
                                 */
                                shutdown();
                                mhandler.postDelayed(new finish(),3100);
                                break;
                            case R.id.logoff:
                                /**
                                 * 用户退出登录
                                 */
                                logoff();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                powerMenu.show();

            }
        });

    }
    public void restart(){
        shutdown();
        mhandler.postDelayed(new restart(),3100);
    }

    public void shutdown(){
        // TODO: 2018/5/8 释放资源
        editor.putInt("open",0);
        editor.putInt("log",0);
        editor.putString("account_loged","");
        editor.commit();
        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(outest_relativeLayout,"alpha",1.0f,0.0f);
        alphaanim.setDuration(3000);
        alphaanim.start();
        shut_down_progressbar.setVisibility(View.VISIBLE);
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
            Intent mintent = new Intent(Desktop.this,startAnimation.class);
            startActivity(mintent);
            finish();
        }
    }

    /**
     * 注销用户
     */
    public void logoff(){
        editor.putInt("log",0);
        editor.putString("account_loged","");   //注销用户
        editor.commit();
        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(outest_relativeLayout,"alpha",1.0f,0.0f);
        alphaanim.setDuration(3000);
        alphaanim.start();
        /**
         * 用户退出登录的声音
         */
        MediaPlayer mediaPlayer = MyMediaPlayer.getMediaplay(this,R.raw.windows_logoff);
        mediaPlayer.start();
        mhandler.postDelayed(new logoffRunnable(),3000);
    }

    class logoffRunnable implements Runnable{
        @Override
        public void run() {
            Intent mintent = new Intent(Desktop.this,lock.class);
            startActivity(mintent);
            finish();
        }
    }

    /**
     * 控制面板点击响应事件处理
     * @param v
     */
    public void mycontrolpanel(View v){
        if(v.getTag()==null)
            createTuoPan((ImageView)v);
        if(control_panel_dialog==null){
            /**
             * 创建对话框（窗口）
             */
            create_control_panel_window();
            control_panel_dialog.show();
            Window window = control_panel_dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = mscreen_width/4*3;
            lp.height = mscreen_height/4*3;
            window.setAttributes(lp);

        }
        else {
            if(!control_panel_dialog.isShowing())
            /**
             * 显示对话框（窗口）
             */
            control_panel_dialog.show();
            Window window = control_panel_dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = mscreen_width/4*3;
            lp.height = mscreen_height/4*3;
            window.setAttributes(lp);
        }
        ((ImageView)control_panel.getTag()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control_panel_dialog.show();
            }
        });


    }

    /**
     * 托盘化
     * @param icon
     */
    public void createTuoPan(ImageView icon){
        /**
         * 在工具栏生成图标
         */
        ImageView imageView = new ImageView(this);
        /**
         * 获取icon上的图片
         */
        Drawable drawable = icon.getBackground();
        int width = dip2px(this,30);
        int height = dip2px(this,30);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
        imageView.setLayoutParams(lp);
        imageView.setBackground(drawable);
        tools_bar.addView(imageView);
        icon.setTag(imageView);
    }

    /**单位转换
     * 从dp转换为px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 构造控制面板的窗口
     */
    void create_control_panel_window(){
        String accountstr = read.getString("account_loged","");
        user = udao.query(accountstr);

        View view = getLayoutInflater().inflate(R.layout.windows,null);
        mmin = (ImageView)view.findViewById(R.id.min_bn);
        mmax = (ImageView)view.findViewById(R.id.max_bn);
        mclose = (ImageView)view.findViewById(R.id.close_bn);
        back = (ImageView)view.findViewById(R.id.back_bn);
        users_account_bn = (TextView)view.findViewById(R.id.users_account_bn);
        mavatar = (ImageView)view.findViewById(R.id.mavatar);
        maccount = (TextView)view.findViewById(R.id.maccount);
        change_other_users_bn = (TextView)view.findViewById(R.id.change_other_users_bn);
        chage_pingju = (LinearLayout)view.findViewById(R.id.change_pingju);

        account = (EditText)view.findViewById(R.id.account);
        password = (EditText)view.findViewById(R.id.password);

        change_pingju_finish_bn = (TextView)view.findViewById(R.id.change_pingju_finish_bn);

        change_other_users = (LinearLayout)view.findViewById(R.id.change_other_users);

        users = (GridView) view.findViewById(R.id.users_gridview);

        addUser = (TextView)view.findViewById(R.id.addUser);

        /**
         * 网络和Internet
         */
        InternetManage = (ImageView)view.findViewById(R.id.internet_icon);
        Internet_bn = (TextView)view.findViewById(R.id.internet_bn);
        ipaddress = (LinearLayout)view.findViewById(R.id.Ipaddress);
        linkstate = (TextView)view.findViewById(R.id.linkstate);
        benjiIp = (TextView)view.findViewById(R.id.benjiIp);
        Internet_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String benjiipstr;
                control_panel_stack.add(first);
                control_panel_stack.peek().setVisibility(View.GONE);
                benjiipstr = InetAddressManager.getLocalIp();
                if(benjiipstr==null)
                {
                    linkstate.setText("本地连接：已断开");
                    benjiIp.setText("");
                }else {
                    linkstate.setText("本地连接：已连接");
                    benjiIp.setText(benjiipstr);
                }
                ipaddress.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                control_panel_stack.add(ipaddress);
            }
        });


        /**
         * 外观和个性化界面
         */
        appearance_GUI = (LinearLayout)view.findViewById(R.id.pic_choose);
        appearance_back = (ImageView)view.findViewById(R.id.bizhi_back);
        waterfall_Pic_choose = (GridView) view.findViewById(R.id.waterfall_pic);
        appearance_bn = (TextView)view.findViewById(R.id.theme_bn);
        /**
         * 更改头像界面
         */
        change_avatar_GUI = (LinearLayout)view.findViewById(R.id.change_avatar_GUI);
        change_avatar_gridview = (StaggeredGridView)view.findViewById(R.id.change_avatr_gridview);
        change_avatr_bn = (TextView)view.findViewById(R.id.change_avatr);

        waterfalladapter madapter = new waterfalladapter(avatarList,Desktop.this);
        change_avatar_gridview.setAdapter(madapter);
        madapter.notifyDataSetChanged();
        /**
         * 点击待选头像
         */
        change_avatar_gridview.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
            @Override
            public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("wallpaperId",avatarList[position]);
                Intent mintent = new Intent(Desktop.this,showwallpaper.class);
                mintent.putExtras(bundle);
                startActivity(mintent);
            }
        });
        /**
         * 长按更改头像
         */
        change_avatar_gridview.setOnItemLongClickListener(new StaggeredGridView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(StaggeredGridView parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(Desktop.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.change_avatar,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String maccount_str = maccount.getText().toString();
                        User tempuser = udao.query(maccount_str);
                        tempuser.setAvatar(avatarList[position]);
                        if(udao.mmodify(tempuser,tempuser.getAccount())){

                            Toast.makeText(Desktop.this, "头像更改成功！", Toast.LENGTH_SHORT).show();
                            mavatar.setImageResource(avatarList[position]);
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });

        change_avatr_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                control_panel_stack.peek().setVisibility(View.GONE);
                control_panel_stack.add(change_avatar_GUI);
                change_avatar_GUI.setVisibility(View.VISIBLE);

            }
        });


        /**
         * 初始化
         */
        mavatar.setImageResource(user.getAvatar());
        maccount.setText(user.getAccount());
        account.setText(user.getAccount());
        password.setText(user.getPassword());

        /**
         * 初始化数据
         */
        inituserlists();


        String[] from = {"avatar","account"};
        int [] to = {R.id.avatar,R.id.account};

        adapter = new SimpleAdapter(this,userlist,R.layout.userlistadapter,from,to);
        users.setAdapter(adapter);
        /**
         * 点击用户列表中的用户
         */
        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                control_panel_stack.peek().setVisibility(View.GONE);
                second.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                /**
                 * 如果是管理员则显示更改其他账户信息
                 */
                User muser = (User) userlists.get(position);
                /**
                 * 显示对应用户的头像和账户
                 */
                mavatar.setImageResource(muser.getAvatar());
                maccount.setText(muser.getAccount());

                if(muser.getNickname()!=null&&muser.getNickname().equals("Admin"))
                    change_other_users_bn.setVisibility(View.VISIBLE);
                else change_other_users_bn.setVisibility(View.GONE);
                control_panel_stack.add(second);
            }
        });
        /**
         * 长按用户列表中的用户
         */
        users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final User muser = (User) userlists.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(Desktop.this);
                builder.setTitle("删除用户").setMessage("确定删除用户"+muser.getAccount()+"?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(udao.delete(muser))
                            Toast.makeText(Desktop.this, "成功删除用户"+muser.getAccount(), Toast.LENGTH_SHORT).show();
                            inituserlists();
                            adapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        /**
         * 返回按钮点击事件
         */

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control_panel_stack.size()>1) {

                    if(control_panel_stack.size()==2)  //弹出的是second
                    {
                        change_other_users_bn.setVisibility(View.GONE);
                        back.setVisibility(View.GONE);
                        control_panel_stack.pop().setVisibility(View.GONE);
                        control_panel_stack.pop().setVisibility(View.VISIBLE);
                    }
                    else{

                        control_panel_stack.pop().setVisibility(View.GONE);
                        control_panel_stack.peek().setVisibility(View.VISIBLE);
                    }

                }
            }
        });


        /**
         * 更改账户信息显示的画面
         */
        first = (LinearLayout)view.findViewById(R.id.first);
        second = (LinearLayout)view.findViewById(R.id.second);

        /**
         * 更改头像和凭据的按钮
         */
        change_password_bn = (TextView)view.findViewById(R.id.changepassword);

        change_password_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = changepingjuGlag;
                oldaccount=maccount.getText().toString();
                account.setText(oldaccount);
                User muser = udao.query(oldaccount);
                password.setText(muser.getPassword());
                control_panel_stack.peek().setVisibility(View.GONE);
                chage_pingju.setVisibility(View.VISIBLE);
                control_panel_stack.add(chage_pingju);
            }
        });


        /**
         * 账户信息管理的点击响应事件
         */
        users_account_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control_panel_stack.add(first);
                control_panel_stack.peek().setVisibility(View.GONE);
                second.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                /**
                 * 如果是管理员则显示更改其他账户信息
                 */
                if(user.getNickname()!=null&&user.getNickname().equals("Admin"))
                    change_other_users_bn.setVisibility(View.VISIBLE);
                control_panel_stack.add(second);
            }
        });


        //最小化
        mmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialogManager.mined(control_panel_dialog);
            }
        });
        //关闭
        mclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools_bar.removeView((View) control_panel.getTag());
                control_panel.setTag(null);
               mdialogManager.closed(control_panel_dialog);
                control_panel_dialog = null;
            }
        });
        //最大化
        mmax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mdialogManager.maxed(control_panel_dialog);
            }
        });
        /**
         * 更改凭据完成按钮（添加用户完成按钮）
         */
        change_pingju_finish_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String maccount = account.getText().toString();
                String mpassword = password.getText().toString();
                User muser = new User();
                muser.setAccount(maccount);
                muser.setPassword(mpassword);

                if(flag==changepingjuGlag){
                    if(user.getAccount().equals(oldaccount)){
                        /**
                         * 修改的是登陆用户的账号密码
                         */
                        user.setAccount(maccount);
                        user.setPassword(mpassword);
                        if(udao.mmodify(user,oldaccount)){

                            Toast.makeText(Desktop.this, "更改成功,即将重启", Toast.LENGTH_SHORT).show();;
                            control_panel_dialog.dismiss();
                            restart();
                        }else Toast.makeText(Desktop.this, "更改失败", Toast.LENGTH_SHORT).show();
                    }else {
                        /**
                         * 修改的其他用户的账号密码
                         */
                        User muser1 = udao.query(oldaccount);
                        muser1.setAccount(maccount);
                        muser1.setPassword(mpassword);
                        if(udao.mmodify(muser1,oldaccount)){
                            Toast.makeText(Desktop.this, "更改成功", Toast.LENGTH_SHORT).show();;
                            inituserlists();
                            adapter.notifyDataSetChanged();
                        }else Toast.makeText(Desktop.this, "更改失败", Toast.LENGTH_SHORT).show();
                    }

                }
                else if(flag==addUserFlag){
                    if(udao.insert(muser)){
                        Toast.makeText(Desktop.this, "添加用户成功", Toast.LENGTH_SHORT).show();
                        inituserlists();
                        adapter.notifyDataSetChanged();
                    }else Toast.makeText(Desktop.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         * 进入其他用户管理界面
         */
        change_other_users_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control_panel_stack.peek().setVisibility(View.GONE);
                change_other_users.setVisibility(View.VISIBLE);
                control_panel_stack.add(change_other_users);
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = addUserFlag;
                account.setText("");
                password.setText("");
                control_panel_stack.peek().setVisibility(View.GONE);
                chage_pingju.setVisibility(View.VISIBLE);
                control_panel_stack.add(chage_pingju);

            }
        });

        appearance_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                waterfalladapter madapter = new waterfalladapter(wallpaper,Desktop.this);
                waterfall_Pic_choose.setAdapter(madapter);
                madapter.notifyDataSetChanged();
                first.setVisibility(View.GONE);
                appearance_GUI.setVisibility(View.VISIBLE);

            }
        });

        appearance_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appearance_GUI.setVisibility(View.GONE);
                first.setVisibility(View.VISIBLE);
            }
        });
        /**
         * 每一项的点击事件
         */
        waterfall_Pic_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("wallpaperId",wallpaper[position]);
                Intent mintent = new Intent(Desktop.this,showwallpaper.class);
                mintent.putExtras(bundle);
                startActivity(mintent);

            }
        });
        /**
         * 长按
         */
        waterfall_Pic_choose.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(Desktop.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.setappearance,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.set_wallpaper:
                                Toast.makeText(Desktop.this, "设置成功", Toast.LENGTH_SHORT).show();
                                editor.putInt("wallpaper",wallpaper[position]);
                                editor.commit();
                                outest_relativeLayout.setBackgroundResource(wallpaper[position]);
                                break;
                            case R.id.set_lockpaper:
                                Toast.makeText(Desktop.this, "设置成功", Toast.LENGTH_SHORT).show();
                                editor.putInt("lockpaper",wallpaper[position]);
                                editor.commit();
                                break;
                            case R.id.set_together:
                                Toast.makeText(Desktop.this, "设置成功", Toast.LENGTH_SHORT).show();
                                editor.putInt("wallpaper",wallpaper[position]);
                                editor.putInt("lockpaper",wallpaper[position]);
                                editor.commit();
                                outest_relativeLayout.setBackgroundResource(wallpaper[position]);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                return true;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        control_panel_dialog = builder.create();


    }

    /**
     * 初始化用户信息列表
     */
    public void inituserlists(){
        /**
         * 必须是同一个数据源，nitifyDataSetChanged才有效
         */
        if(userlist==null)
        userlist = new ArrayList<>();
        else userlist.clear();
        //必须是同一个数据源，nitifyDataSetChanged才有效
        userlists = udao.query();

        HashMap<String,Object> hashMap;
        User user1;
        for(int i = 0;i<userlists.size();i++){
            hashMap = new HashMap<>();
            user1 = (User)userlists.get(i);
                hashMap.put("avatar", user1.getAvatar());
                hashMap.put("account", user1.getAccount());
                userlist.add(hashMap);
        }
    }

    /**
     * 构造我的电脑窗口
     */
    public void create_mycomputer_window(){

        View view = getLayoutInflater().inflate(R.layout.mycomputer,null);
        c_pan = (LinearLayout)view.findViewById(R.id.c_pan);
        d_pan = (LinearLayout)view.findViewById(R.id.d_pan);
        mmin_computer = (ImageView) view.findViewById(R.id.mmin);
        mmax_computer = (ImageView)view.findViewById(R.id.mmax);
        mclose_computer = (ImageView)view.findViewById(R.id.mclose);
        Nav = (EditText) view.findViewById(R.id.Nav);
        C_container_GUI = (LinearLayout)view.findViewById(R.id.C_container_GUI);
        C_container = (GridView)view.findViewById(R.id.C_container);
        mycomputer_first = (LinearLayout)view.findViewById(R.id.first);
        mycomputer_back = (ImageView) view.findViewById(R.id.mycomputer_back);
        C_pan_used_progressbar = (ProgressBar)view.findViewById(R.id.progressBar);
        C_pan_used_text = (TextView)view.findViewById(R.id.description_memory_of_c);

        mmenu = (TextView)view.findViewById(R.id.mmenu);
        mmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCreate_dialog.show();
            }
        });

        long C_used = fileSystem.get_used_size_of_C_pan();
        Log.i("C_used",C_used+"");
        C_pan_used_text.setText((C_pan_size-C_used)+"b可用，共10kb("+C_pan_size+"b)");
        C_pan_used_progressbar.setProgress((int)C_used);



        /**
         * 文件管理中返回键的事件处理
         */
        mycomputer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parent = Nav.getText().toString();
                if(parent.lastIndexOf("/")==-1){  //在c盘根目录
                    C_container_GUI.setVisibility(View.GONE);
                    mycomputer_first.setVisibility(View.VISIBLE);
                }else{
                    parent = parent.substring(0,parent.lastIndexOf("/"));
                    Nav.setText(parent);
                    files = fileSystem.getFiles(parent);
                    array2List(files,filesList);
                    madapter.notifyDataSetChanged();
                }
            }
        });
        /**
         * c_pan按钮响应事件（进入c盘）
         */
        c_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycomputer_first.setVisibility(View.GONE);
                C_container_GUI.setVisibility(View.VISIBLE);
                Nav.setText("C");

                files = fileSystem.getFiles("C");
                if(filesList==null)
                    filesList = new ArrayList<File>();
                array2List(files,filesList);



                for(int i = 0;i<files.length;i++){
                    Log.i(files[i].getName()+"_size",fileSystem.get_fit_size_of(files[i])+"");
                }
                Log.i("c盘容量",fileSystem.get_fit_used_size_of_C_pan());

                 madapter = new fileadapter(filesList,getLayoutInflater());
                C_container.setAdapter(madapter);

                C_container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (fileSystem.getfiletype(files[position])){
                            case "txt":
                                if(noteBook_dialog==null)
                                create_notebook_dialog(files[position],Nav.getText().toString());
                                noteBook_dialog.show();
                                Window window = noteBook_dialog.getWindow();
                                WindowManager.LayoutParams lp =window.getAttributes();
                                lp.width = mscreen_width/4*3;
                                lp.height = mscreen_height/4*3;
                                window.setAttributes(lp);
                                break;
                            case "dir":
                                Nav.append("/"+files[position].getName());
                                files = fileSystem.getFiles(Nav.getText().toString());
                                array2List(files,filesList);
                                madapter.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    }
                });

                C_container.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                        PopupMenu powerMenu = new PopupMenu(Desktop.this,view);
                        powerMenu.getMenuInflater().inflate(R.menu.right_menu,powerMenu.getMenu());

                        powerMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String  mparent = "";
                                switch (item.getItemId()){
                                    case R.id.deleteFile:
                                        /**
                                         * 删除文件
                                         */
                                         mparent = Nav.getText().toString();
                                        Log.i("mparent",mparent);
                                        fileSystem.deleteDirectory(Desktop.this.getFilesDir().toString()+File.separator+mparent+File.separator+files[position].getName());
                                        files = fileSystem.getFiles(mparent);
                                        array2List(files,filesList);
                                        madapter.notifyDataSetChanged();
                                        break;
                                    case R.id.rename:
                                        /**
                                         * 重命名文件
                                         */
                                        final TextView filenameedit = (TextView)view.findViewById(R.id.file_name);
                                        mparent = Nav.getText().toString();
                                        create_rename_dialog(files[position],mparent,filenameedit);
                                        mdialog.show();

                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            }
                        });
                        powerMenu.show();

                        //return true 不会触发onclick
                        return true;
                    }
                });



            }
        });

        d_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/9 显示d_pan 的文件夹（内容）
            }
        });

        //最小化
        mmin_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialogManager.mined(mycomputer);
            }
        });
        //关闭
        mclose_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools_bar.removeView((View) mcomputer.getTag());
                mcomputer.setTag(null);
                mdialogManager.closed(mycomputer);
                mycomputer = null;
            }
        });
        //最大化
        mmax_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialogManager.maxed(mycomputer);
            }
        });

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setView(view);
        mycomputer = build.create();


    }

    /**
     * 构造新建对话框
     */
    public void createnew_Create_dialog(){
        View v = getLayoutInflater().inflate(R.layout.newcreate,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建").setView(v);
        create_file = (TextView)v.findViewById(R.id.create_txt);
        create_dir = (TextView)v.findViewById(R.id.create_dir);

        create_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/13 创建文件夹的操作
                String parent = Nav.getText().toString();
                String filename = getDefaultFilename(create_dir.getText().toString(),parent);
                fileSystem.createDir(parent+File.separator+filename);
                files = fileSystem.getFiles(parent);
                array2List(files,filesList);
                madapter.notifyDataSetChanged();
                newCreate_dialog.dismiss();
            }
        });

        create_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/13 创建文本文档
                String parent = Nav.getText().toString();
                String file_name = getDefaultFilename(create_file.getText().toString(),parent);
                file_name +=".txt";
                fileSystem.createFile(parent+File.separator+file_name);
                files = fileSystem.getFiles(parent);
                array2List(files,filesList);
                madapter.notifyDataSetChanged();
                newCreate_dialog.dismiss();




            }
        });
        newCreate_dialog = builder.create();

    }

    /**
     * 得到默认的文件名
     * @param filetype
     * @param parent
     */
    public String getDefaultFilename(String filetype,String parent){
        String index = "";
        int max = 0;
        File[] mfiles = fileSystem.getFiles(parent);
        for(int i =0;i<mfiles.length;i++){
            String filename = mfiles[i].getName();
            if(filename.contains(filetype))
                if(filename.lastIndexOf(".")!=-1) {
                    index = filename.substring(filename.indexOf(filetype) + filetype.length(), filename.lastIndexOf("."));
                }
                else index = filename.substring(filename.indexOf(filetype) + filetype.length());
                if(index!=null&&!index.equals("")){
                   int index_int = Integer.parseInt(index);
                   max = Math.max(max,index_int);
               }
        }
        max = max+1;
        return filetype+max;
    }

    /**
     * 创建记事本对话框
     */
    void create_notebook_dialog(final File file,String parent){
        View v = getLayoutInflater().inflate(R.layout.notebook,null);
        file_menu = (TextView)v.findViewById(R.id.file_menu);
        file_tetx = (EditText)v.findViewById(R.id.file_text);
        max_note = (ImageView)v.findViewById(R.id.mmax);
        min_note = (ImageView)v.findViewById(R.id.mmin);
        close_note = (ImageView)v.findViewById(R.id.mclose);
        file_path_noteBook = (TextView)v.findViewById(R.id.file_path);

        file_path_noteBook.setText("~"+parent+File.separator+file.getName());

        file_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/14 弹出文件菜单选项
                PopupMenu popmenu = new PopupMenu(Desktop.this,file_menu);
                popmenu.getMenuInflater().inflate(R.menu.file_menu_layout,popmenu.getMenu());
                popmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.save:
                                fileSystem.write_File(file,file_tetx.getText().toString());
                                Toast.makeText(Desktop.this, "文件"+file.getName()+"已保存", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popmenu.show();
            }
        });

        file_tetx.setText(fileSystem.read_File(file));

        max_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialogManager.maxed(noteBook_dialog);
            }
        });

        min_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialogManager.mined(noteBook_dialog);
            }
        });
        /**
         * 关闭按钮
         */
        close_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools_bar.removeView((ImageView)notebookicon.getTag());
                notebookicon.setTag(null);
                mdialogManager.closed(noteBook_dialog);
                noteBook_dialog = null;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        noteBook_dialog = builder.create();

        notebookicon = new ImageView(this);
        notebookicon.setBackgroundResource(R.drawable.notebook);
        createTuoPan(notebookicon);
        /**
         * 托盘图标点击事件
         */
        ((ImageView)notebookicon.getTag()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!noteBook_dialog.isShowing()){
                    noteBook_dialog.show();
                }
            }
        });

    }

    /**
     *将数组转换为list
     * @param mfilesarray
     * @param mfilesList
     */
    public void array2List(File[] mfilesarray,List<File> mfilesList){
        mfilesList.clear();
        for(int i = 0;i<mfilesarray.length;i++){
            mfilesList.add(mfilesarray[i]);
        }
    }

    /**
     * 创建重命名对话框
     */
    public void create_rename_dialog(final File file, final String parent, final TextView file_name_edit){
       View v = getLayoutInflater().inflate(R.layout.rename,null);
       old_name = (TextView)v.findViewById(R.id.old_name);
       new_name = (EditText)v.findViewById(R.id.new_name);
        finish_bn = (TextView)v.findViewById(R.id.finish);
        old_name.setText("文件 "+parent+File.separator+file.getName());
        new_name.setText(file.getName());
        finish_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name_str = new_name.getText().toString();
                fileSystem.RenameFile(file,new_name_str);
                file_name_edit.setText(new_name_str);

                mdialog.dismiss();
                mdialog.cancel();
                mdialog = null;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        mdialog = builder.create();
    }

    /**
     * 任务管理器窗口
     */
    public void create_manageTask(){

        memoryLists = new ArrayList<>();
        memoryLists.clear();
        memeory temp_memory = new memeory();
        temp_memory.setPid("0");
        temp_memory.setSpace(TOTAL_MEMORY);
        temp_memory.setStates(0);
        memoryLists.add(temp_memory);



        memorymanager = new memoryManager(memoryLists,TOTAL_MEMORY);
        memorymanager.setmemoryChangedListener(new memoryManager.memoryChangedListener() {
            @Override
            public void memorychanged() {
                memory_paint();
                memory_decription.setText("Total:100M Availab:"+memorymanager.getAvailable_memory()+"M");
            }
        });
        memorymanager.setAvailable_memory(TOTAL_MEMORY);

        processManage = new ProcessManage(jiuxvList,zuseList);
        processManage.setMjiuxvListener(new ProcessManage.jiuxvListListener() {
            @Override
            public void jiuxvChanged() {
                mprocessadapter_jiuxv.notifyDataSetChanged();
                mprocessadapter_zuse.notifyDataSetChanged();
            }
        });

        View v = getLayoutInflater().inflate(R.layout.managetask,null);
        mk_min = (ImageView)v.findViewById(R.id.min_bn);
        mk_close = (ImageView)v.findViewById(R.id.close_bn);
        add_bn = (ImageView)v.findViewById(R.id.add_process);
        jiuxv_listview = (ListView)v.findViewById(R.id.jiuxv_listView);
        zuse_Listview = (ListView)v.findViewById(R.id.zuse_listView);

        memory = (LinearLayout)v.findViewById(R.id.total_memory);

        memory_paint();

        process_aligo = (Spinner) v.findViewById(R.id.process_choose);
        grab_or_not = (Spinner)v.findViewById(R.id.grab_choose);
        memory_aligo_choose = (Spinner)v.findViewById(R.id.memory_aligo_choose);

        grab_or_not.setEnabled(false);

        current_process_pid = (TextView)v.findViewById(R.id.current_process_pid);

        current_process_finishprocess = (ProgressBar) v.findViewById(R.id.current_process_finish_process);

        current_process_myprocess = (TextView)v.findViewById(R.id.current_process_myprocess);

        memory_decription = (TextView)v.findViewById(R.id.memory_decription);

        /**
         * 点击进度条，阻塞当前运行进程
         */
        current_process_finishprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_exe_process!=null){
                    zuse_FLAG = true;
                    zhongdaun_FLAG = true;

                }
            }
        });

        /**
         *最小化
         */
        mk_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ManageTask.isShowing())
                mdialogManager.mined(ManageTask);
            }
        });
        /**
         * 关闭
         */
        mk_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools_bar.removeView((View)managetaskIcon.getTag());
                managetaskIcon.setTag(null);
                mdialogManager.closed(ManageTask);
                ManageTask = null;
                /**
                 * 将就绪队列和阻塞队列清空
                 */
                jiuxvList.clear();
                zuseList.clear();

                memoryLists.clear();

                if(process_exe_thred!=null)
                {
                    process_exe_thred.interrupt();
                    process_exe_thred = null;
                }


            }
        });

        mprocessadapter_jiuxv = new processAdapter(jiuxvList,this);
        jiuxv_listview.setAdapter(mprocessadapter_jiuxv);

        mprocessadapter_zuse = new zuse_processAdapter(zuseList,this);
        zuse_Listview.setAdapter(mprocessadapter_zuse);

        zuse_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processManage.awake(position);
            }
        });

        /**
         * 增加进程按钮的响应事件
         */
        add_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 得到一个随机进程
                 */
               newAddProcess = process.getRandomProcess();

               int temp_index = 0;
               switch (current_memory_aligo){

                   //首次适应算法
                   case FFFLAG:

                       //非循环首次适应算法会把available_last_index置零
                       available_last_index = 0;
                       temp_index = memorymanager.FF(newAddProcess);
                       break;

                   //循环首次适应算法
                   case NFFLAG:

                       temp_index = memorymanager.NF(newAddProcess,available_last_index);
                       if(temp_index!=Integer.MAX_VALUE)
                       available_last_index = temp_index;

                       break;

                   //最佳适应算法
                   case BFFLAG:

                       available_last_index = 0;
                       temp_index = memorymanager.BF(newAddProcess);
                       break;

                   //最坏适应算法
                   case WFFLAG:

                       available_last_index = 0;
                       temp_index = memorymanager.WF(newAddProcess);
                       break;
                   default:
                       break;
               }
               if(temp_index!=Integer.MAX_VALUE){   //可以创建进程
                   memorymanager.fenpei(newAddProcess,temp_index);
                   /**
                    * 加入就绪队列
                    */
                   processManage.addprocessJiuxv(newAddProcess);
                   /**
                    * 如果是优先权调度算法且当前为抢占方式
                    */
                   if(current_process_aligo_FLAG==PREFLAG&&current_GRABFLAG==GRABFLAG)
                       if(current_exe_process!=null&&newAddProcess.getPriority()<current_exe_process.getPriority())
                           zhongdaun_FLAG = true;
                       else
                           ;    //不执行任何动作
               }
                else {  //内存不足
                   Toast.makeText(Desktop.this, "内存不足，无法创建新的进程", Toast.LENGTH_SHORT).show();
                   newAddProcess = null;
               }
            }
        });

        /**
         * 内存分配算法的选择
         */
        memory_aligo_choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] temp_choose = getResources().getStringArray(R.array.memory_aligo);
                String item_value = temp_choose[position];
                switch (item_value){
                    case "首次适应算法（First Fit）":
                        current_memory_aligo = FFFLAG;
                        break;
                    case "循环首次应算法（Next Fit）":
                        current_memory_aligo = NFFLAG;
                        break;
                    case "最佳适应（Best Fit）":
                        current_memory_aligo = BFFLAG;
                        break;
                    case "最坏适应（Worst Fit）":
                        current_memory_aligo = WFFLAG;
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        process_aligo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 得到供选择的所有值
                 */
                String [] process_aligo = getResources().getStringArray(R.array.process_aligo);
                String item_value = process_aligo[position];
                switch (item_value){
                    case "先来先服务（FCFS）":
                        current_process_aligo_FLAG = FCFSFLAG;
                        grab_or_not.setEnabled(false);
                        break;
                    case "短作业优先（SJF）":
                        current_process_aligo_FLAG = SJFFLAG;
                        grab_or_not.setEnabled(false);
                        break;
                    case "时间片轮转（RR）":
                        current_process_aligo_FLAG = RRFLAG;
                        grab_or_not.setEnabled(false);
                        break;
                    case "优先权调度算法" :
                        current_process_aligo_FLAG = PREFLAG;
                        grab_or_not.setEnabled(true);
                        break;
                    case "高响应比优先":
                        current_process_aligo_FLAG = REFLAG;
                        grab_or_not.setEnabled(false);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        grab_or_not.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 供选择的所有值
                 */
                String [] grab_or_not_str = getResources().getStringArray(R.array.grab_or_not);
                String item_value = grab_or_not_str[position];
                switch (item_value){
                    case "非抢占式":
                        current_GRABFLAG = NOGRABFLAG;
                        break;
                    case "抢占式":
                        current_GRABFLAG = GRABFLAG;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        ManageTask = builder.create();

    }

    /**
     * 模拟进程调度的Runnable
     */
    Runnable  process_choose  = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (!jiuxvList.isEmpty() && cpu_num == 1) {
                    switch (current_process_aligo_FLAG) {
                        case SJFFLAG:
                            current_exe_process = processManage.SJF();
                            break;
                        case RRFLAG:
                            current_exe_process = processManage.FCFS();
                            break;
                        case PREFLAG:
                            current_exe_process = processManage.priority();
                            break;
                        case REFLAG:
                            current_exe_process = processManage.REMethod();
                            break;
                        case FCFSFLAG:
                        default:
                            current_exe_process = processManage.FCFS();
                            break;
                    }
                    cpu_num = 0;  //cpu被占用
                    process_exe_thred=new Thread(process_exe);
                    process_exe_thred.start();
                }
            }
        }
    };

    /**
     * 模拟进程运行的Runnable
     */
    Runnable process_exe = new Runnable() {

      @Override
      public void run() {
          synchronized (this) {
              /**
               * p操作（请求资源）
               */

                  /**
                  * 初始化时间片的使用为0
                  */
                  int temp_time_length = 0;
                  zhongdaun_FLAG = false;
                  boolean flag = false;
                  int temp_total_time = current_exe_process.getTotal_time();
                  int temp_myprocess = current_exe_process.getMyprocess();
                  Message m = process_handler.obtainMessage();
                  m.obj = current_exe_process;
                  m.what = 123;
                  process_handler.sendMessage(m);
              while (temp_myprocess != temp_total_time&&!zhongdaun_FLAG) {

                  try {
                      Thread.sleep(1000);
                      temp_myprocess++;
                      Message m1 = process_handler.obtainMessage();
                      m1.what = 234;
                      m1.obj = temp_myprocess;
                      process_handler.sendMessage(m1);
                      if (temp_myprocess == temp_total_time) {
                          flag = true;
                          break;
                      }
                      else if(current_process_aligo_FLAG==RRFLAG){
                           temp_time_length++;
                          if(temp_time_length==RR_TIME_LENGTH){
                              break;
                          }
                      }

                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }


              /**
               * v操作（释放资源）
               */
              if (flag) {   //进程运行完成
                  try {
                      Thread.sleep(1000);
                      process_handler.sendEmptyMessage(345);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }

              }else {      //被中断

                  //保存进程中断场景
                  current_exe_process.setMyprocess(temp_myprocess);
                  //被阻塞
                  if(zuse_FLAG){
                      process_handler.sendEmptyMessage(890);
                  }else {

                      //时间片用完
                      if(current_process_aligo_FLAG==RR_TIME_LENGTH){
                          process_handler.sendEmptyMessage(567);
                      }
                      //被抢占
                      else
                          process_handler.sendEmptyMessage(456);
                  }


              }


          }
      }
  };

    /**
     * 计算等待时间的线程
     */
  Runnable cal_waitTime = new Runnable() {
      @Override
      public void run() {
          while(true){
              if(!jiuxvList.isEmpty()){
                  try {
                      for(int i=0;i<jiuxvList.size();i++){
                          process temp_process = jiuxvList.get(i);
                          int temp_wait_time = temp_process.getWait_time();
                          /**
                           * 如果不是正在运行的进程
                           */
                          if(!temp_process.getPid().equals(current_exe_process.getPid()))
                          {
                              temp_wait_time++;
                              temp_process.setWait_time(temp_wait_time);
                          }

                      }
                      process_handler.sendEmptyMessage(678);
                      /**
                       * 每秒钟等待时间增加 1
                       */
                      Thread.sleep(1000);
                  }catch (Exception e){
                      e.printStackTrace();
                  }

              }
          }
      }
  };

  void memory_paint(){
      /**
       * 清空画屏
       */
      memory.removeAllViewsInLayout();

      memeory temp_memory;
      int temp_space;
      for(int i = 0;i<memoryLists.size();i++){
              temp_memory = memoryLists.get(i);
              temp_space = temp_memory.getSpace();
              TextView v1 = new TextView(this);
              LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,(float)temp_space);
              llp.gravity = Gravity.CENTER;
          if(!temp_memory.getPid().equals("0"))
                  v1.setText(temp_memory.getPid()+"   "+temp_memory.getSpace()+"m");
          else v1.setText(temp_memory.getSpace()+"m");
              if(temp_memory.getStates()==0)
                  v1.setBackgroundColor(getResources().getColor(R.color.Been_green));
              else v1.setBackgroundColor(getResources().getColor(R.color.yellow));
              v1.setTextColor(getResources().getColor(R.color.colorPrimary));
              v1.setLayoutParams(llp);
              v1.setTextSize(5);
              v1.setGravity(View.TEXT_ALIGNMENT_CENTER);
          memory.addView(v1);
      }
  }

    /**
     * 创建远程桌面配置对话框
     */
  public void createRemoteSetDialog(){
      View view = getLayoutInflater().inflate(R.layout.remoteset,null);
      remoteIp = (EditText)view.findViewById(R.id.remoteIp);
      remoteOkbutton = (Button)view.findViewById(R.id.remoteOkButton);
      linkanim = (FloatingActionButton)view.findViewById(R.id.linkanim);
      remote_desktop_set_first = (LinearLayout)view.findViewById(R.id.first);

      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setView(view);

      rotateAnim = ObjectAnimator.ofFloat(linkanim,"rotation",0,360);
      rotateAnim.setDuration(1000);
      rotateAnim.setRepeatCount(-1);  //无限重复

      /**
       * 确定按钮响应事件
       */
      remoteOkbutton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String remoteIpStr = remoteIp.getText().toString();
              if(InetAddressManager.isvaild(remoteIpStr))
              {
                  InetAddressManager.ServerIp = remoteIpStr;
                  Log.e("inetaddress",InetAddressManager.ServerIp);
                  new Thread(requestRemoteRunnable).start();   //发送远程连接请求
                  remote_desktop_set_first.setVisibility(View.GONE);
                  linkanim.setVisibility(View.VISIBLE);
                  rotateAnim.start();
              }
              else
                  Toast.makeText(Desktop.this, "Ipv4地址不合法", Toast.LENGTH_SHORT).show();
          }
      });

      remoteSetDialog = builder.create();


  }

    /**
     * 创建远程桌面对话框
     */
  public void createRemoteDesktopDialog(){
        View view = getLayoutInflater().inflate(R.layout.remotedesktop,null);
        remote_min = (ImageView)view.findViewById(R.id.min_bn);
        remote_max = (ImageView)view.findViewById(R.id.max_bn);
        remote_close = (ImageView)view.findViewById(R.id.close_bn);
        remote_desktop_show = (ImageView)view.findViewById(R.id.remote_desktop_show);
        remoteIpTextview = (TextView)view.findViewById(R.id.remoteIp);
        remoteIpTextview.setText(InetAddressManager.ServerIp);

        remote_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialogManager.mined(remote_desktop);
            }
        });

        remote_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialogManager.maxed(remote_desktop);
            }
        });

        remote_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools_bar.removeView((ImageView)remoteDeskTopIcon.getTag());
                remoteDeskTopIcon.setTag(null);
                mdialogManager.closed(remote_desktop);
                remote_desktop = null;

                /**
                 * 告诉远程机器断开远程桌面
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(socketRequest==null||socketRequest.isClosed()){

                            try {
                                socketRequest = new Socket(InetAddressManager.ServerIp,6666);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        DataOutputStream dos = null;
                        try {
                            dos = new DataOutputStream(socketRequest.getOutputStream());
                            String message = "#CLOSE\n";
                            dos.write(message.getBytes(),0,message.getBytes().length);
                            dos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }

        });

      ((ImageView)remoteDeskTopIcon.getTag()).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(!remote_desktop.isShowing())
                  remote_desktop.show();
          }
      });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        remote_desktop = builder.create();

      // TODO: 2018/6/6 开启等待画面传输的线程

      if(receiveThread==null){
          receiveThread = new Thread(receiveRunnable);
          receiveRunnableCanRun = true;
          receiveThread.start();
      }


    }

    /**
     * 请求远程桌面的线程
     */
  Runnable requestRemoteRunnable = new Runnable() {
      String result = "";
      @Override
      public void run() {
          try {
              socketRequest = new Socket(InetAddressManager.ServerIp,6666);
              String message = "#IP="+InetAddressManager.getLocalIp()+"\n";
              socketRequest.setSoTimeout(15*1000);  //设置超时15秒
              DataOutputStream dos = new DataOutputStream(socketRequest.getOutputStream());
              dos.write(message.getBytes(),0,message.getBytes().length);
              dos.flush();

              BufferedReader br = new BufferedReader(new InputStreamReader(socketRequest.getInputStream()));


              String inputline = br.readLine();
              result = new String(inputline);
              Message m = mhandler.obtainMessage();
              m.obj = result;
              m.what = 123;
              mhandler.sendMessage(m);

          } catch (IOException e) {
              e.printStackTrace();
              Log.e("time",e.getMessage());
              mhandler.sendEmptyMessage(678);
          }
      }
  };



    /**
     * 接受远程桌面的请求的线程
     */
  Runnable responseRunnable = new Runnable() {
        String result = "";
      @Override
      public void run() {
          try {
              serverSocketResponse = new ServerSocket(6666);
          } catch (IOException e) {
              e.printStackTrace();
          }
          while(true){
              try {

                  socketRequest = serverSocketResponse.accept();
                  String inputline = "";
                  BufferedReader br = new BufferedReader(new InputStreamReader(socketRequest.getInputStream()));
                  inputline = br.readLine();
                  Log.e("inputline",inputline);
                  result = new String(inputline);

                  if(result.startsWith("#IP"))
                  {
                      InetAddressManager.ServerIp = result.split("=")[1];
                      Log.e("ServerIp",InetAddressManager.ServerIp);
                      mhandler.sendEmptyMessage(345);
                  }else if(result.startsWith("#CLOSE")){
                      if(mCaptureThread!=null){
                          capturethread.captureCanRun = false;
                          mCaptureThread.interrupt();
                          mCaptureThread = null;
                      }
                      if(msendThread!=null){
                          if(socket!=null)
                              socket = null;
                          if(sendthread!=null)
                              sendthread.sendCanRun = false;
                          msendThread.interrupt();
                          msendThread = null;
                      }
                  }

              } catch (IOException e) {
                  e.printStackTrace();
                  Log.e("error1",e.getMessage());
              }
          }

      }
  };

    /**
     * 接收远程画面的线程
     */
    Runnable receiveRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(6667);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(receiveRunnableCanRun){
                try {
                    socket = serverSocket.accept();

                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    reveiveBitmap = BitmapFactory.decodeStream(dis);
                    mhandler.sendEmptyMessage(234);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("receive_error","接受发生错误");
                }

            }

        }
    };



    /**
     * 选择请求回应的对话框
     */
  public void createChooseForRequestDialog(){
      View view = getLayoutInflater().inflate(R.layout.chooseforremote,null);
      requestText = (TextView)view.findViewById(R.id.requesttext);
      requestOkBn = (Button)view.findViewById(R.id.requestOkBn);
      requestNoBn = (Button)view.findViewById(R.id.requestNoBn);

      requestText.setText(InetAddressManager.ServerIp+"请求远程连接您的计算机，是否允许？");
      requestOkBn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try {
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          DataOutputStream dos = null;
                          try {
                              dos = new DataOutputStream(socketRequest.getOutputStream());
                              String message = "OK\n";
                              dos.write(message.getBytes(),0,message.getBytes().length);
                              dos.flush();
                              dos.close();
                          } catch (Exception e) {
                              e.printStackTrace();
                          }

                      }
                  }).start();

                  if(mCaptureThread==null)
                  {
                      capturethread = new CaptureThread();
                      mCaptureThread = new Thread(capturethread);
                  }

                  capturethread.captureCanRun = true;
                  mCaptureThread.start();

                  if(msendThread==null)
                  {
                      sendthread = new sendThread();
                      msendThread = new Thread(sendthread);
                  }
                  sendthread.sendCanRun = true;
                  msendThread.start();


              } catch (Exception e) {
                  e.printStackTrace();
                  Log.e("error",e.getMessage()+"");
              }finally {
                  chooseForRequestDialog.dismiss();
              }

          }
      });
      requestNoBn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              new Thread(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          DataOutputStream dos = new DataOutputStream(socketRequest.getOutputStream());
                          String message = "NO\n";
                          dos.write(message.getBytes(),0,message.getBytes().length);
                          dos.flush();
                          dos.close();
                          chooseForRequestDialog.dismiss();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }
              }).start();


          }
      });

      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setView(view);
      chooseForRequestDialog = builder.create();
      chooseForRequestDialog.setCancelable(false);

  }

    /**
     * 截图线程
     */
  class CaptureThread implements Runnable{
      volatile boolean captureCanRun = true;
      @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
      @Override
      public void run() {
          while (captureCanRun){
              try {
                  Thread.sleep(1000);
                  mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
                  startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),REQUEST_PROJECTION);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }

              }
      }
  }

    /**
     * 发送线程
     */
  class sendThread implements Runnable{
      volatile  boolean sendCanRun = true;
      DataOutputStream dos;
      @Override
      public void run() {
          while(sendCanRun){
              if(sendBitmap!=null){
                  byte[] out = bitmap2ByteArray(sendBitmap);
                  try {
                      Thread.sleep(1000);
                      socket = new Socket(InetAddressManager.ServerIp, 6667); //6667端口传输桌面画面
                      dos = new DataOutputStream(socket.getOutputStream());
                      dos.write(out, 0, out.length);
                      socket.close();
                      dos.close();

                  } catch (Exception e) {
                      e.printStackTrace();
                      Log.e("error", e.getMessage());
                      if(mCaptureThread!=null){
                          capturethread.captureCanRun = false;
                          mCaptureThread.interrupt();
                          mCaptureThread = null;
                          Log.e("error","截图线程关闭了");
                      }
                      if(msendThread!=null){
                          if(socket!=null)
                              socket = null;
                          if(sendthread!=null)
                              sendthread.sendCanRun = false;
                          msendThread.interrupt();
                          msendThread = null;
                      }
                  }
              }
          }
      }
  }

    /**
     * bitmap转byte数组
     * @param bitmap
     * @return
     */
  public byte[] bitmap2ByteArray(Bitmap bitmap){
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG,90,bos);
      return bos.toByteArray();
  }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROJECTION ) {
            try {
                final int mWidth = getWindowManager().getDefaultDisplay().getWidth();
                final int mHeight = getWindowManager().getDefaultDisplay().getHeight();
                final ImageReader mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int mScreenDensity = metrics.densityDpi;
                final MediaProjection mProjection = mediaProjectionManager.getMediaProjection(-1, data);
                final VirtualDisplay virtualDisplay = mProjection.createVirtualDisplay("screen-mirror",
                        mWidth, mHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, null);
                mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {

                        try {
                            /**
                             * 去掉--->mProjection.stop(); 会出现不停的截图现象
                             */

                            mProjection.stop();
                            Image image = mImageReader.acquireLatestImage();
                            final Image.Plane[] planes = image.getPlanes();
                            final ByteBuffer buffer = planes[0].getBuffer();
                            int pixelStride = planes[0].getPixelStride();
                            int rowStride = planes[0].getRowStride();
                            int rowPadding = rowStride - pixelStride * mWidth;
                            sendBitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                            sendBitmap.copyPixelsFromBuffer(buffer);
                            image.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
