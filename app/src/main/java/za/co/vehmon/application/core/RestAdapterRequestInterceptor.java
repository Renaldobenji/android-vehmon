package za.co.vehmon.application.core;

import android.util.Base64;

import retrofit.RequestInterceptor;

public class RestAdapterRequestInterceptor implements RequestInterceptor {

    private UserAgentProvider userAgentProvider;

    public RestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
    }

    @Override
    public void intercept(RequestFacade request) {
        // Add header to set content type of JSON
        request.addHeader("Content-Type", "application/json");
        request.addHeader("User-Agent", userAgentProvider.get());
    }
}
