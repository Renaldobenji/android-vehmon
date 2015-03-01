package za.co.vehmon.application.core;

/**
 * Created by Renaldo on 1/15/2015.
 */
public class AbsenceRequest {

    public String[] getAbsenceRequestTypes() {
        return AbsenceRequestTypes;
    }

    public void setAbsenceRequestTypes(String[] absenceRequestTypes) {
        AbsenceRequestTypes = absenceRequestTypes;
    }

    public String[] AbsenceRequestTypes;

    public AbsenceRequest()
    {
        String[] absenceTypes = new String[5];

        absenceTypes[0] = "Sick";
        absenceTypes[1] = "Annual";

        this.setAbsenceRequestTypes(absenceTypes);
    }

    private String fromDate;
    private String toDate;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(String leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
    }

    public Boolean getSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    private String userID;
    private String leaveTypeID;
    private Boolean synced;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
}
