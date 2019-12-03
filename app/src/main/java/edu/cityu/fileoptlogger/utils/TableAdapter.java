package edu.cityu.fileoptlogger.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.cityu.fileoptlogger.R;
import edu.cityu.fileoptlogger.info.ColdTraceInfo;

public class TableAdapter extends BaseAdapter {

    private List<ColdTraceInfo> list;
    private LayoutInflater inflater;

    public TableAdapter(Context context, List<ColdTraceInfo> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null){
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ColdTraceInfo goods = (ColdTraceInfo) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.path = (TextView) convertView.findViewById(R.id.text_path);
            viewHolder.ctime = (TextView) convertView.findViewById(R.id.text_ctime);
            viewHolder.atime = (TextView) convertView.findViewById(R.id.text_atime);
            viewHolder.dtime = (TextView) convertView.findViewById(R.id.text_dtime);
            viewHolder.fsize = (TextView) convertView.findViewById(R.id.text_fsize);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.path.setText(goods.getPath());
        viewHolder.path.setTextSize(13);
        viewHolder.path.setSelected(true);
        viewHolder.path.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.path.setFocusable(true);
        viewHolder.path.setFocusableInTouchMode(true);
        viewHolder.path.setSingleLine(true);

        viewHolder.ctime.setText(goods.getCtime());
        viewHolder.ctime.setTextSize(13);
        viewHolder.ctime.setSelected(true);
        viewHolder.ctime.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.ctime.setFocusable(true);
        viewHolder.ctime.setFocusableInTouchMode(true);
        viewHolder.ctime.setSingleLine(true);

        viewHolder.atime.setText(goods.getAtime());
        viewHolder.atime.setTextSize(13);
        viewHolder.atime.setSelected(true);
        viewHolder.atime.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.atime.setFocusable(true);
        viewHolder.atime.setFocusableInTouchMode(true);
        viewHolder.atime.setSingleLine(true);

        viewHolder.dtime.setText(goods.getDtime());
        viewHolder.dtime.setTextSize(13);
        viewHolder.dtime.setSelected(true);
        viewHolder.dtime.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.dtime.setFocusable(true);
        viewHolder.dtime.setFocusableInTouchMode(true);
        viewHolder.dtime.setSingleLine(true);

        viewHolder.fsize.setText(goods.getFsize() + "");
        viewHolder.fsize.setTextSize(13);
        viewHolder.fsize.setSelected(true);
        viewHolder.fsize.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        viewHolder.fsize.setFocusable(true);
        viewHolder.fsize.setFocusableInTouchMode(true);
        viewHolder.fsize.setSingleLine(true);

        return convertView;
    }

    public static class ViewHolder{
        public TextView path;
        public TextView ctime;
        public TextView atime;
        public TextView dtime;
        public TextView fsize;
    }
}
