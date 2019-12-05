package edu.cityu.fileoptlogger.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.cityu.fileoptlogger.info.ColdTraceInfo;
import edu.cityu.fileoptlogger.info.Global;
import edu.cityu.fileoptlogger.info.HotTraceInfo;

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
        Log.i(Global.TAG, "file-compressing service doing in background");
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
            if(tracefile.getName().endsWith(".zip")) {
                continue;
            }
            if(isRunning) {
                try {
                    boolean ret = ZipUtils.zipFile(tracefile,
                            new File(tracefile.getAbsoluteFile() + ".zip"));
                    if(ret) {
                        tracefile.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Log.i(Global.TAG, "file-compressing Servic is stopped");
                break;
            }
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
