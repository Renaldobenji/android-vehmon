package za.co.vehmon.application.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import za.co.vehmon.application.R;
import za.co.vehmon.application.core.PauseTimerEvent;
import za.co.vehmon.application.core.ResumeTimerEvent;
import za.co.vehmon.application.core.StopTimerEvent;
import za.co.vehmon.application.core.TimerPausedEvent;
import za.co.vehmon.application.core.TimerService;
import za.co.vehmon.application.core.TimerTickEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.InjectView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BootstrapTimerActivity extends BootstrapFragmentActivity implements View.OnClickListener {

    @Inject Bus eventBus;

    @InjectView(R.id.chronometer) protected TextView chronometer;
    @InjectView(R.id.start) protected Button start;
    @InjectView(R.id.stop) protected Button stop;
    @InjectView(R.id.pause) protected Button pause;
    @InjectView(R.id.resume) protected Button resume;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bootstrap_timer);

        setTitle(R.string.title_timer);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause.setOnClickListener(this);
        resume.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.start:
               //startTimer();
                break;
            case R.id.stop:
               //produceStopEvent();
                break;
            case R.id.pause:
                //producePauseEvent();
                break;
            case R.id.resume:
                //produceResumeEvent();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            // Source:
            // http://developer.android.com/training/implementing-navigation/ancestral.html
            // This is the home button in the top left corner of the screen.
            case android.R.id.home:
                final Intent upIntent = NavUtils.getParentActivityIntent(this);
                // If parent is not properly defined in AndroidManifest.xml upIntent will be null
                // TODO hanlde upIntent == null
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
                default:
                return super.onOptionsItemSelected(item);
        }
    }


}
