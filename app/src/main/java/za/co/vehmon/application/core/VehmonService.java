
package za.co.vehmon.application.core;

import android.content.Context;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class VehmonService {

    private RestAdapter restAdapter;

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
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }

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
        User user = new User();
        user.setFirstName("Renaldo");
        user.setLastName("Benjamin");
        user.setObjectId("#12345");
        user.setPhone("0722771137");
        user.setUsername("Renaldob");
        user.setSessionToken("#SessionToken");
        return user;
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