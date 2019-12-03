package edu.cityu.fileoptlogger.info;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "hot_trace_info")
public class HotTraceInfo {

    public HotTraceInfo() {
    }

    public HotTraceInfo(String path, String ctime, String atime, String dtime, long fsize) {
        this.path = path;
        this.ctime = ctime;
        this.atime = atime;
        this.dtime = dtime;
        this.fsize = fsize;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String path;

    @DatabaseField(columnName = "ctime")
    private String ctime;

    @DatabaseField(columnName = "atime")
    private String atime;

    @DatabaseField(columnName = "dtime")
    private String dtime;

    @DatabaseField(columnName = "fsize")
    private long fsize;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public long getFsize() {
        return fsize;
    }

    public void setFsize(long fsize) {
        this.fsize = fsize;
    }
}
