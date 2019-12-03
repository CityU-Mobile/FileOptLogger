package edu.cityu.fileoptlogger.utils;


import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import edu.cityu.fileoptlogger.info.Global;

/**
 * @className DaoOperation
 * @description 批量操作的封装
 * @author 潘日维
 * @version V1.0
 * @date 2017/6/7
 */

public class DaoOperation<T> {

    public static final int INSERT  = 1;
    public static final int DELETE  = 2;
    public static final int UPDATE  = 3;

    private Dao<T,?> customDao;

    public DaoOperation(Dao dao) {
        this.customDao = dao;
    }

    public boolean doBatchInTransaction(final T[] list, final int batchType) {
        boolean doBatch = false;
        ConnectionSource connectionSource = customDao.getConnectionSource();
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return doBatch(list, batchType);
            }
        };
        try {
            doBatch = transactionManager.callInTransaction(callable);
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
        }
        return doBatch;
    }

    private boolean doBatch(T[] list, int batchType) {
        int result = 0;
        try {
            for (T t : list) {
                switch (batchType) {
                    case DaoOperation.INSERT:
                        result += customDao.create(t);
                        break;
                    case DaoOperation.DELETE:
                        result += customDao.delete(t);
                        break;
                    case DaoOperation.UPDATE:
                        if(t != null){
                            result += customDao.update(t);
                        }
                        break;
                    default:
                        Log.w(Global.TAG, "no this type.");
                        break;
                }
            }
        } catch (SQLException e) {
            Log.e(Global.TAG, e.getMessage());
        }
        return result == list.length;
    }

    public boolean doBatchInTransaction(final List<T> list, final int batchType) {
        boolean doBatch = false;
        ConnectionSource connectionSource = customDao.getConnectionSource();
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return doBatch(list, batchType);
            }
        };
        try {
            doBatch = transactionManager.callInTransaction(callable);
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
        }
        return doBatch;
    }

    private boolean doBatch(List<T> list, int batchType) {
        int result = 0;
        try {
            for (T t : list) {
                switch (batchType) {
                    case DaoOperation.INSERT:
                        result += customDao.create(t);
                        break;
                    case DaoOperation.DELETE:
                        result += customDao.delete(t);
                        break;
                    case DaoOperation.UPDATE:
                        if(t != null){
                            result += customDao.update(t);
                        }
                        break;
                    default:
                        Log.w(Global.TAG, "no this type.");
                        break;
                }
            }
        } catch (SQLException e) {
            Log.e(Global.TAG, e.getMessage());
        }
        return result == list.size();
    }

    public boolean insert(T obj){
        try {
            int n = this.customDao.create(obj);
            return n == 1 ? true : false;
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return false;
        }
    }

    public boolean update(T obj){
        try {
            int n = this.customDao.update(obj);
            return n == 1 ? true : false;
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return false;
        }
    }

    public boolean delete(T obj){
        try {
            int n = this.customDao.delete(obj);
            return n == 1 ? true : false;
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return false;
        }
    }

    public T query(String path) {
        try {
            List<T> varlist = this.customDao.queryForEq("path", path);
            if(varlist != null && varlist.size() != 0) {
                return varlist.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return null;
        }
    }

    public List<T> queryWithConutAndOrderLimitation(long count, String col, boolean ascending){
        try {
            long size = this.customDao.countOf();
            if(count > size){
                count = size;
            }
            List<T> tList = this.customDao.queryBuilder().limit(count).orderBy(col, ascending).query();
            return tList;
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return null;
        }
    }

    public long countof(){
        try {
            return this.customDao.queryBuilder().countOf();
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return -1;
        }
    }

    public List<T> queryWithConuntLimitation(long count){
        try {
            List<T> query = this.customDao.queryBuilder().limit(count).query();
            return query;
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return null;
        }
    }

    public List<T> queryWithConuntLimitationOrderDESC(long count){
        try {
            List<T> query = this.customDao.queryBuilder().limit(count).query();
            return query;
        } catch (SQLException e) {
            Log.e(Global.TAG, "e = " + e.getMessage());
            return null;
        }
    }


}
