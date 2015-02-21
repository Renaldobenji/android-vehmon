package za.co.vehmon.application.gps;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.drive.*;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import za.co.vehmon.application.Injector;
import za.co.vehmon.application.core.StopTimerEvent;

/**
 * Created by Renaldo on 2/21/2015.
 */
public class GPSPlayTrackingService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject protected Bus eventBus;
    @Inject NotificationManager notificationManager;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.inject(this);
        // Register the bus so we can send notifications.
        //eventBus.register(this);
        //Register to google api
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        this.mGoogleApiClient.connect();
    }

    @Subscribe
    public void onStopEvent(StopTimerEvent stopEvent) {
        mGoogleApiClient.disconnect();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
    }

    protected LocationRequest createLocationRequest() {
        Integer minTime = 1000 * 60 * 2;
        Integer fastestIntervalTime = 1000 * 10;
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(minTime);
        mLocationRequest.setFastestInterval(fastestIntervalTime);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return mLocationRequest;
    }

    protected void startLocationUpdates() {
        LocationRequest locReq = createLocationRequest();

        /*PendingIntent gpsIntent = PendingIntent.getService(this,1,null,null);
        LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, locReq, gpsIntent);*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
