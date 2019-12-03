package edu.cityu.fileoptlogger.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import edu.cityu.fileoptlogger.info.ColdTraceInfo;

public class AsyncTableUpadter extends AsyncTask<String,Integer,Integer> {

    private Handler handler;
    private Context context;

    public AsyncTableUpadter(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... strings) {


        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        DaoOperation<ColdTraceInfo> cold_operation = new DaoOperation(helper.getCustomDao(ColdTraceInfo.class));

        List<ColdTraceInfo> coldTraceInfos = cold_operation.queryWithConuntLimitationOrderDESC(10);

        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putSerializable("table", new BundleSet(coldTraceInfos));
        message.setData(bundle);
        handler.sendMessage(message);

        return null;
    }


}
