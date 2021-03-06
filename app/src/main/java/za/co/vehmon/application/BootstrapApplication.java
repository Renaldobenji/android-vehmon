

package za.co.vehmon.application;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import java.util.List;

import za.co.vehmon.application.core.ShiftReportContract;
import za.co.vehmon.application.core.User;

/**
 * vehmon application
 */
public class BootstrapApplication extends Application {

    private static BootstrapApplication instance;

    /**
     * Create main application
     */
    public BootstrapApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public BootstrapApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Perform injection
        Injector.init(getRootModule(), this);

    }

    private Object getRootModule() {
        return new RootModule();
    }


    /**
     * Create main application
     *
     * @param instrumentation
     */
    public BootstrapApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static BootstrapApplication getInstance() {
        return instance;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        BootstrapApplication.user = user;
    }

    private static User user;

    public static List<ShiftReportContract> ShiftReport;
}
