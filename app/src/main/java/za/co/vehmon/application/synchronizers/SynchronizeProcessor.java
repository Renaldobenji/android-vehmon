package za.co.vehmon.application.synchronizers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import javax.inject.Inject;

import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.util.Ln;

import static za.co.vehmon.application.core.Constants.Notification.SYNC_NOTIFICATION_ID;

/**
 * Created by Renaldo on 2/16/2015.
 */
//Class this call will be a service that gets executed to sync to the server
public class SynchronizeProcessor extends Service {

    @Inject protected Bus eventBus;
    @Inject NotificationManager notificationManager;

    private List<ISynchronize> synchronizers;

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
        // Synchronizers
        setupSynchronizers();

        startForeground(SYNC_NOTIFICATION_ID, getNotification(getString(R.string.timer_running)));
    }

    private void setupSynchronizers()
    {
        this.synchronizers = new ArrayList<ISynchronize>();
        this.synchronizers.add(new AbsenceRequestSynchronizer());
        this.synchronizers.add(new MessageSynchronizer());
        this.synchronizers.add(new TimeManagementSynchronizer());
        this.synchronizers.add(new GPSSynchronizer());
    }

    @Override
    public void onDestroy() {

        // Unregister bus, since its not longer needed as the service is shutting down
        eventBus.unregister(this);

        notificationManager.cancel(SYNC_NOTIFICATION_ID);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (this.synchronizers.isEmpty())
            stopSelf();

        synchronized(this)
        {
            for(ISynchronize sync : this.synchronizers)
            {
                try {
                    updateNotification(String.format("{0} is syncing",sync.toString()));
                    sync.Synchronize(this);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        stopSelf();

        return START_NOT_STICKY;
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
                .setSmallIcon(R.drawable.ic_stat_ab_notification)
                .setContentText(message)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                        //.setContentIntent(pendingIntent)
                .getNotification();
    }
    private void updateNotification(String message) {
        notificationManager.notify(SYNC_NOTIFICATION_ID, getNotification(message));

    }




}
