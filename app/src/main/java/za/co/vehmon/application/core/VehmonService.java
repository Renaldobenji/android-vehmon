
package za.co.vehmon.application.core;

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
    public User authenticate(String email, String password) {
        User user = new User();
        user.setFirstName("Renaldo");
        user.setLastName("Benjamin");
        user.setObjectId("#12345");
        user.setPhone("0722771137");
        user.setUsername("Renaldob");
        user.setSessionToken("#SessionToken");
        return user;
    }
}