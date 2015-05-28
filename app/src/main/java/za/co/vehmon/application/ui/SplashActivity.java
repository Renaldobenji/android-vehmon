package za.co.vehmon.application.ui;

import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import javax.inject.Inject;

import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity;
import za.co.vehmon.application.core.Constants;
import za.co.vehmon.application.core.User;
import za.co.vehmon.application.services.UserTokenValidationResponse;
import za.co.vehmon.application.util.Ln;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 3/14/2015.
 */
public class SplashActivity extends Activity {

    @Inject protected VehmonServiceProvider serviceProvider;

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    serviceProvider.getService(getApplicationContext()).SantechLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AccountsException e) {
                    e.printStackTrace();

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Injector.inject(this);
        //I will do all my crap here
        //registerInBackground();
        //End
        new SafeAsyncTask<UserTokenValidationResponse>() {

            @Override
            public UserTokenValidationResponse call() throws Exception {
                UserTokenValidationResponse response = serviceProvider.getService(SplashActivity.this).RenewUserToken();
                return response;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final UserTokenValidationResponse hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                boolean userHasAuthenticated = (hasAuthenticated.UserTokenState.equals("Valid"));
                if (userHasAuthenticated)
                {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constants.VehmonSharedPrefs.name, Context.MODE_PRIVATE);
                    String username = sharedPref.getString("username","");
                    User user = new User();
                    user.setUsername(username);
                    final BootstrapApplication globalApplication = (BootstrapApplication)getApplicationContext();
                    globalApplication.setUser(user);
                    navigateToMain();
                    finish();
                }else
                {
                    navigateToLogin();
                    finish();
                }

            }
        }.execute();

    }

    private void navigateToLogin() {
        final Intent i = new Intent(this, BootstrapAuthenticatorActivity.class);
        startActivity(i);
    }

    private void navigateToMain() {
        final Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}


