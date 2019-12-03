package edu.cityu.fileoptlogger.info;

public class Global {

    public static String LAST_COLLECTION_TIME = "";
    public static DeviceInfo USER_DEVICEINFO = null;
    public static final String TAG = "FileOptLogger";
    public static final String SHAREDPREFFERENCES = "FileOptLogger";
    public static final String CHANNEL_ID = "edu.cityu.fileoptlogger";
    public static final String CHANNEL_NAME = "FileOptMonitor";
    public static final String NOTIF_DESC = "Collecting VFS Trace";
    public static final String NOTIF_DESC2 = "Moving VFS Trace";
    public static final int NOTIFICATION_ID = 1001;

    public static final String APPLOCATION = "/data/data/edu.cityu.fileoptlogger/";
    public static final String MAIN_LOCATION = "/data/inotifytools/";
    public static final String INOTIFYWAIT_LOCATION = APPLOCATION + "inotifywait";
    public static final String INOTIFYWAIT_MAIN_LOCATION = MAIN_LOCATION + "inotifywait";
    public static final String FUSER_MAIN_LOCATION = MAIN_LOCATION + "fuser";
    public static final String FUSER_LOCATION = APPLOCATION + "fuser";

    public static final String RECEIVER_LAST_TIME_ACTION = "edu.cityu.fileoptlogger.lasttime";
    public static final String RECEIVER_GET_TABLE_ACTION = "edu.cityu.fileoptlogger.gettable";

    public static final String LOG_FILE_LOCATION = "/sdcard/fblog/";

}
