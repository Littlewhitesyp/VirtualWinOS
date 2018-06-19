package com.example.hasee.virtualwinos.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hasee.virtualwinos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2018/5/8.
 */

public class usersDao implements myDao {

    SQLiteDatabase mdb;
    final static String tablename = "users";
    final  static String Filed1 = "account";
    final  static String Filed2 = "nickname";
    final  static String Filed3 = "password";
    final static String Filed4 = "avatar";

    public usersDao(SQLiteOpenHelper helper){
        mdb = helper.getReadableDatabase();
    }

    /**
     * 增加用户
     * @param object
     * @return
     */
    @Override
    public boolean insert(Object object) {

        User user = (User)object;
        User user1 = query(user.getAccount());
        if(user1==null) {
            ContentValues values = new ContentValues();
            values.put(Filed1, user.getAccount());
            values.put(Filed2, user.getNickname());
            values.put(Filed3, user.getPassword());
            values.put(Filed4, R.drawable.defaultavatar);  //默认头像
            long result = mdb.insert(tablename, null, values);
            if (result == -1)
                return false;
            else return true;
        }else return false;  //该用户已存在
    }

    /**
     * 查找所有用户
     * @return
     */
    public List<Object> query() {
        List<Object> userLists = new ArrayList<>();
        Cursor mcursor = mdb.query(tablename,null,null,null,null,null,null);
        while (mcursor.moveToNext()){
            User user = new User();
            user.setAccount(mcursor.getString(0));
            user.setNickname(mcursor.getString(1));
            user.setPassword(mcursor.getString(2));
            user.setAvatar(mcursor.getInt(3));
            userLists.add(user);
        }
        return userLists;
    }

    /**
     * 删除一个用户
     * @param object
     * @return
     */
    @Override
    public boolean delete(Object object) {
        User user = (User)object;
        String[] args = new String[1];
        args[0] = user.getAccount();
        long result = mdb.delete(tablename,Filed1+"=?",args);
        if(result==-1)
        return false;
        else return true;
    }

    /**
     * 修改用户信息
     * @param object
     * @return
     */
    @Override
    public boolean mmodify(Object object,String account) {
        User user = (User)object;
        ContentValues values = new ContentValues();
        values.put(Filed1,user.getAccount());
        values.put(Filed2,user.getNickname());
        values.put(Filed3,user.getPassword());
        values.put(Filed4,user.getAvatar());
        String[] args = new String[1];
        args[0] = account;
        long result = mdb.update(tablename,values,Filed1+"=?",args);
        if(result==-1)
        return false;
        else return true;
    }

    /**
     * 根据账号查询一个用户的信息
     * @param account
     * @return
     */
    public User query(String account){
        User user = new User();
        String [] args = new String[1];
        args[0] = account;
        Cursor mcursor = mdb.query(tablename,null,Filed1+"=?",args,null,null,null);
        if(mcursor.moveToNext())
        {
            user.setAccount(mcursor.getString(0));
            user.setNickname(mcursor.getString(1));
            user.setPassword(mcursor.getString(2));
            user.setAvatar(mcursor.getInt(3));
            return user;
        }
        return null;

    }

    /**
     * 判断登陆是否成功
     * @param account
     * @param password
     * @return
     */
    public boolean login(String account,String password){
        String[] args = new String[2];
        args[0] = account;
        args[1] = password;
        Cursor cursor = mdb.query(tablename,null,Filed1+"=? and "+Filed3+"=?",args,null,null,null);
        if(cursor.moveToNext()) return true;
        else return  false;
    }

}
