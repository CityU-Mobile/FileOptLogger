package edu.cityu.fileoptlogger.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.cityu.fileoptlogger.info.Global;

public class FileObserverThread extends AsyncTask<String,Integer,Integer> {

    @Override
    protected Integer doInBackground(String... strings) {
        Log.i(Global.TAG, " run async thread to collec data");
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd-HH");
        new ShellUtils(
                "/data/inotifytools/inotifywait -mrq " +
                        "--timefmt '%y-%m-%d %H:%M:%S' " +
                        "--format '%T;%w;%f;%e;%s' " +
                        "-e modify,delete,create,access " +
                        "/data/data/com.facebook.katana/cache /data/data/com.facebook.katana/files >> " +
                        Global.LOG_FILE_LOCATION + "fb.log-" + SDF.format(new Date())  + " 2>/dev/null").execute(true);
        return null;
    }

}
