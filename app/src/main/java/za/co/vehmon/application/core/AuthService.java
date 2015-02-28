package za.co.vehmon.application.core;

import retrofit.http.GET;
import retrofit.http.Path;
import za.co.vehmon.application.services.TokenGenerationResult;
import za.co.vehmon.application.services.UserTokenValidationResponse;

/**
 * Created by Renaldo on 2/28/2015.
 */
public interface AuthService {

    @GET(Constants.Http.VEHMON_URL_AUTH_FRAG)
    TokenGenerationResult GetTokenForUser(@Path("userName") String username, @Path("password") String password);

    @GET(Constants.Http.VEHMON_URL_AUTH_RENEW_FRAG)
    UserTokenValidationResponse RenewToken(@Path("token") String token);
}
