

package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import butterknife.ButterKnife;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.RegistrationIntentService;
import za.co.vehmon.application.VehmonPreferences;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.BootstrapAuthenticatorActivity;
import za.co.vehmon.application.core.Constants;
import za.co.vehmon.application.core.User;
import za.co.vehmon.application.core.VehmonService;
import za.co.vehmon.application.events.NavItemSelectedEvent;
import za.co.vehmon.application.gps.GPSTrackingService;
import za.co.vehmon.application.services.UserTokenValidationResponse;
import za.co.vehmon.application.synchronizers.SynchronizeProcessor;
import za.co.vehmon.application.util.Ln;
import za.co.vehmon.application.util.SafeAsyncTask;
import za.co.vehmon.application.util.UIUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;




/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link za.co.vehmon.application.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    @Inject protected VehmonServiceProvider serviceProvider;

    //Server API key:
    //AIzaSyAj9222KhG5qX667aK7DhLA2bo18-bvPmQ
    //Sender ID:
    //531347791726

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "1";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String SENDER_ID = "531347791726";
    private GoogleCloudMessaging gcm;
    private AtomicInteger msgId = new AtomicInteger();
    static final String TAG = "Vehmon App";
    String regid;
    Context context;
    private static final String url = "http://vehmonmachine.cloudapp.net/";
    private static final String apkName = "Vehmon.apk";
    public ProgressDialog pd;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        this.context = this;
        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
        ButterKnife.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        if(!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,           /* DrawerLayout object */
                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(title);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(drawerTitle);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);

            navigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            navigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean sentToken = sharedPreferences
                        .getBoolean(VehmonPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(context,"Registration Successful",Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT);
                }
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean sentToken = sharedPreferences.getBoolean(VehmonPreferences.SENT_TOKEN_TO_SERVER, false);
        if (!sentToken) {
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
        initScreen();
        startSynchronization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(VehmonPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend(context, regid);

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(Context context, String regId) {
        try {
            serviceProvider.getService(context).SetDeviceID(regId);
        }
        catch (Exception ex)
        {
            //TODO: Sort out this errors
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
        startActivity(i);
    }


    private void initScreen() {
        Ln.d("Foo");
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new CarouselFragment())
                .commit();
    }


    private void navigateToAbsenceRequest()
    {
        final Intent i = new Intent(this,AbsenceRequestActivity.class);
        startActivity(i);
    }

    private void navigateToViewLeave()
    {
        final Intent i = new Intent(this,ViewLeaveActivity.class);
        startActivity(i);
    }

    private void navigateToViewShifts()
    {
        final Intent i = new Intent(this,ViewShiftsActivity.class);
        startActivity(i);
    }

    /*
    This is where you find this option list
    NavigationDrawerFragment
     */
    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
            case 1:
                navigateToAbsenceRequest();
                break;
            case 2:
                navigateToViewLeave();
                break;
            case 3:
                navigateToViewShifts(); //function in Navigation Drawer Fragment
                break;
            case 4:
                pd = ProgressDialog.show(MainActivity.this,"Update","Downloading Application",true);
                new AppdateApplicationAsync().execute();
                break;
        }
    }

    private void startSynchronization()
    {
        if (!isSynchronizationServiceRunning()) {
            final Intent i = new Intent(this, SynchronizeProcessor.class);
            startService(i);
        }
    }

    private boolean isSynchronizationServiceRunning() {
        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SynchronizeProcessor.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private class AppdateApplicationAsync extends AsyncTask<Void, String, String>
    {
        protected String doInBackground(Void... params) {

            //Get Agents in background
            String errorMessage = "";
            try
            {
                if (!DownloadOnSDcard())
                {
                    errorMessage = "Error downloading application.";
                }
            }
            catch(Exception e)
            {
                errorMessage = e.getMessage();
            }
            return errorMessage;
        }

        public boolean DownloadOnSDcard()
        {
            FileOutputStream fos = null;
            InputStream is = null;
            HttpURLConnection c = null;
            try{
                URL urls = new URL(url+apkName); // My URL

                c = (HttpURLConnection) urls.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(false);
                c.connect(); // Connection Complete here.!

                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                File file = new File(PATH); // PATH = /mnt/sdcard/download/
                if (!file.exists()) {
                    file.mkdirs();
                }

                //If file exists delete it, previously downloaded
                File f = new File(Environment.getExternalStorageDirectory() + "/download/" + apkName);
                if (f.exists()) {
                    f.delete();
                }

                File outputFile = new File(file,apkName);
                fos = new FileOutputStream(outputFile);

                is = c.getInputStream(); // Get from Server and Catch In Input Stream Object.

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1); // Write In FileOutputStream.
                }

                return true;
            }
            catch (IOException e)
            {
                return false;
            }
            finally
            {
                try
                {
                    if (fos != null)
                        fos.close();

                    if (is != null)
                        is.close();

                    if (c != null)
                        c.disconnect();
                }catch(Exception ex)
                {
                    return false;
                }

            }
        }

        protected void onProgressUpdate(String... progress) {
            //pd = ProgressDialog.show(BootstrapAuthenticatorActivity.this,"Update",progress[0],true);
        }

        protected void onPreExecute() {
            //pd = ProgressDialog.show(BootstrapAuthenticatorActivity.this,"Update","Confirming Update...",true);
        }

        protected void onPostExecute(String result) {
            pd.dismiss();
            if (!result.equals(""))
            {
                Toast.makeText(getBaseContext(), "Update Info: " + result, Toast.LENGTH_LONG).show();
            }
            else
            {
                //This is where i need to kick off install of application
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + apkName)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //also try this
                startActivityForResult(intent, 1001);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case(1001):{
                pd.dismiss();
                finish();
                break;
            }
        }
    }
}
