package za.co.vehmon.application.ui;

import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.LayoutInflater;
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
import za.co.vehmon.application.BootstrapServiceProvider;
import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.BootstrapService;
import za.co.vehmon.application.core.StopTimerEvent;
import za.co.vehmon.application.core.TimerService;
import za.co.vehmon.application.core.TimerTickEvent;
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


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.time_management, container, false);
        ButterKnife.inject(this, view);
        eventBus.register(this);
        return view;
    }

    @OnClick(R.id.clockIn)
    public void ClockIn(View view) {
        try {

            new SafeAsyncTask<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    final Boolean svc = serviceProvider.getService(getActivity()).clockIn(new Date());
                    return svc;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final Boolean isSuccessful) throws Exception {
                    super.onSuccess(isSuccessful);

                    if (isSuccessful == false)
                        return;

                    buttonClockIn.setVisibility(View.GONE);
                    buttonClockOut.setVisibility(View.VISIBLE);
                    startTimer();
                }
            }.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        if (!isTimerServiceRunning()) {
            final Intent i = new Intent(getActivity(), TimerService.class);
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

    @OnClick(R.id.clockout)
    public void ClockOut(View view) {
        try {
            new SafeAsyncTask<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    final Boolean svc = serviceProvider.getService(getActivity()).clockOut(new Date());
                    return svc;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final Boolean isSuccessful) throws Exception {
                    super.onSuccess(isSuccessful);

                    if (isSuccessful == false)
                        return;

                    buttonClockIn.setVisibility(View.VISIBLE);
                    buttonClockOut.setVisibility(View.GONE);
                    produceStopEvent();
                }
            }.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
