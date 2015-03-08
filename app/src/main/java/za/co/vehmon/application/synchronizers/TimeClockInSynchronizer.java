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
public class TimeClockInSynchronizer implements ISynchronize {
    @Override
    public SynchronizedResult Synchronize(final Context context,final VehmonServiceProvider serviceProvider) {
        SynchronizedResult result = new SynchronizedResult();
        result.setSynchronizedSuccessful(true);

        final TimeManagementDatasource ds = new TimeManagementDatasource(context);
        List<TimeManagement> clockIns = ds.GetUnsynchronizedClockIn();

        for(final TimeManagement times: clockIns)
        {
            try {
                new SafeAsyncTask<ShiftResponse>() {
                    @Override
                    public ShiftResponse call() throws Exception {
                        String clockInTime = times.getClockInTime();
                        VehmonService service = serviceProvider.getService(context);
                        ShiftResponse svc = service.SyncStartShiftToServer(clockInTime);
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

                        if (response.LogState.equals("4"))
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
}
