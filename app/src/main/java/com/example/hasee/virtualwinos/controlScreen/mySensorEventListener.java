package com.example.hasee.virtualwinos.controlScreen;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

/**
 * Created by hasee on 2018/5/6.
 */

public class mySensorEventListener implements SensorEventListener {
    /**
     * 传递方向句柄
     */
    Handler mhandler;
    public mySensorEventListener(Handler handler){
        mhandler = handler;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /**
         * 根据x判断屏幕方向
         */
        float x = sensorEvent.values[SensorManager.DATA_X];
        Message m = mhandler.obtainMessage();
        m.what = 666;
        Morieation morieation = new Morieation();
        /**
         * 发送(手机x方向的加速度)morieation对象
         */
        morieation.setX(x);
        m.obj = morieation;
        mhandler.sendMessage(m);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
