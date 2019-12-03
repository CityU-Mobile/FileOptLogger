package edu.cityu.fileoptlogger.info;

public class InotifyInfo {

    private String fileName;
    private long ino;
    private String cTime;
    private String aTime;
    private String dTime;
    private int opt;
    private long fileSize = -1;

    public static final int CREATE = 1;
    public static final int ACCESS = 2;
    public static final int MODIFY = 3;
    public static final int DELETE = 4;

    public InotifyInfo(String fileName, long ino, String cTime, String aTime, String dTime, int opt, long fileSize) {
        this.fileName = fileName;
        this.ino = ino;
        this.cTime = cTime;
        this.aTime = aTime;
        this.dTime = dTime;
        this.opt = opt;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getIno() {
        return ino;
    }

    public void setIno(long ino) {
        this.ino = ino;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getaTime() {
        return aTime;
    }

    public void setaTime(String aTime) {
        this.aTime = aTime;
    }

    public String getdTime() {
        return dTime;
    }

    public void setdTime(String dTime) {
        this.dTime = dTime;
    }

    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


    public static int getOptCode(String event) {
        if(event.contains("ISDIR")) {
            return -1;
        }

        if(event.contains("CREATE")) {
            return CREATE;
        }

        if(event.contains("ACCESS")) {
            return ACCESS;
        }

        if(event.contains("MODIFY")) {
            return MODIFY;
        }

        if(event.contains("DELETE")) {
            return DELETE;
        }

        return -1;
    }

}
