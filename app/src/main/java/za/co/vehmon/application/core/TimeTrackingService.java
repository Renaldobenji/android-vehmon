package za.co.vehmon.application.core;

import retrofit.http.GET;
import retrofit.http.Path;
import za.co.vehmon.application.services.ShiftResponse;

/**
 * Created by Renaldo on 3/1/2015.
 */
public interface TimeTrackingService {

    @GET(Constants.Http.VEHMON_URL_AUTH_STARTSHIFT_FRAG)
    ShiftResponse StartShift(@Path("token") String token,@Path("clockInLat") long clockInLat,@Path("clockOutLat") long clockOutLat,@Path("startTime") String startTime);

    @GET(Constants.Http.VEHMON_URL_AUTH_ENDSHIFT_FRAG)
    ShiftResponse EndShift(@Path("userToken") String userToken,@Path("shiftId") int shiftId, @Path("endTime") String endTime);

    ShiftResponse LogCoordinatesToShift(String userToken, int shiftId, String csvCoords);
}
