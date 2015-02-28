package za.co.vehmon.application.services;

/**
 * Created by Renaldo on 2/28/2015.
 */
public class UserTokenValidationResponse {
    public String UserTokenState;

    public static String getStatus(String state)
    {
        try {
            switch (Integer.getInteger(state)) {
                case 0:
                    return "Valid";
                case 1:
                    return "Invalid";
                case 2:
                    return "Timeout";
                case 3:
                    return "NotInRole";
                default:
                    return "Unknown";
            }
        }catch (Exception ex)
        {
            return "Invalid";
        }
    }
}
