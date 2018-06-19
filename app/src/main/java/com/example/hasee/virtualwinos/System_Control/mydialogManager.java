package com.example.hasee.virtualwinos.System_Control;

import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by hasee on 2018/5/14.
 */

public class mydialogManager {
   int mscreen_width,   //手机屏幕的宽度
         mscreen_height;  //手机屏幕的高度
   public mydialogManager(int screen_width,int screen_height){
       mscreen_width = screen_width;
       mscreen_height = screen_height;
   }

    /**
     * 最小化
     * @param dialog
     */
   public void mined(Dialog dialog){
       dialog.dismiss();
   }

    /**
     * 最大化
     * @param dialog
     */
   public void maxed(Dialog dialog){
       Window window = dialog.getWindow();
       WindowManager.LayoutParams lp = window.getAttributes();

       if(lp.width==mscreen_width/4*3)
       {
           lp.width = mscreen_width;
           lp.height = mscreen_height;
       }
       else{
           lp.width = mscreen_width/4*3;
           lp.height = mscreen_height/4*3;
       }
       window.setAttributes(lp);
   }

    /**
     * 关闭
     * @param dialog
     */
   public void closed(Dialog dialog){
       dialog.dismiss();
       dialog.cancel();
   }


}
