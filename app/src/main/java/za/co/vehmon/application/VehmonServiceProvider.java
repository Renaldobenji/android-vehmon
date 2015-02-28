
package za.co.vehmon.application;

import android.accounts.AccountsException;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import retrofit.RestAdapter;
import za.co.vehmon.application.authenticator.ApiKeyProvider;
import za.co.vehmon.application.core.Constants;
import za.co.vehmon.application.core.VehmonService;

public class VehmonServiceProvider {

    private RestAdapter restAdapter;
    private ApiKeyProvider keyProvider;

    public VehmonServiceProvider(RestAdapter restAdapter, ApiKeyProvider keyProvider) {
        this.restAdapter = restAdapter;
        this.keyProvider = keyProvider;
    }

    /**
     * Get service for configured key provider
     * <p/>
     * This method gets an auth key and so it blocks and shouldn't be called on the main thread.
     *
     * @return bootstrap service
     * @throws java.io.IOException
     * @throws android.accounts.AccountsException
     */

    public VehmonService getService(final Context context)
            throws IOException, AccountsException {

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.VehmonSharedPrefs.name, Context.MODE_PRIVATE);
        String token = sharedPref.getString("AuthToken","");
        VehmonService service = new VehmonService(restAdapter);
        service.setToken(token);
        // TODO: See how that affects the bootstrap service.
        return service;
    }
}
