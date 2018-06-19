package com.example.hasee.virtualwinos.System_Control;

import android.content.Context;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static android.os.Environment.getDataDirectory;

/**
 * Created by hasee on 2018/5/9.
 */

public class FileSystem {
    static Context mcontext;
    static  String root;
    static File[] files;

    public FileSystem(Context context){
        mcontext = context;
        root = mcontext.getFilesDir().toString();
    }


    /**
     * 创建文件
     * @param filename
     */
    public static void createFile(String filename){

        File file = new File(root,filename);
        if(!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    /**
     * 创建文件夹
     * @param filename
     */
    public static void createDir(String filename){
        File file = new File(root,filename);
        if(!file.mkdirs())
          Log.e("error",file.getAbsolutePath()+"没有被创建");
        Log.e("dir",file.getAbsolutePath());
    }

    /**
     * 删除文件
     * @param filename
     */
    public static void deleteFile(String filename){

        File file = new File(root+File.separator+filename);
        if(file.exists())
        file.delete();
    }

    /**
     * 删除文件夹
     * @param dirpath
     */
    public static void deleteDirectory(String dirpath){
        Log.i("dir_path",dirpath);
         File file = new File(dirpath);
        if(!file.isDirectory()&&file.exists())
            file.delete();
        else{
            File[] files = file.listFiles();
            for(int i = 0;i<files.length;i++)
            {
                deleteDirectory(files[i].getAbsolutePath());
            }
            if(file.exists())
                file.delete();
        }
    }

    /**
     * 显示文件夹下的所有文件以及文件夹
     * @param dirpath
     */
    public static File[] getFiles(String dirpath){
        String dataDirectory = Environment.getDataDirectory().getName();
        Log.e("datadir",dataDirectory);
        File file = new File(root+File.separator+dirpath+File.separator);
        Log.e("filename",file.getName());
        if(file.isDirectory()) {
            try {
                if(files==null)
                files = file.listFiles();
                else
                    files=Arrays.copyOf(file.listFiles(),file.listFiles().length);
                Log.e("files.length",files.length+"");
                return files;
            }catch (Exception e){
                Log.e("file_error",e.getMessage());
                return null;
            }
        }else {

            files = new File[1];
            files[0] = file;
            return files;
        }
    }

    /**
     * 重命名文件
     * @param file
     * @param new_name
     */
    public void RenameFile(File file,String new_name){
        String parent = file.getParent();
        new_name = parent+File.separator+new_name;
        file.renameTo(new File(new_name));
    }

    /**
     * 得到文件类型
     * @param file
     * @return
     */
    public static String getfiletype(File file){
        String type = "";
        String filename = "";
        if(file.isDirectory())
            return "dir";
        else {
            filename = file.getName();
            type = filename.substring(filename.lastIndexOf(".")+1);
            return type;
        }
    }

    /**
     * 向txt文件中写内容
     * @param file
     * @param text
     */
    public void write_File(File file,String text){

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("writwFileError",e.getMessage());
        }
    }

    /**
     * 从txt文件中读内容
     * @param file
     * @return
     */
    public String read_File(File file){
        BufferedReader breader ;
        StringBuffer text = new StringBuffer();
        try{
            breader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String inputLine = "";
            while ((inputLine=breader.readLine())!=null){
                text.append(inputLine);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return text.toString();
    }

    /**
     * 得到文件大小，单位是b
     * @param file
     * @return
     */
    public long get_size_of(File file){
        return file.length();
    }

    public long get_size_of(String filename){
        File file = new File(root,filename);
        return file.length();
    }

    /**
     * 得到合适单位的文件大小
     * @param file
     * @return
     */
    public String get_fit_size_of(File file){
        long length = file.length();
        if(length<1024)
            return length+"b";
        else
            return (long)(length/1024.0)+"kb";
    }

    public String get_fit_size_of(String filename){
        File file = new File(root,filename);
        long length = file.length();
        if(length<1024)
            return length+"b";
        else
            return (long)(length/1024.0)+"kb";
    }

    public long get_size_of_dir(File file){
        long length = 0;
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i = 0;i<files.length;i++){
                if(files[i].isFile())
                    length += files[i].length();
                else
                    length += get_size_of_dir(files[i]);
            }
        }
        return length;
    }

    /**
     * 得到c盘已经使用的大小
     * @return
     */
    public long get_used_size_of_C_pan(){
        File file = new File(root+File.separator+"C"+File.separator);
        long length = get_size_of_dir(file);
        return length;
    }

    /**
     * 得到c盘已经使用的大小，单位合适的
     * @return
     */
    public String get_fit_used_size_of_C_pan(){
        File file = new File(root+File.separator+"C"+File.separator);
        long length = get_size_of_dir(file);
        if(length<1024)
            return length+"b";
        else
            return (long)(length/1024.0)+"kb";
    }
}
