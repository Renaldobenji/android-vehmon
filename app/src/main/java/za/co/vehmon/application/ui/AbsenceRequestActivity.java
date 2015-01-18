package za.co.vehmon.application.ui;

import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.AbsenceRequestWrapper;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 1/15/2015.
 */
public class AbsenceRequestActivity extends BootstrapActivity {

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @InjectView(R.id.spinnerAbsenceType) protected Spinner spinnerAbsenceType;
    @InjectView(R.id.editTextFromDate) protected EditText editTextFromDate;
    @InjectView(R.id.editTextToDate) protected EditText editTextToDate;
    //@InjectView(R.id.imageButtonToDate) protected ImageButton imageButtonToDate;
    //@InjectView(R.id.imageButtonFromDate) protected ImageButton imageButtonFromDate;
    //@InjectView(R.id.buttonAbsenceSubmit) protected Button buttonAbsenceSubmit;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.absence_request);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        setDatePickerDialog();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new SafeAsyncTask<String[]>() {
            @Override
            public String[] call() throws Exception {

                final String[] absenceTypesArray = serviceProvider.getService(AbsenceRequestActivity.this).FetchAbsenceTypes();
                return absenceTypesArray;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                }
            }

            @Override
            protected void onSuccess(final String[] isSuccessful) throws Exception {
                super.onSuccess(isSuccessful);

                spinnerAbsenceType.setAdapter(createAbsenceTypeAdapter(isSuccessful));
            }
        }.execute();
    }

    private void setDatePickerDialog()
    {
        //From Date Setup
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextFromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //To Date Setup
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextToDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private ArrayAdapter<String> createAbsenceTypeAdapter(String[] items)
    {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, items);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return spinnerArrayAdapter;
    }

    @OnClick(R.id.imageButtonToDate)
    public void ToDateImage(View view)
    {
        toDatePickerDialog.show();
    }

    @OnClick(R.id.imageButtonFromDate)
    public void FromDateImage(View view)
    {
        fromDatePickerDialog.show();
    }

    private Date parseDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return format.parse(date);
    }


    @OnClick(R.id.buttonAbsenceSubmit)
    public void AbsenceRequestSubmit(View view)
    {
        final int absenceRequestTypeID = 1;

        try {
            final Date fromDate = parseDate(editTextFromDate.getText().toString());
            final Date toDate = parseDate(editTextToDate.getText().toString());

            new SafeAsyncTask<AbsenceRequestWrapper.AbsenceRequestResult>() {
                @Override
                public AbsenceRequestWrapper.AbsenceRequestResult call() throws Exception {

                    AbsenceRequestWrapper.AbsenceRequestResult result = serviceProvider.getService(AbsenceRequestActivity.this).SubmitAbsenceRequest(absenceRequestTypeID,fromDate,toDate);
                    return result;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final AbsenceRequestWrapper.AbsenceRequestResult result) throws Exception {
                    super.onSuccess(result);
                }
            }.execute();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
