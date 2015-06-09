package za.co.vehmon.application.gps;

import android.accounts.AccountsException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.OperationCanceledException;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.Constants;
import za.co.vehmon.application.core.GPSLogWrapper;
import za.co.vehmon.application.core.StopTimerEvent;
import za.co.vehmon.application.ui.BootstrapTimerActivity;
import za.co.vehmon.application.util.Ln;
import za.co.vehmon.application.util.SafeAsyncTask;
import za.co.vehmon.application.util.VehmonCurrentDate;

/**
 * Created by Renaldo on 2/18/2015.
 */
public class GPSTrackingService extends Service implements LocationListener {

    private LocationManager locationManager;
    private String provider;
    private long minTime = 1000 * 60 * 2; //5 Minute updates
    private int minDistance = 1000 * 2 ; //5 Kilometers
    private final int TWO_MINUTES = 1000 * 60 * 2;
    private Timer sourceGPSTimer;
    private int timeTrackingID;
    @Inject protected Bus eventBus;
    @Inject NotificationManager notificationManager;
    @Inject protected VehmonServiceProvider serviceProvider;

    private Location currentBestLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.inject(this);
        // Register the bus so we can send notifications.
        eventBus.register(this);
    }

    @Subscribe
    public void onStopEvent(StopTimerEvent stopEvent) {
        if (this.locationManager != null)
        {
            this.locationManager.removeUpdates(this);
        }
        if (sourceGPSTimer != null)
            sourceGPSTimer.cancel();
        stopSelf();
    }

    @Override
    public void onDestroy() {

        // Unregister bus, since its not longer needed as the service is shutting down
        eventBus.unregister(this);

        notificationManager.cancel(Constants.Notification.GPS_NOTIFICATION_ID);

        Ln.d("Service has been destroyed");

        super.onDestroy();
    }

    private void setupGPSTimer()
    {
        sourceGPSTimer = new Timer("GPSTimer", true);
        sourceGPSTimer.scheduleAtFixedRate
        (
                new TimerTask() {
                    public void run() {
                        try {
                            onLocationChanged(locationManager.getLastKnownLocation(provider));
                        } catch (Exception e) {

                        }

                    }
                },0,minTime
        );
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        try {
            this.timeTrackingID = Integer.valueOf(intent.getExtras().get("TimeTrackingID").toString());
            if (this.timeTrackingID == 0)
                stopSelf();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);//Only use gps coordinates

            provider = locationManager.getBestProvider(criteria, false);

            if (provider == null) {
                //No location provider enabled, this is a problem.....
                stopSelf();
            }

            //The last known location of this provider
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null)
                this.onLocationChanged(location);

            if (provider != null && provider.length() > 0) {
                //Provider found
                locationManager.requestLocationUpdates(provider, minTime, minDistance, this);
            }
            notifyTimerRunning();

            startForeground(Constants.Notification.GPS_NOTIFICATION_ID, getNotification("Vehmon Service Running"));
            setupGPSTimer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //Something went wrong, exit
            stopSelf();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onLocationChanged(final Location location) {
        // TODO Auto-generated method stub
        StringBuilder currentBestString = new StringBuilder();
        StringBuilder currentLocationString = new StringBuilder();

        if (location == null)
            return;

        if (isBetterLocation(location,currentBestLocation))
        {
            currentBestLocation = location;
        }
        //Log coordinates to the DB
        if (currentBestLocation != null) {
            currentBestString.append("CurrentBest,").append(VehmonCurrentDate.GetCurrentDate()).append(currentBestLocation.getAccuracy() + ",").append(currentBestLocation.getAltitude() + ",")
                    .append(currentBestLocation.getBearing() + ",")
                    .append(currentBestLocation.getLatitude() + ",").append(currentBestLocation.getLongitude() + ",").append(currentBestLocation.getProvider() + ",")
                    .append(currentBestLocation.getSpeed());
        }

        currentLocationString.append("CurrentLocation,").append(VehmonCurrentDate.GetCurrentDate()).append(location.getAccuracy() + ",").append(location.getAltitude()+",")
                .append(location.getBearing() + ",")
                .append(location.getLatitude() + ",").append(location.getLongitude() + ",").append(location.getProvider() + ",")
                .append(location.getSpeed());

        if (currentBestString.length() > 0)
            Logger.addRecordToLog(currentBestString.toString());

        Logger.addRecordToLog(currentLocationString.toString());

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

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Location getBestLastKnownLocation()
    {
        float bestAccuracy = 0;
        Location bestResult = null;
        long bestTime = 0;

        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider: matchingProviders) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                }
                else if (time < minTime &&
                        bestAccuracy == Float.MAX_VALUE && time > bestTime){
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        return bestResult;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
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
                        //.setContentIntent(pendingIntent)
                .getNotification();
    }

    private void notifyTimerRunning() {
        updateNotification("Vehmon Service Running");
    }


    private void updateNotification(String message) {
        notificationManager.notify(Constants.Notification.GPS_NOTIFICATION_ID, getNotification(message));

    }
}
