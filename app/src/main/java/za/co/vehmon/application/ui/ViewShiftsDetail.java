package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.ShiftReportContract;
import za.co.vehmon.application.services.LeaveRequestResponse;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 6/22/2015.
 */
public class ViewShiftsDetail extends BootstrapActivity{

    @Inject protected VehmonServiceProvider serviceProvider;

    @InjectView(R.id.listViewShifts) protected ListView listViewShifts;
    protected TextView tvTotalHours;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_shifts_details);
        ButterKnife.inject(this);

        tvTotalHours=(TextView)findViewById(R.id.tvTotalHoursValue);

        BootstrapApplication app = (BootstrapApplication)this.getApplicationContext();
        tvTotalHours.setText(GetTotalHoursWorked(app.ShiftReport));

        listViewShifts.setAdapter(new ViewShftsListAdapter(getLayoutInflater(), app.ShiftReport));
    }

    @Override
    public void onBackPressed() {
        BootstrapApplication app = (BootstrapApplication)this.getApplicationContext();
        app.ShiftReport = null;
        finish();
        return;
    }

    private String GetTotalHoursWorked(List<ShiftReportContract> report)
    {
        double totalMinutes = 0;
        for (ShiftReportContract items : report)
        {
            try {
                totalMinutes += Double.parseDouble(items.MinutesWorked);
            }catch (Exception e)
            {
                totalMinutes +=0;
            }
        }

        if (totalMinutes == 0)
            return "0";

        return String.format("%.2f", totalMinutes / 60) ;
    }

}
