package za.co.vehmon.application.core;

/**
 * Created by Renaldo on 2/16/2015.
 */
public class TimeManagement {
    private int id;
    private String clockInTime;
    private String clockOutTime;
    private String inLat;
    private String inLng;

    public String getShiftID() {
        return shiftID;
    }

    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }

    private String shiftID;
    private String outLat;
    private String outLng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public String getInLat() {
        return inLat;
    }

    public void setInLat(String inLat) {
        this.inLat = inLat;
    }

    public String getInLng() {
        return inLng;
    }

    public void setInLng(String inLng) {
        this.inLng = inLng;
    }

    public String getOutLat() {
        return outLat;
    }

    public void setOutLat(String outLat) {
        this.outLat = outLat;
    }

    public String getOutLng() {
        return outLng;
    }

    public void setOutLng(String outLng) {
        this.outLng = outLng;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getInSynced() {
        return inSynced;
    }

    public void setInSynced(int inSynced) {
        this.inSynced = inSynced;
    }

    public int getOutSynced() {
        return outSynced;
    }

    public void setOutSynced(int outSynced) {
        this.outSynced = outSynced;
    }

    private String userID;
    private int inSynced;
    private int outSynced;
}
