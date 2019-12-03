package edu.cityu.fileoptlogger.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

import edu.cityu.fileoptlogger.info.ColdTraceInfo;
import edu.cityu.fileoptlogger.info.Global;
import edu.cityu.fileoptlogger.info.HotTraceInfo;

/**
 * @className DatabaseHelper
 * @description 数据库helper
 * @author 潘日维
 * @version V1.0
 * @date 2017/6/7
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private static final String DB_NAME = "user_trace.db";
    private static DatabaseHelper instance = null;
    private static Class[] classes;
    private HashMap<Class, Dao> map;

    public DatabaseHelper(Context context, Class... clazz){
        super(context, DB_NAME, null, DATABASE_VERSION);
        classes = clazz;
        map = new HashMap<>();
    }


    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            Class[] clazz = new Class[]{HotTraceInfo.class, ColdTraceInfo.class};
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context, clazz);
            }
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.d(Global.TAG, " sqlite oncreate ...");
            if(classes != null){
                for(Class c : classes){
                    Log.d(Global.TAG , "create table ... " + c.getName());
                    int s = TableUtils.createTableIfNotExists(connectionSource, c);
                    Log.d(Global.TAG , "create table ... " + c.getName() + ", ret = " + s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public <E> Dao<E , Integer> getCustomDao(Class<E> clazz) {

        try {

            if(!this.map.containsKey(clazz)){
                map.put(clazz, getDao(clazz));
            }

        } catch (SQLException e) {

            return null;

        }

        return map.get(clazz);
    }

    @Override
    public void close() {
        super.close();
        if(this.map.size() > 0){
            Set<Class> classes = this.map.keySet();
            for (Class c: classes){
                Dao dao = this.map.get(c);
                dao = null;
            }
        }
    }

}
