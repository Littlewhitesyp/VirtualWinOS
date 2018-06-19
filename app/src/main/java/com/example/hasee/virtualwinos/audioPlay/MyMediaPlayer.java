package com.example.hasee.virtualwinos.audioPlay;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

import static android.R.attr.id;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by hasee on 2018/5/7.
 */

public class MyMediaPlayer {
    public static MediaPlayer getMediaplay(Context context,int id){
        MediaPlayer mediaPlayer = MediaPlayer.create(context,id);
        try {
            if(mediaPlayer!=null)mediaPlayer.stop();
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }
}
