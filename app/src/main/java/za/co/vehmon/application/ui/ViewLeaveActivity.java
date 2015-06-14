package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.services.LeaveRequestContract;
import za.co.vehmon.application.services.LeaveRequestResponse;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 6/8/2015.
 */
public class ViewLeaveActivity  extends BootstrapActivity {

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.listViewLeave) protected ListView listViewLeave;
    protected TextView tAvailableBalance;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_leave);
        ButterKnife.inject(this);

        tAvailableBalance=(TextView)findViewById(R.id.tvAvailableBalanceValue);

        final ProgressDialog barProgressDialog = ProgressDialog.show(this,"Please wait...", "Fetching Leave",true);

        new SafeAsyncTask<LeaveRequestResponse>() {
            @Override
            public LeaveRequestResponse call() throws Exception {
                final LeaveRequestResponse absenceTypesArray = serviceProvider.getService(ViewLeaveActivity.this).GetAllLeaveRequests();
                return absenceTypesArray;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                }
                barProgressDialog.dismiss();
            }

            @Override
            protected void onSuccess(final LeaveRequestResponse response) throws Exception {
                super.onSuccess(response);
                tAvailableBalance.setText(response.AvailableBalance + " Days");
                listViewLeave.setAdapter(new ViewLeaveListAdapter(getLayoutInflater(), response.LeaveRequests));
                barProgressDialog.dismiss();
            }
        }.execute();
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

}
