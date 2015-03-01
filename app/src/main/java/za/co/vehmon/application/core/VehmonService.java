
package za.co.vehmon.application.core;

import android.content.Context;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import za.co.vehmon.application.services.LeaveRequestResponse;
import za.co.vehmon.application.services.ShiftResponse;
import za.co.vehmon.application.services.TokenGenerationResult;
import za.co.vehmon.application.services.UserTokenValidationResponse;

/**
 * Bootstrap API service
 */
public class VehmonService {

    private RestAdapter restAdapter;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public VehmonService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public VehmonService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
        this.token = token;
    }

    private AuthService getAuthService() {return getRestAdapter().create(AuthService.class);}

    private LeaveService getLeaveService() {return getRestAdapter().create(LeaveService.class);}

    private TimeTrackingService getTimeTrackingService() {return getRestAdapter().create(TimeTrackingService.class);}

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * TODO: Need to implement Service that authenticates user
     * @param email
     * @param password
     * @return
     */
    public User Authenticate(String email, String password) {

        email = "Renaldob";
        password = "Password";
        User user = new User();

        TokenGenerationResult result = getAuthService().GetTokenForUser(email,password);

        if (result.TokenGenerationState.equals("0")) {
            user.setSuccessful(false);
            user.setErrorMessage("User not found");
            return user;
        }
        else if (result.TokenGenerationState.equals("1"))
        {
            user.setSuccessful(false);
            user.setErrorMessage("invalid password");
            return user;
        }

        user.setSuccessful(true);
        user.setFirstName(email);
        user.setLastName("email");
        user.setUsername(email);
        user.setSessionToken(result.GeneratedToken);

        return user;
    }

    public UserTokenValidationResponse RenewUserToken()
    {
        UserTokenValidationResponse response = new UserTokenValidationResponse();
        try {
            response = getAuthService().RenewToken(token);
        }
        catch (Exception ex)
        {
            response.UserTokenState = "1";
        }
        //Fetch token from shared preferences
        return response;
    }

    public LeaveRequestResponse SyncLeaveRequestToServer(String startTime, String endTime, String leaveRequestType)
    {
        LeaveRequestResponse response;
        try
        {
            response = getLeaveService().RequestLeave(this.token,startTime,endTime,leaveRequestType);
        }
        catch (Exception ex)
        {
            return null;
        }
        return response;
    }

    public ShiftResponse SyncStartShiftToServer(String clockInLat, String clockOutLat, String startTime)
    {
        ShiftResponse response;
        try
        {
            response = getTimeTrackingService().StartShift(token,Long.valueOf(clockInLat).longValue(),Long.valueOf(clockOutLat).longValue(),startTime);
        }
        catch (Exception ex)
        {
            return null;
        }

        return response;
    }

    public ShiftResponse SyncEndShiftToServer(String shiftID, String endTime)
    {
        ShiftResponse response;

        try
        {
            response = getTimeTrackingService().EndShift(token,Integer.valueOf(shiftID),endTime);
        }
        catch (Exception ex)
        {
            return null;
        }
        return response;
    }

    public TimeManagementWrapper.TimeManagementResult ClockIn(Context context, Date date)
    {
        return new TimeManagementWrapper().ClockIn(context, date);
    }

    public TimeManagementWrapper.TimeManagementResult ClockOut(Context context, Date date)
    {
        return new TimeManagementWrapper().ClockOut(context, date);
    }

    public String[] FetchAbsenceTypes()
    {
        return new AbsenceRequestWrapper().FetchAbsenceTypes();
    }

    public AbsenceRequestWrapper.AbsenceRequestResult SubmitAbsenceRequest(Context context, int absenceRequestTypeID, Date fromDate, Date toDate)
    {
        return new AbsenceRequestWrapper().SubmitAbsenceRequest(context,absenceRequestTypeID,fromDate,toDate);
    }

    public MessageWrapper.MessageResult CreateNewMessage(Context context, String from, String to)
    {
        return new MessageWrapper().CreateNewConversation(context,from,to);
    }

    public MessageWrapper.MessageResult GetAllMessageConversations(Context context)
    {
        return new MessageWrapper().GetAllMessage(context);
    }

    public MessageWrapper.MessageResult GetAllMessageForConversation(Context context, long conversationID)
    {
        return new MessageWrapper().GetAllMessageForConversation(context, conversationID);
    }

    public MessageWrapper.MessageResult SubmitMessage(Context context, Integer conversationID, String from, String to, String message)
    {
        return new MessageWrapper().SubmitMessage(context, conversationID,from,to,message);
    }

    public GPSLogWrapper.GPSLogResult LogGPSCoordinates(Context context, String lat, String lng, String accuracy, String timeManagementID)
    {
        return new GPSLogWrapper().LogGPSCoordinates(context,lat,lng,accuracy,timeManagementID);
    }

}