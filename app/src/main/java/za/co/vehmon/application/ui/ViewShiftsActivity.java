package za.co.vehmon.application.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import za.co.vehmon.application.BootstrapApplication;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.ShiftReportContract;
import za.co.vehmon.application.services.LeaveRequestResponse;
import za.co.vehmon.application.util.SafeAsyncTask;
import za.co.vehmon.application.util.VehmonCurrentDate;

/**
 * Created by Renaldo on 6/22/2015.
 */
public class ViewShiftsActivity extends BootstrapActivity{

    @Inject protected VehmonServiceProvider serviceProvider;

    @InjectView(R.id.editTextShiftFromDate) protected EditText editTextShiftFromDate;
    @InjectView(R.id.editTextShiftToDate) protected EditText editTextShiftToDate;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Activity myActivity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_shifts);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        setDatePickerDialog();

        myActivity = this;

    }

    private void setDatePickerDialog()
    {
        //From Date Setup
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextShiftFromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //To Date Setup
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextShiftToDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private Date parseDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return format.parse(date);
    }

    @OnClick(R.id.imageButtonShiftToDate)
    public void ToDateImage(View view)
    {
        toDatePickerDialog.show();
    }

    @OnClick(R.id.imageButtonShiftFromDate)
    public void FromDateImage(View view)
    {
        fromDatePickerDialog.show();
    }

    @OnClick(R.id.buttonShiftSubmit)
    public void ViewLeaveRequestSubmit(View view) {

        try {
            final Date fromDate = parseDate(editTextShiftFromDate.getText().toString());
            final Date toDate = parseDate(editTextShiftToDate.getText().toString());

            if (fromDate == null || toDate == null) {
                Toast.makeText(getApplicationContext(), "FromDate or ToDate is not set",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (fromDate.after(toDate)) {
                Toast.makeText(getApplicationContext(), "FromDate cannot be after ToDate",
                        Toast.LENGTH_LONG).show();
                return;
            }

            final ProgressDialog barProgressDialog = ProgressDialog.show(ViewShiftsActivity.this,"Please wait...", "Submitting Request",true);
            new SafeAsyncTask<List<ShiftReportContract>>() {
                @Override
                public List<ShiftReportContract> call() throws Exception {
                    final List<ShiftReportContract> svc = serviceProvider.getService(ViewShiftsActivity.this).GetUserShifts(VehmonCurrentDate.GetCurrentDate(fromDate),VehmonCurrentDate.GetCurrentDate(toDate));
                    return svc;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                    barProgressDialog.dismiss();
                }

                @Override
                protected void onSuccess(final List<ShiftReportContract> result) throws Exception {
                    super.onSuccess(result);
                    barProgressDialog.dismiss();
                    if (result == null || result.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "No Shifts for the selected criteria",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    BootstrapApplication app = (BootstrapApplication)myActivity.getApplicationContext();
                    app.ShiftReport = result;

                    startActivity(new Intent(myActivity,ViewShiftsDetail.class));
                }
            }.execute();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ButterKnife.inject(this);
    }
}
