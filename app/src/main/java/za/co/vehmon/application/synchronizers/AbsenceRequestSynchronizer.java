package za.co.vehmon.application.synchronizers;

import android.content.Context;
import android.os.OperationCanceledException;

import java.util.List;

import javax.inject.Inject;

import za.co.vehmon.application.VehmonServiceProvider;
import za.co.vehmon.application.core.AbsenceRequest;
import za.co.vehmon.application.datasource.AbsenceRequestDatasource;
import za.co.vehmon.application.datasource.MessagesDatasource;
import za.co.vehmon.application.services.LeaveRequestResponse;
import za.co.vehmon.application.util.SafeAsyncTask;

/**
 * Created by Renaldo on 2/16/2015.
 */
public class AbsenceRequestSynchronizer implements ISynchronize {

    @Inject protected VehmonServiceProvider serviceProvider;

    @Override
    public SynchronizedResult Synchronize(final Context context) {

        SynchronizedResult result = new SynchronizedResult();
        result.setSynchronizedSuccessful(true);
        try {
            final AbsenceRequestDatasource ds = new AbsenceRequestDatasource(context);
            List<AbsenceRequest> itemsToSync = ds.GetUnsynhronizedAbsenceRequests();

            for (final AbsenceRequest items : itemsToSync) {

                try {
                    new SafeAsyncTask<LeaveRequestResponse>() {
                        @Override
                        public LeaveRequestResponse call() throws Exception {
                            final LeaveRequestResponse svc = serviceProvider.getService(context).SyncLeaveRequestToServer(items.getFromDate(),items.getToDate(), items.getLeaveTypeID());
                            return svc;
                        }

                        @Override
                        protected void onException(final Exception e) throws RuntimeException {
                            super.onException(e);
                            if (e instanceof OperationCanceledException) {
                            }
                        }

                        @Override
                        protected void onSuccess(final LeaveRequestResponse response) throws Exception {
                            super.onSuccess(response);
                            if (response.RequestStatus == "Success")
                            {
                                ds.updateSynced(items.getId());
                            }
                        }
                    }.execute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }catch (Exception ex)
        {
            result.setSynchronizedSuccessful(false);
            result.setSynchronizedMessage(ex.toString());
        }

        return result;
    }

}
