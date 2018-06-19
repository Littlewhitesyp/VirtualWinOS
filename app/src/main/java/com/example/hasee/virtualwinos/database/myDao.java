package com.example.hasee.virtualwinos.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by hasee on 2018/5/8.
 */

public interface myDao {

    boolean insert(Object object);   //增
    List<Object> query();                     //查
    boolean delete(Object object); //删
    boolean mmodify(Object object,String account);


}
