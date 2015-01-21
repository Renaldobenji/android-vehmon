package za.co.vehmon.application.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import javax.inject.Inject;

import za.co.vehmon.application.BootstrapServiceProvider;
import za.co.vehmon.application.Injector;
import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.authenticator.LogoutService;
import za.co.vehmon.application.core.VehmonService;

/**
 * Created by Renaldo on 1/21/2015.
 */
public class MessageListFragment extends ItemListFragment<String> {

    @Inject protected VehmonServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return 0;
    }

    @Override
    protected SingleTypeAdapter<String> createAdapter(List<String> items) {
        return null;
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return null;
    }
}
