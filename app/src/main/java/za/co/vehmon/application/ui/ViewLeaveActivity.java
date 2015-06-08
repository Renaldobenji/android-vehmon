package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.InjectView;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.services.LeaveRequestContract;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 6/8/2015.
 */
public class ViewLeaveActivity  extends BootstrapActivity {

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.listViewLeave) protected ListView listViewLeave;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_leave);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new SafeAsyncTask<List<LeaveRequestContract>>() {
            @Override
            public List<LeaveRequestContract> call() throws Exception {
                final List<LeaveRequestContract> absenceTypesArray = serviceProvider.getService(ViewLeaveActivity.this).GetAllLeaveRequests();
                return absenceTypesArray;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                }
            }

            @Override
            protected void onSuccess(final List<LeaveRequestContract> response) throws Exception {
                super.onSuccess(response);

                listViewLeave.setAdapter(new ViewLeaveListAdapter(getLayoutInflater(), response) );
            }
        }.execute();
    }

}
