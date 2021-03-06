package za.co.vehmon.application.ui;

import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.StopTimerEvent;
import za.co.vehmon.application.core.TimeManagementWrapper;
import za.co.vehmon.application.core.TimerService;
import za.co.vehmon.application.core.TimerTickEvent;
import za.co.vehmon.application.gps.FusionTrackingService;
import za.co.vehmon.application.gps.GPSTrackingService;
import za.co.vehmon.application.synchronizers.SynchronizeProcessor;
import za.co.vehmon.application.util.SafeAsyncTask;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Renaldo on 1/11/2015.
 */
public class TimeManagementFragment extends android.support.v4.app.Fragment{

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    @Inject Bus eventBus;

    @InjectView(R.id.clockIn) protected Button buttonClockIn;
    @InjectView(R.id.clockout) protected Button buttonClockOut;
    @InjectView(R.id.timetrackingchronometer) protected TextView timetrackingchronometer;

    private static final String FORCE_REFRESH = "forceRefresh";
    private static int TIMETRACKINGID = 0;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu optionsMenu, final MenuInflater inflater) {
        inflater.inflate(R.menu.bootstrap, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (!isUsable()) {
            return false;
        }
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Force a refresh of the items displayed ignoring any cached items
     */
    protected void forceRefresh() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(FORCE_REFRESH, true);
        refresh(bundle);
    }
    private ActionBarActivity getActionBarActivity() {
        return ((ActionBarActivity) getActivity());
    }
    /**
     * Refresh the fragment's list
     */
    public void refresh() {
        refresh(null);
    }

    private void refresh(final Bundle args) {
        if (!isUsable()) {
            return;
        }

        getActionBarActivity().setSupportProgressBarIndeterminateVisibility(true);
    }

    /**
     * Is this fragment still part of an activity and usable from the UI-thread?
     *
     * @return true if usable on the UI-thread, false otherwise
     */
    protected boolean isUsable() {
        return getActivity() != null;
    }

    private void logout() {
        logoutService.logout(new Runnable() {
            @Override
            public void run() {
                // Calling a refresh will force the service to look for a logged in user
                // and when it finds none the user will be requested to log in again.
                forceRefresh();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.time_management, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Display clock out information
        if (isTimerServiceRunning()) {
            buttonClockIn.setVisibility(View.GONE);
            buttonClockOut.setVisibility(View.VISIBLE);
        }

        eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    @OnClick(R.id.clockIn)
    public void ClockIn(View view) {

        //GPS must be enabled
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlertToUser();
            return;
        }

        try {

            new SafeAsyncTask<TimeManagementWrapper.TimeManagementResult>() {
                @Override
                public TimeManagementWrapper.TimeManagementResult call() throws Exception {
                    final TimeManagementWrapper.TimeManagementResult svc = serviceProvider.getService(getActivity()).ClockIn(getActivity(), new Date());
                    return svc;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final TimeManagementWrapper.TimeManagementResult response) throws Exception {
                    super.onSuccess(response);

                    buttonClockIn.setVisibility(View.GONE);
                    buttonClockOut.setVisibility(View.VISIBLE);
                    TIMETRACKINGID = (int)response.getTimeTrackingID();
                    startTracking(response.getTimeTrackingID());
                    startTimer();
                    startSynchronization();
                }
            }.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS is disabled. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void startTimer() {
        if (!isTimerServiceRunning()) {
            final Intent i = new Intent(getActivity(), TimerService.class);
            getActivity().startService(i);
        }
    }

    private void startTracking(long timeTrackingID)
    {
        if (!isTrackingServiceRunning()) {
            //final Intent i = new Intent(getActivity(), GPSTrackingService.class);
            final Intent i = new Intent(getActivity(), FusionTrackingService.class);
            i.putExtra("TimeTrackingID",timeTrackingID);
            getActivity().startService(i);
        }
    }

    /**
     * Called by {@link Bus} when a tick event occurs.
     *
     * @param event The event
     */
    @Subscribe
    public void onTickEvent(final TimerTickEvent event) {

        setFormattedTime(event.getMillis());

    }

    /**
     * Formats the time to look like "HH:MM:SS"
     *
     * @param millis The number of elapsed milliseconds
     * @return A formatted time value
     */
    public static String formatTime(final long millis) {
        //TODO does not support hour>=100 (4.1 days)
        return String.format("%02d:%02d:%02d",
                millis / (1000 * 60 * 60),
                (millis / (1000 * 60)) % 60,
                (millis / 1000) % 60
        );
    }

    /**
     * Sets the formatted time
     *
     * @param millis the elapsed time
     */
    private void setFormattedTime(long millis) {
        final String formattedTime = formatTime(millis);
        timetrackingchronometer.setText(formattedTime);
    }
    /**
     * Posts a {@link za.co.vehmon.application.core.StopTimerEvent} message to the {@link Bus}
     */
    private void produceStopEvent() {
        eventBus.post(new StopTimerEvent());
    }

    @Subscribe
    public void onStopEvent(final StopTimerEvent event) {
        setFormattedTime(0); // Since its stopped, zero out the timer.
    }
    /**
     * Checks to see if the timer service is running or not.
     *
     * @return true if the service is running otherwise false.
     */
    private boolean isTimerServiceRunning() {
        final ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (TimerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isTrackingServiceRunning() {
        final ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GPSTrackingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.clockout)
    public void ClockOut(View view) {
        try {
            new SafeAsyncTask<TimeManagementWrapper.TimeManagementResult>() {
                @Override
                public TimeManagementWrapper.TimeManagementResult call() throws Exception {
                    final TimeManagementWrapper.TimeManagementResult svc = serviceProvider.getService(getActivity()).ClockOut(getActivity(),new Date(),TIMETRACKINGID);
                    return null;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final TimeManagementWrapper.TimeManagementResult isSuccessful) throws Exception {
                    super.onSuccess(isSuccessful);

                    buttonClockIn.setVisibility(View.VISIBLE);
                    buttonClockOut.setVisibility(View.GONE);
                    produceStopEvent();
                    startSynchronization();
                }
            }.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void startSynchronization()
    {
        if (!isSynchronizationServiceRunning()) {
            final Intent i = new Intent(getActivity(), SynchronizeProcessor.class);
            getActivity().startService(i);
        }
    }

    private boolean isSynchronizationServiceRunning() {
        final ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SynchronizeProcessor.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
