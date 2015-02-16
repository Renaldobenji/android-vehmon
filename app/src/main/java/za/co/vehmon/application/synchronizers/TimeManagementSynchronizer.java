package za.co.vehmon.application.synchronizers;

import android.content.Context;

import java.util.List;

import za.co.vehmon.application.core.TimeManagement;
import za.co.vehmon.application.datasource.TimeManagementDatasource;

/**
 * Created by Renaldo on 2/16/2015.
 */
public class TimeManagementSynchronizer implements ISynchronize {
    @Override
    public SynchronizedResult Synchronize(Context context) {

        SynchronizedResult result = new SynchronizedResult();

        try {
            TimeManagementDatasource ds = new TimeManagementDatasource(context);
            synchronizeClockIns(ds);
            synchronizeClockOuts(ds);
        }
        catch (Exception ex)
        {

        }
        return result;
    }

    private SynchronizedResult synchronizeClockIns(TimeManagementDatasource ds)
    {
        SynchronizedResult result = new SynchronizedResult();
        List<TimeManagement> clockIns = ds.GetUnsynchronizedClockIn();

        for(TimeManagement times: clockIns)
        {

        }

        return result;
    }

    private SynchronizedResult synchronizeClockOuts(TimeManagementDatasource ds)
    {
        SynchronizedResult result = new SynchronizedResult();
        List<TimeManagement> clockOuts = ds.GetUnsynchronizedClockOut();

        for(TimeManagement times: clockOuts)
        {

        }

        return result;
    }
}
