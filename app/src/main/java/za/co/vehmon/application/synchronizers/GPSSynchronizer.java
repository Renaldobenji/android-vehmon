package za.co.vehmon.application.synchronizers;

import android.content.Context;
import android.os.OperationCanceledException;

import java.util.List;

import javax.inject.Inject;

import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.GPSLog;
import za.co.vehmon.application.datasource.GPSLogDatasource;
import za.co.vehmon.application.services.Coordinate;
import za.co.vehmon.application.services.MessageResponse;
import za.co.vehmon.application.services.ShiftResponse;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 3/7/2015.
 */
public class GPSSynchronizer implements ISynchronize {

    @Override
    public SynchronizedResult Synchronize(final Context context,final VehmonServiceProvider serviceProvider) {

        SynchronizedResult result = new SynchronizedResult();
        result.setSynchronizedSuccessful(true);
        final GPSLogDatasource gpsDS = new GPSLogDatasource(context);
        //Fetch all UnSynced GPS Coordinates, including the Shift ID
        List<GPSLog> gpsLogs = gpsDS.GetUnsynchronizedGPSLogs(context);

        for(final GPSLog gps : gpsLogs)
        {
            try {
                new SafeAsyncTask<ShiftResponse>() {
                    @Override
                    public ShiftResponse call() throws Exception {
                        if (gps.getShiftID() == 0 )
                            return null;

                        String coords = new StringBuilder().append(gps.getLat()).append(",").append(gps.getLng()).append(",").append(gps.getDate()).toString();
                        final ShiftResponse svc = serviceProvider.getService(context).SendGPSLogToServer(String.valueOf(gps.getShiftID()),coords);
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
                            gpsDS.UpdateGPSLog(gps.getId());
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
