package com.example.hasee.virtualwinos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by hasee on 2018/5/8.
 */

public class openHelper extends SQLiteOpenHelper {
    String mname; //数据库表名字
    int   mversion = 1;

    public openHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {


        super(context, name, factory, version);
        mname = name;
        mversion = version;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL("create table if not exists "+
                 mname+" (account varchar(20) primary key," +
                 "nickname varchar(20), " +
                 "password varchar(20), " +
                         "avatar int" +
                 ")"
         );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 2018/5/8  更新

    }
}
