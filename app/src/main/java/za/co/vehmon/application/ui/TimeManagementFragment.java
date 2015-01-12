package za.co.vehmon.application.ui;

import android.accounts.AccountsException;
import android.accounts.OperationCanceledException;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 1/11/2015.
 */
public class TimeManagementFragment extends android.support.v4.app.Fragment{

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.clockIn) protected Button buttonClockIn;
    @InjectView(R.id.clockout) protected Button buttonClockOut;


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
        return view;
    }

    @OnClick(R.id.clockIn)
    public void ClockIn(View view) {
        try {

            new SafeAsyncTask<String>() {
                @Override
                public String call() throws Exception {
                    final Boolean svc = serviceProvider.getService(getActivity()).clockIn(new Date());
                    return "WhatEver";
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final String hasAuthenticated) throws Exception {
                    super.onSuccess(hasAuthenticated);
                    String s = hasAuthenticated;
                }
            }.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.clockout)
    public void ClockOut(View view) {
        try {
            new SafeAsyncTask<String>() {
                @Override
                public String call() throws Exception {
                    final Boolean svc = serviceProvider.getService(getActivity()).clockOut(new Date());
                    return "";
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    if (e instanceof OperationCanceledException) {
                    }
                }

                @Override
                protected void onSuccess(final String hasAuthenticated) throws Exception {
                    super.onSuccess(hasAuthenticated);
                }
            }.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
