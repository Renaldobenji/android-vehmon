package za.co.vehmon.application.core;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import za.co.vehmon.application.services.Coordinate;
import za.co.vehmon.application.services.ShiftResponse;

/**
 * Created by Renaldo on 3/1/2015.
 */
public interface TimeTrackingService {

    @POST(Constants.Http.VEHMON_URL_AUTH_STARTSHIFT_FRAG)
    ShiftResponse StartShift(@Path("token") String token,@Path("clockInLat") String clockInLat,@Path("clockOutLat") String clockOutLat,@Path("startTime") String startTime);

    //@POST(Constants.Http.VEHMON_URL_AUTH_ENDSHIFT_FRAG)
    //ShiftResponse EndShift(@Path("userToken") String userToken,@Path("shiftId") String shiftId, @Path("endTime") String endTime);

    @POST(Constants.Http.VEHMON_URL_AUTH_ENDSHIFT_FRAG)
    ShiftResponse EndShift(@Path("userToken") String userToken,@Path("shiftId") String shiftId, @Path("endTime") String endTime);

    @POST(Constants.Http.VEHMON_URL_AUTH_LOGGPS_FRAG)
    ShiftResponse LogCoordinatesToShift(@Path("token")String userToken,@Path("shiftId") String shiftId,@Path("csvCoords") String coords);

    @GET(Constants.Http.VEHMON_URL_AUTH_GETSHIFTS_FRAG)
    List<ShiftReportContract> GetUserShifts(@Path("token")String userToken,@Path("startDate") String startDate,@Path("endDate") String endDate);
}
