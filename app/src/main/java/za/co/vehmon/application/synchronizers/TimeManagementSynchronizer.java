package za.co.vehmon.application.synchronizers;

import android.content.Context;
import android.os.OperationCanceledException;

import java.util.List;

import javax.inject.Inject;

import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.TimeManagement;
import za.co.vehmon.application.datasource.TimeManagementDatasource;
import za.co.vehmon.application.services.ShiftResponse;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 2/16/2015.
 */
public class TimeManagementSynchronizer implements ISynchronize {

    @Inject protected VehmonServiceProvider serviceProvider;

    @Override
    public SynchronizedResult Synchronize(Context context) {

        SynchronizedResult result = new SynchronizedResult();

        try {
            TimeManagementDatasource ds = new TimeManagementDatasource(context);
            synchronizeClockIns(context,ds);
            synchronizeClockOuts(context,ds);
        }
        catch (Exception ex)
        {

        }
        return result;
    }

    private SynchronizedResult synchronizeClockIns(final Context context, final TimeManagementDatasource ds)
    {
        SynchronizedResult result = new SynchronizedResult();
        List<TimeManagement> clockIns = ds.GetUnsynchronizedClockIn();

        for(final TimeManagement times: clockIns)
        {
            try {
                new SafeAsyncTask<ShiftResponse>() {
                    @Override
                    public ShiftResponse call() throws Exception {
                        final ShiftResponse svc = serviceProvider.getService(context).SyncStartShiftToServer(times.getInLat(),times.getInLng(), times.getClockInTime());
                        return svc;
                    }

                    @Override
                    protected void onException(final Exception e) throws RuntimeException {
                        super.onException(e);
                        if (e instanceof OperationCanceledException) {
                        }
                    }

                    @Override
                    protected void onSuccess(final ShiftResponse response) throws Exception {
                        super.onSuccess(response);
                        if (response.LogState == "Success")
                        {
                            ds.updateClockInSynced(times.getId(),response.ShiftId);
                        }
                    }
                }.execute();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }

    private SynchronizedResult synchronizeClockOuts(final Context context, final TimeManagementDatasource ds)
    {
        SynchronizedResult result = new SynchronizedResult();
        List<TimeManagement> clockOuts = ds.GetUnsynchronizedClockOut();

        for(final TimeManagement times: clockOuts)
        {
            try {
                new SafeAsyncTask<ShiftResponse>() {
                    @Override
                    public ShiftResponse call() throws Exception {
                        final ShiftResponse svc = serviceProvider.getService(context).SyncEndShiftToServer(times.getShiftID(), times.getClockOutTime());
                        return svc;
                    }

                    @Override
                    protected void onException(final Exception e) throws RuntimeException {
                        super.onException(e);
                        if (e instanceof OperationCanceledException) {
                        }
                    }

                    @Override
                    protected void onSuccess(final ShiftResponse response) throws Exception {
                        super.onSuccess(response);
                        if (response.LogState == "Success")
                        {
                            ds.updateClockOutSynced(times.getId());
                        }
                    }
                }.execute();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }
}
