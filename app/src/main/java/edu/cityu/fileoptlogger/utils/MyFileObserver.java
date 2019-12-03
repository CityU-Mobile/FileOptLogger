package edu.cityu.fileoptlogger.utils;

import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.cityu.fileoptlogger.info.Global;
import edu.cityu.fileoptlogger.info.InotifyInfo;
import edu.cityu.fileoptlogger.jni.JNITools;


public class MyFileObserver extends FileObserver{

    private static final int READ = 0;
    private static final int WRITE = 1;
    private static final int CREATE = 2;
    private static final int DELETE = 3;

    public String parentDir;

    private static final int NO_OPT_INT = -10023;
    private static final String NO_OPT_STR = "-10023";

    private ArrayList<InotifyInfo> infoQueue;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MyFileObserver(String path, ArrayList<InotifyInfo> infoQueue) {
        super(path,
                FileObserver.CREATE |
                        FileObserver.ACCESS |
                        FileObserver.MODIFY |
                        FileObserver.DELETE
        );
        this.parentDir = path;
        this.infoQueue = infoQueue;
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        String now = SDF.format(new Date());

        if(path == null || path.equals("")) {
            return;
        }

        path = this.parentDir + "/" + path;

        if(event == FileObserver.CREATE) { // when file is being creating, file ino maybe invalid, so give a init ino for every file.
            long ino = JNITools.getIno(path);
            if(ino == -1)
                ino = NO_OPT_INT;
            //this.infoQueue.add(new InotifyInfo(path, ino, now, NO_OPT_STR, NO_OPT_STR, CREATE, 0));
            Log.i(Global.TAG,  "[" + path + "] " + "MY CREATE: " + ino);
        } else if (event == FileObserver.ACCESS) {
            File file = new File(path);
            long ino = JNITools.getIno(path);
            if(ino == -1)
                ino = NO_OPT_INT;
            //this.infoQueue.add(new InotifyInfo(path, ino, NO_OPT_STR, now, NO_OPT_STR, READ, file.length()));
            Log.i(Global.TAG, "[" + path + "] " + "MY ACCESS: " + ino + "(" + file.length() + ")");
        } else if(event == FileObserver.MODIFY) {
            File file = new File(path);
            long ino = JNITools.getIno(path);
            if(ino == -1)
                ino = NO_OPT_INT;
            Log.i(Global.TAG, "[" + path + "] " + "MY MODIFY: " + ino + "(" + file.length() + ")");
            //this.infoQueue.add(new InotifyInfo(path, ino, NO_OPT_STR, now, NO_OPT_STR, WRITE, file.length()));
        } else if(event == FileObserver.DELETE) {
            long ino = JNITools.getIno(path);
            if(ino == -1)
                ino = NO_OPT_INT;
            Log.i(Global.TAG, "[" + path + "] " + "MY DELETE");
            //this.infoQueue.add(new InotifyInfo(path, ino, NO_OPT_STR, NO_OPT_STR, now, DELETE, NO_OPT_INT));
        }
    }

}
