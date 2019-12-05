package edu.cityu.fileoptlogger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.cityu.fileoptlogger.info.ColdTraceInfo;
import edu.cityu.fileoptlogger.info.DeviceInfo;
import edu.cityu.fileoptlogger.info.Global;
import edu.cityu.fileoptlogger.utils.AsyncTableUpadter;
import edu.cityu.fileoptlogger.utils.BundleSet;
import edu.cityu.fileoptlogger.utils.FileObserverService;
import edu.cityu.fileoptlogger.utils.InotifyInstallationService;
import edu.cityu.fileoptlogger.utils.MoveDataService;
import edu.cityu.fileoptlogger.utils.TableAdapter;
import edu.cityu.fileoptlogger.utils.UIUpdateReceiver;

public class MainActivity extends AppCompatActivity {

    private Handler time_handler;
    private Handler table_mHandler;

    private ListView tableListView;
    Context context;

    public TextView col_time_tv;
    public Intent sev_intent;
    public Intent exdata_intent;
    private UIUpdateReceiver ui_receiver;
    private static final int PERMISSIONS_CODE = 1001; //权限通知回调标识位
    public static final int PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS_CODE = 1002;
    public static final String DENY_MESSAGE = "获取应用程序统计权限失败";

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE);
        initMeasureID();

        context = getApplicationContext();

        TextView userid_label_tv = (TextView) findViewById(R.id.userid_label);
        userid_label_tv.setText("User ID:");
        userid_label_tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        TextView userid_tv = (TextView) findViewById(R.id.userid);
        userid_tv.setText(Global.USER_DEVICEINFO.toUserId());


        TextView col_label_tv = (TextView) findViewById(R.id.collection_time_label);
        col_label_tv.setText("Last Collection Time:");
        col_label_tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        col_time_tv = (TextView) findViewById(R.id.collection_time);
        col_time_tv.setText("");

        startService(new Intent(this, InotifyInstallationService.class));

        sev_intent  = new Intent(this, FileObserverService.class);
        Switch sev_switch = findViewById(R.id.sev_switch);
        sev_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                if(status) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        startForegroundService(sev_intent);
                    else
                        startService(sev_intent);
                } else {
                    stopService(sev_intent);
                }
            }
        });

        exdata_intent = new Intent(this, MoveDataService.class);
        Switch exdata_switch = findViewById(R.id.exdata_switch);
        exdata_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                if(status) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        startForegroundService(exdata_intent);
                    else
                        startService(exdata_intent);
                } else {
                    stopService(exdata_intent);
                }
            }
        });

        time_handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                col_time_tv.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
        };

        ui_receiver = new UIUpdateReceiver(time_handler, table_mHandler);
        IntentFilter ui_filter = new IntentFilter();
        ui_filter.addAction(Global.RECEIVER_GET_TABLE_ACTION);
        ui_filter.addAction(Global.RECEIVER_LAST_TIME_ACTION);
        registerReceiver(ui_receiver, ui_filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ui_receiver);
    }

    @SuppressLint("MissingPermission")
    public void initMeasureID(){
        Global.USER_DEVICEINFO = new DeviceInfo();
        SharedPreferences sp = getSharedPreferences(Global.SHAREDPREFFERENCES, MODE_PRIVATE);
        if(sp.getString("username", null) == null) {
            SharedPreferences.Editor editor = sp.edit();
            try {
                Global.USER_DEVICEINFO.setDeviceId("uid"); // 用来作为用户的唯一id
                Global.USER_DEVICEINFO.setBuildModel(android.os.Build.MODEL);
                Global.USER_DEVICEINFO.setReleaseVersion(android.os.Build.VERSION.RELEASE);
                Global.USER_DEVICEINFO.setUuid(UUID.randomUUID().toString().replace("-",""));
                editor.putString("username", Global.USER_DEVICEINFO.toDeviceId());
                editor.commit();
                Log.i(Global.TAG, "SERVICE ... UNIQUEID = " + Global.USER_DEVICEINFO.toDeviceId());
            }catch (Exception e){
                Log.i(Global.TAG, "SERVICE ... UNIQUEID = " + e.getMessage());
            }
        } else {
            String username = sp.getString("username", null);
            String[] split = username.split(",");
            Global.USER_DEVICEINFO.setUuid(split[0]);
            Global.USER_DEVICEINFO.setDeviceId(split[1]);
            Global.USER_DEVICEINFO.setBuildModel(split[2]);
            Global.USER_DEVICEINFO.setReleaseVersion(split[3]);
        }
    }


    private void initPermissions(String ... permissions){

        ArrayList<String> permissionList = new ArrayList<>();
        //获取写入到sdcard的权限

        for (String p : permissions) {
            if(ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED){
                Log.i(Global.TAG, "申请获取" + p + "的权限");
                permissionList.add(p);
            }
        }

        String[] permissionSet = permissionList.toArray(new String[permissionList.size()]);

        if(permissionSet.length > 0){
            ActivityCompat.requestPermissions(this, permissionSet, PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isWritable = false;
        boolean isReadable = false;

        if(requestCode == PERMISSIONS_CODE){

            for (int i = 0; i < permissions.length; i++) {

                Log.i(Global.TAG, "permission = " + permissions[i] + ", ret = " + grantResults[i]);

                if(permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    isWritable = true;
                }

                if(permissions[i].equals(Manifest.permission.READ_PHONE_STATE) && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    isReadable = true;
                }

            }


        }


    }

}