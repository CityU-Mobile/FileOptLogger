package edu.cityu.fileoptlogger.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import edu.cityu.fileoptlogger.MainActivity;
import edu.cityu.fileoptlogger.R;
import edu.cityu.fileoptlogger.info.Global;

public class MoveDataService extends Service {


    private MoveDataThread task;

    public MoveDataService() {
        this.task = new MoveDataThread(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent pintent, int flags, int startId) {
        Log.i(Global.TAG, " on start data-moving services~~~~~~~~~~~~~~~~~~~`");
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
        if(!task.isRunning()) {
            task.execute();
            Toast.makeText(this, "start data-moving service", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "data-moving service is running", Toast.LENGTH_SHORT);
        }
        return super.onStartCommand(pintent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(Global.TAG, " destroy data-moving services~~~~~~~~~~~~~~~~~~~`");
        task.stop();
        Toast.makeText(this, "data-moving service is stopped", Toast.LENGTH_SHORT);
        stopForeground(true);
        super.onDestroy();
    }



}
