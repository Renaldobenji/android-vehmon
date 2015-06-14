package za.co.vehmon.application.services;

import java.util.List;

/**
 * Created by Renaldo on 3/1/2015.
 */
public class LeaveRequestResponse {
    public String RequestStatus;
    public String RequestId;
    public List<LeaveRequestContract> LeaveRequests;
    public String AvailableBalance;

}
