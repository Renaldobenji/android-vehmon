package za.co.vehmon.application.core;

/**
 * Created by Renaldo on 3/7/2015.
 */
public class GPSLog {
    private long id;
    private String lat;
    private String lng;
    private String accuracy;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public int getTimeManagementID() {
        return timeManagementID;
    }

    public void setTimeManagementID(int timeManagementID) {
        this.timeManagementID = timeManagementID;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    private int synced;
    private int timeManagementID;
    private int shiftID;
}
