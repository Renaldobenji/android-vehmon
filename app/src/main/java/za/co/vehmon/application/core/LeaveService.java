package za.co.vehmon.application.core;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import za.co.vehmon.application.services.LeaveRequestContract;
import za.co.vehmon.application.services.LeaveRequestResponse;

/**
 * Created by Renaldo on 3/1/2015.
 */
public interface LeaveService {

    //yyyy/MM/dd/HH/mm
    @POST(Constants.Http.VEHMON_URL_AUTH_LEAVE_FRAG)
    LeaveRequestResponse RequestLeave(@Path("token") String token, @Path("startTime") String startTime,@Path("endTime") String endTime,@Path("leaveRequestType") String leaveRequestType);

    @GET(Constants.Http.VEHMON_URL_AUTH_GETALLLEAVE_FRAG)
    LeaveRequestResponse GetAllLeaveRequests(@Path("token") String token);
}
