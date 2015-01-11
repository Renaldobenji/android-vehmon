
package za.co.vehmon.application;

import android.accounts.AccountsException;
import android.app.Activity;

import java.io.IOException;

import retrofit.RestAdapter;
import za.co.vehmon.application.authenticator.ApiKeyProvider;
import za.co.vehmon.application.core.BootstrapService;
import za.co.vehmon.application.core.VehmonService;

/**
 * Provider for a {@link za.co.vehmon.application.core.BootstrapService} instance
 */
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
    public VehmonService getService(final Activity activity)
            throws IOException, AccountsException {
        // The call to keyProvider.getAuthKey(...) is what initiates the login screen. Call that now.
        keyProvider.getAuthKey(activity);

        // TODO: See how that affects the bootstrap service.
        return new VehmonService(restAdapter);
    }
}