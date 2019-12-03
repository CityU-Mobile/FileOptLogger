package edu.cityu.fileoptlogger.info;

/**
 * Created by Hubery on 2017/10/26.
 */

public class DeviceInfo {

    private String uuid;
    private String deviceId;
    private String buildModel;
    private String releaseVersion;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceId, String buildModel, String releaseVersion) {
        this.deviceId = deviceId;
        this.buildModel = buildModel;
        this.releaseVersion = releaseVersion;
    }

    public DeviceInfo(String uuid, String deviceId, String buildModel, String releaseVersion) {
        this.uuid = uuid;
        this.deviceId = deviceId;
        this.buildModel = buildModel;
        this.releaseVersion = releaseVersion;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBuildModel() {
        return buildModel;
    }

    public void setBuildModel(String buildModel) {
        this.buildModel = buildModel;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String toUserId(){
        return nullStr(deviceId) + "-" + nullStr(buildModel) + "-" + nullStr(releaseVersion);
    }

    public String toDeviceId(){
        return nullStr(uuid) + "," + nullStr(deviceId) + "," + nullStr(buildModel) + "," + nullStr(releaseVersion);

    }

    private String nullStr(String str){
        if(str == null || str.equals("")){
            str = "null";
        }
        return str;
    }

}
