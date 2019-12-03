package edu.cityu.fileoptlogger.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import edu.cityu.fileoptlogger.info.Global;

public class UIUpdateReceiver extends BroadcastReceiver {

    private Handler lastColTimeHandler;
    private Handler tableHandler;

    public UIUpdateReceiver(Handler lastColTimeHandler, Handler tableHandler) {
        this.lastColTimeHandler = lastColTimeHandler;
        this.tableHandler = tableHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Global.RECEIVER_LAST_TIME_ACTION)) { ;
            Message message = lastColTimeHandler.obtainMessage();
            lastColTimeHandler.sendMessage(message);
        } else if(action.equals(Global.RECEIVER_GET_TABLE_ACTION)) {

        }
    }

}
