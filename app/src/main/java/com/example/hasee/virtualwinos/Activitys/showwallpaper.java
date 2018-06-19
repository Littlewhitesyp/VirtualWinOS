package com.example.hasee.virtualwinos.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hasee.virtualwinos.R;

public class showwallpaper extends AppCompatActivity {
    ImageView wallpaper;
    LinearLayout showpic_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_showwallpaper);
        init();
    }

    /**
     * 初始化
     */
    void init(){
        showpic_layout = (LinearLayout)findViewById(R.id.showpic_layout);
        wallpaper = (ImageView)findViewById(R.id.wallpaper);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("wallpaperId");
        wallpaper.setImageResource(id);
        showpic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
