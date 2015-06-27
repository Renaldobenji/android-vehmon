package za.co.vehmon.application.gps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.OperationCanceledException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.Constants;
import za.co.vehmon.application.core.GPSLogWrapper;
import za.co.vehmon.application.core.StopTimerEvent;
import za.co.vehmon.application.util.Ln;
import za.co.vehmon.application.util.SafeAsyncTask;
import za.co.vehmon.application.util.VehmonCurrentDate;

/**
 * Created by Renaldo on 6/9/2015.
 */
public class FusionTrackingService extends Service implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

    protected static final String TAG = "Vehmon Location Updates";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected String mLastUpdateTime;

    @Inject protected Bus eventBus;
    @Inject NotificationManager notificationManager;
    @Inject protected VehmonServiceProvider serviceProvider;
    private int timeTrackingID;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.inject(this);
        // Register the bus so we can send notifications.
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        // Unregister bus, since its not longer needed as the service is shutting down
        eventBus.unregister(this);
        mGoogleApiClient.disconnect();
        notificationManager.cancel(Constants.Notification.GPS_NOTIFICATION_ID);
        Ln.d("Service has been destroyed");

        super.onDestroy();
    }

    @Subscribe
    public void onStopEvent(StopTimerEvent stopEvent) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices: API.
        this.timeTrackingID = Integer.valueOf(intent.getExtras().get("TimeTrackingID").toString());
        if (this.timeTrackingID == 0)
            stopSelf();

        buildGoogleApiClient();
        //Connect
        mGoogleApiClient.connect();

        startForeground(Constants.Notification.GPS_NOTIFICATION_ID, getNotification("Vehmon Service Running"));

        return Service.START_NOT_STICKY;
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }

        startLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(final Location location) {
        if (location == null)
            return;

        StringBuilder currentLocationString = new StringBuilder();
        currentLocationString.append("CurrentLocation,")
                .append(VehmonCurrentDate.GetCurrentDate())
                .append(location.getAccuracy() + ",")
                .append(location.getAltitude()+",")
                .append(location.getBearing() + ",")
                .append(location.getLatitude() + ",")
                .append(location.getLongitude() + ",")
                .append(location.getProvider() + ",")
                .append(location.getSpeed());

        //Logger.addRecordToLog(currentLocationString.toString());

        //Save to DB
        new SafeAsyncTask<GPSLogWrapper.GPSLogResult>() {
            @Override
            public GPSLogWrapper.GPSLogResult call() throws Exception {
                serviceProvider.getService(getApplicationContext()).LogGPSCoordinates(getApplicationContext(),String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),String.valueOf(location.getAccuracy()),String.valueOf(timeTrackingID));
                return null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                }
            }

            @Override
            protected void onSuccess(final GPSLogWrapper.GPSLogResult isSuccessful) throws Exception {
                super.onSuccess(isSuccessful);
            }
        }.execute();

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Creates a notification to show in the notification bar
     *
     * @param message the message to display in the notification bar
     * @return a new {@link android.app.Notification}
     */
    private Notification getNotification(String message) {
        //final Intent i = new Intent(this, BootstrapTimerActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

        return new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.icon)
                .setContentText(message)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .getNotification();
    }
}
