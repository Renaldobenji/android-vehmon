

package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
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
import com.squareup.otto.Subscribe;

import javax.inject.Inject;




/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link za.co.vehmon.application.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    @Inject protected VehmonServiceProvider serviceProvider;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        startSynchronization();
        checkAuth();

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


    private void initScreen() {
        Ln.d("Foo");
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new CarouselFragment())
                .commit();

        if (!userHasAuthenticated) {
            navigateToLogin();
        }
    }

    private void checkAuth() {
        new SafeAsyncTask<UserTokenValidationResponse>() {

            @Override
            public UserTokenValidationResponse call() throws Exception {
                UserTokenValidationResponse response = serviceProvider.getService(MainActivity.this).RenewUserToken();
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
                userHasAuthenticated = (hasAuthenticated.UserTokenState.equals("Valid"));
                if (userHasAuthenticated)
                {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constants.VehmonSharedPrefs.name, Context.MODE_PRIVATE);
                    String username = sharedPref.getString("username","");
                    User user = new User();
                    user.setUsername(username);
                    final BootstrapApplication globalApplication = (BootstrapApplication)getApplicationContext();
                    globalApplication.setUser(user);
                }
                initScreen();
            }
        }.execute();
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

    private void navigateToLogin() {
        final Intent i = new Intent(this, BootstrapAuthenticatorActivity.class);
        startActivity(i);
    }

    private void navigateToAbsenceRequest()
    {
        final Intent i = new Intent(this,AbsenceRequestActivity.class);
        startActivity(i);
    }

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
}
