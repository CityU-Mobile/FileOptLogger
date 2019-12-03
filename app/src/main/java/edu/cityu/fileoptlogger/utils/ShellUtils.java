package edu.cityu.fileoptlogger.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.cityu.fileoptlogger.info.Global;

/**
 * @className ShellContent
 * @description
 * @author 潘日维
 * @version V1.0
 * @date 2017/6/13
 */
public class ShellUtils {

    private String command;

    public ShellUtils(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean execute(boolean isRoot){
        Log.i(Global.TAG, "执行 --> " + this.command);
        if(isRoot){
            return rootCommand(command);
        }else{
            return normalCommand(command);
        }
    }

    public String executeAndReturn(boolean isRoot){
        Log.i(Global.TAG, "执行 --> " + this.command);
        if(isRoot){
            return rootCommandReturn(command);
        }else{
            return normalCommandReturn(command);
        }
    }

    public static boolean chainedExecute(boolean isRoot, ShellUtils... commands){
        for(ShellUtils cmd : commands){
            if(!cmd.execute(isRoot)){
                Log.i(Global.TAG, "cmd error! , cmd = " + cmd.getCommand());
                return false;
            }
        }
        return true;
    }

    private static boolean normalCommand(String command){//用root的权限执行命令行
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            new ReadInputSteamThread("sin",p.getInputStream()).start();
            new ReadInputSteamThread("ern",p.getErrorStream()).start();
            p.waitFor();
            return true;
        } catch (Exception e) {
            Log.i(Global.TAG, e.getMessage());
            return false;
        } finally {
            if(p != null){
                p.destroy();
            }
        }
    }

    private static String normalCommandReturn(String command){//用root的权限执行命令行
        String ret = "";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            new ReadInputSteamThread("error",p.getErrorStream()).start();
            ret = convertStreamToString(p.getInputStream());
            p.waitFor();

            //new ReadInputSteamThread("sin",p.getInputStream()).start();

        } catch (Exception e) {
            Log.i(Global.TAG, e.getMessage());
        } finally {
            if(p != null){
                p.destroy();
            }
        }
        return ret;
    }

    private static boolean rootCommand(String command){//用root的权限执行命令行
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            new ReadInputSteamThread("sin",process.getInputStream()).start();
            new ReadInputSteamThread("ern",process.getErrorStream()).start();
            process.waitFor();
        } catch (Exception e) {
            Log.i(Global.TAG, e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                Log.i(Global.TAG, e.getMessage());
            }
        }
        return true;
    }

    private static String rootCommandReturn(String command){//用root的权限执行命令行，然后获取返回结果
        Process process = null;
        DataOutputStream os = null;
        String ret = "";
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            InputStream in = process.getInputStream();
            ret = convertStreamToString(in);
        } catch (Exception e) {
            Log.i(Global.TAG, e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                Log.i(Global.TAG, e.getMessage());
            }
        }
        return ret;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();



        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "/n");

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }



        return sb.toString();

    }

    static class ReadInputSteamThread extends Thread {

        String name;
        InputStream inputStream;

        public ReadInputSteamThread(String name, InputStream inputStream) {
            this.name = name;
            this.inputStream = inputStream;
        }

        @Override
        public void run() {

            Log.i(Global.TAG, "启动异步读线程");
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));
            String line = "";
            try {

                while ((line = reader.readLine()) != null){
                    Log.i(Global.TAG, "name : + " + name + " line = " + line);
                }

            }catch (Exception e){

                Log.i(Global.TAG, e.getMessage());

            }finally {

                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
