package edu.cityu.fileoptlogger.utils;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.cityu.fileoptlogger.info.Global;

public class InotifyInstallationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new ShellUtils("mkdir " + Global.MAIN_LOCATION).execute(true);
        new ShellUtils("mkdir " + Global.LOG_FILE_LOCATION).execute(true);

        String str = new ShellUtils("ls " + Global.INOTIFYWAIT_MAIN_LOCATION).executeAndReturn(true);
        if(str.replace("/n","").equals(Global.INOTIFYWAIT_MAIN_LOCATION)){
            Log.i(Global.TAG, "inotifywait existed");
            new ShellUtils("chmod 775 " + Global.INOTIFYWAIT_MAIN_LOCATION).execute(true);
        }else{
            Log.i(Global.TAG, "inotifywait not existed");
            AssetManager assets = getAssets();
            try {
                installFromAssets(assets.open("inotifywait"), Global.INOTIFYWAIT_LOCATION);
                new ShellUtils("chmod 775 " + Global.INOTIFYWAIT_LOCATION).execute(true);
                String ret = new ShellUtils("ls " + Global.INOTIFYWAIT_LOCATION).executeAndReturn(true);
                new ShellUtils("mv " + Global.INOTIFYWAIT_LOCATION + " " + Global.MAIN_LOCATION).execute(true);
                Log.i(Global.TAG, "inotifywait is installed successfully, path = " + ret);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        str = new ShellUtils("ls " + Global.FUSER_MAIN_LOCATION).executeAndReturn(true);
        if(str.replace("/n","").equals(Global.FUSER_MAIN_LOCATION)){
            Log.i(Global.TAG, "fuser existed");
            new ShellUtils("chmod 775 " + Global.FUSER_MAIN_LOCATION).execute(true);
        }else{
            Log.i(Global.TAG, "fuser not existed");
            AssetManager assets = getAssets();
            try {
                installFromAssets(assets.open("fuser"), Global.FUSER_LOCATION);
                new ShellUtils("chmod 775 " + Global.FUSER_LOCATION).execute(true);
                String ret = new ShellUtils("ls " + Global.FUSER_LOCATION).executeAndReturn(true);
                new ShellUtils("mv " + Global.FUSER_LOCATION + " " + Global.MAIN_LOCATION).execute(true);
                Log.i(Global.TAG, "fuser is installed successfully, path = " + ret);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void installFromAssets(InputStream inputStream, String path){
        ByteArrayOutputStream bos = null;
        byte[] data = null;
        try {

            bos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
                bos.write(buff, 0, rc);
            }
            data = bos.toByteArray(); //in_b为转换之后的结果

            writeFile(data, path);

        }catch (IOException e){

            Log.e(Global.TAG, e.getMessage());

        }finally {
            try {
                inputStream.close();
                bos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //将字符串 写入到文件当中
    public String writeFile(byte[] buf, String fileName){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String path = null;
        try
        {
            path = fileName;
            file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e){
            return null;
        } finally{
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }

}
