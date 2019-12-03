package edu.cityu.fileoptlogger.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.cityu.fileoptlogger.info.ColdTraceInfo;
import edu.cityu.fileoptlogger.info.Global;
import edu.cityu.fileoptlogger.info.HotTraceInfo;
import edu.cityu.fileoptlogger.info.InotifyInfo;

public class MoveDataThread extends AsyncTask<String,Integer,Integer> {

    private boolean isRunning;
    private Context context;

    private DatabaseHelper helper;
    private DaoOperation<HotTraceInfo> operation;
    private DaoOperation<ColdTraceInfo> cold_operation;

    public MoveDataThread(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        helper = DatabaseHelper.getInstance(context);
        operation = new DaoOperation(helper.getCustomDao(HotTraceInfo.class));
        cold_operation = new DaoOperation(helper.getCustomDao(ColdTraceInfo.class));
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd-HH");
        Log.i(Global.TAG, "data-moving service doing in background");
        isRunning = true;
        File dir = new File(Global.LOG_FILE_LOCATION);
        if(dir.exists()) {
            Log.i(Global.TAG, Global.LOG_FILE_LOCATION + " exists");
        }
        File[] filelist = dir.listFiles();
        int filesize = filelist.length;
        int cnt = 0;
        for(File tracefile : filelist) {
            Log.i(Global.TAG, "File Trace Name: " + tracefile);
            String curFname = "fb.log-" + SDF.format(new Date());
            if(tracefile.getName().contains(curFname)) {
                continue;
            }
            if(isRunning) {
                BufferedReader reader;
                String line = "";
                try {
                    reader = new BufferedReader(new FileReader(tracefile));
                    while ((line = reader.readLine()) != null){
                        String[] split = line.split(";");
                        if(split.length != 5) {
                            continue;
                        }
                        String time = split[0];
                        String path = split[1] + "" + split[2];
                        int opt = InotifyInfo.getOptCode(split[3]);
                        long size = Long.valueOf(split[4]);
                        HotTraceInfo hInfo;
                        if(opt != -1) {
                            hInfo = operation.query(path);
                            if(hInfo != null) {
                                if(opt == InotifyInfo.CREATE) {
                                    hInfo.setCtime(time);
                                    operation.update(hInfo);
                                } else if(opt == InotifyInfo.ACCESS || opt == InotifyInfo.MODIFY) {
                                    hInfo.setAtime(time);
                                    hInfo.setFsize(size);
                                    operation.update(hInfo);
                                } else if(opt == InotifyInfo.DELETE) {
                                    hInfo.setDtime(time);
                                    cold_operation.insert(ColdTraceInfo.getColdTraceInfo(hInfo));
                                    operation.delete(hInfo);
                                }
                            } else {
                                hInfo = new HotTraceInfo();
                                hInfo.setPath(path);
                                if(opt == InotifyInfo.CREATE) {
                                    hInfo.setCtime(time);
                                } else if(opt == InotifyInfo.ACCESS || opt == InotifyInfo.MODIFY) {
                                    hInfo.setAtime(time);
                                    hInfo.setFsize(size);
                                } else if(opt == InotifyInfo.DELETE) {
                                    hInfo.setDtime(time);
                                } else {
                                    continue;
                                }
                                boolean rret = operation.insert(hInfo);
                                Log.i(Global.TAG, "insert info: " +rret);
                            }

                            Log.i(Global.TAG, "info= " + time + " " + path + " " + opt + " " + size);
                        }
                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Log.i(Global.TAG, "Data-moving Servic is stopped");
                break;
            }

            tracefile.delete();
        }
        isRunning = false;
        return null;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }

}
