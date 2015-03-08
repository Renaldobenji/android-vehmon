package za.co.vehmon.application.synchronizers;

import android.content.Context;
import android.os.OperationCanceledException;

import java.util.List;

import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.TimeManagement;
import za.co.vehmon.application.core.VehmonService;
import za.co.vehmon.application.datasource.TimeManagementDatasource;
import za.co.vehmon.application.services.ShiftResponse;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 3/8/2015.
 */
public class TimeClockOutSynchronizer implements ISynchronize {
    @Override
    public SynchronizedResult Synchronize(final Context context,final VehmonServiceProvider serviceProvider) {
        SynchronizedResult result = new SynchronizedResult();
        result.setSynchronizedSuccessful(true);

        final TimeManagementDatasource ds = new TimeManagementDatasource(context);
        List<TimeManagement> clockOuts = ds.GetUnsynchronizedClockOut();

        for(final TimeManagement times: clockOuts)
        {
            try {
                new SafeAsyncTask<ShiftResponse>() {
                    @Override
                    public ShiftResponse call() throws Exception {
                        if (times.getShiftID() == null || times.getShiftID().isEmpty() || times.getShiftID().equals("0"))
                            return null;

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
                        if (response == null)
                            return;

                        if (response.LogState == "4")
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
