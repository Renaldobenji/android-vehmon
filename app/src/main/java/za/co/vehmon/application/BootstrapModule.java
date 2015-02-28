package za.co.vehmon.application;

import android.accounts.AccountManager;
import android.content.Context;

import za.co.vehmon.application.authenticator.ApiKeyProvider;
import za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.Constants;
import za.co.vehmon.application.core.PostFromAnyThreadBus;
import za.co.vehmon.application.core.RestAdapterRequestInterceptor;
import za.co.vehmon.application.core.RestErrorHandler;
import za.co.vehmon.application.core.TimerService;
import za.co.vehmon.application.core.UserAgentProvider;
import za.co.vehmon.application.core.VehmonService;
import za.co.vehmon.application.gps.GPSTrackingService;
import za.co.vehmon.application.ui.AbsenceRequestActivity;
import za.co.vehmon.application.ui.BootstrapTimerActivity;
import za.co.vehmon.application.ui.Dialogs.NewMessageDialog;
import za.co.vehmon.application.ui.MainActivity;
import za.co.vehmon.application.ui.MessageListFragment;
import za.co.vehmon.application.ui.MessageViewActivity;
import za.co.vehmon.application.ui.NavigationDrawerFragment;
import za.co.vehmon.application.ui.TimeManagementFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                NavigationDrawerFragment.class,
                TimerService.class,
                VehmonService.class,
                TimeManagementFragment.class,
                AbsenceRequestActivity.class,
                MessageListFragment.class,
                MessageViewActivity.class,
                NewMessageDialog.class,
                GPSTrackingService.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    VehmonService provideVehmonService(RestAdapter restAdapter) {
        return new VehmonService(restAdapter);
    }

    @Provides
    VehmonServiceProvider provideVehmonServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new VehmonServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.VEHMON_URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
