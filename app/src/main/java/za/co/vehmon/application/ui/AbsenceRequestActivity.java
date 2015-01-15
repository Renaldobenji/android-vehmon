package za.co.vehmon.application.ui;

import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import butterknife.InjectView;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 1/15/2015.
 */
public class AbsenceRequestActivity extends BootstrapActivity {

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.spinnerAbsenceType) protected Spinner spinnerAbsenceType;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.absence_request);
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

    private ArrayAdapter<String> createAbsenceTypeAdapter(String[] items)
    {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        return spinnerArrayAdapter;
    }
}
