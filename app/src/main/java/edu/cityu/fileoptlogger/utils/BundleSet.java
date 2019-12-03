package edu.cityu.fileoptlogger.utils;

import java.io.Serializable;
import java.util.List;

import edu.cityu.fileoptlogger.info.ColdTraceInfo;

public class BundleSet implements Serializable {

    private List<ColdTraceInfo> list;

    public BundleSet(List<ColdTraceInfo> list) {
        this.list = list;
    }

    public List<ColdTraceInfo> getList() {
        return list;
    }
}
