package za.co.vehmon.application.synchronizers;

import android.content.Context;

import za.co.vehmon.application.VehmonServiceProvider;

/**
 * Created by Renaldo on 2/16/2015.
 */
public interface ISynchronize {

    public SynchronizedResult Synchronize(Context context,VehmonServiceProvider serviceProvider);
}
