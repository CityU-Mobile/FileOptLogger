package edu.cityu.fileoptlogger.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.cityu.fileoptlogger.MainActivity;
import edu.cityu.fileoptlogger.R;
import edu.cityu.fileoptlogger.info.Global;

public class FileObserverService extends Service {

    AlarmManager alarmManager;
    PendingIntent pIntent;
    SharedPreferences sp;
    MyFileObserver observer;
    FileObserverThread task;

    public FileObserverService() {
        observer = new MyFileObserver("/data/data/edu.cityu.fileoptlogger/test", null);
        task = new FileObserverThread();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent pintent, int flags, int startId) {
        Log.i(Global.TAG, " on start data-collecting services~~~~~~~~~~~~~~~~~~~`");
        String s = new ShellUtils("ls -li " + observer.parentDir).executeAndReturn(true);
        Log.i(Global.TAG, "shell return: " + s);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationChannel channel = new NotificationChannel(Global.CHANNEL_ID, Global.CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(Global.NOTIF_DESC);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Global.CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(Global.NOTIF_DESC)
                    .setAutoCancel(true)
                    .setOngoing(true).setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            startForeground(Global.NOTIFICATION_ID, builder.build());
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(Global.NOTIF_DESC)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true).setContentIntent(pendingIntent);

            Notification notification = builder.build();
            startForeground(1, notification);
        }
        Intent intent = new Intent();//创建Intent对象
        intent.setAction(Global.RECEIVER_LAST_TIME_ACTION);
        sendBroadcast(intent);//发送广播
        task.execute();
        return super.onStartCommand(pintent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(Global.TAG, " destroy data-collecting services~~~~~~~~~~~~~~~~~~~`");
        //observer.stopWatching();
        new ShellUtils("kill $(/data/inotifytools/fuser /data/inotifytools/inotifywait)").execute(true);
        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
