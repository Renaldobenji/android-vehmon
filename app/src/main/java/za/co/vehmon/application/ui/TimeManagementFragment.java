package za.co.vehmon.application.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import za.co.vehmon.application.BootstrapServiceProvider;
import za.co.vehmon.application.Injector;
import za.co.vehmon.application.R;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;

/**
 * Created by Renaldo on 1/11/2015.
 */
public class TimeManagementFragment extends android.support.v4.app.Fragment{

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.time_management, container, false);
    }
}
