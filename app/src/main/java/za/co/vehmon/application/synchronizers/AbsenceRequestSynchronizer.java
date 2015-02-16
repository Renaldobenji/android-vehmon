package za.co.vehmon.application.synchronizers;

import android.content.Context;

import java.util.List;

import za.co.vehmon.application.core.AbsenceRequest;
import za.co.vehmon.application.datasource.AbsenceRequestDatasource;
import za.co.vehmon.application.datasource.MessagesDatasource;

/**
 * Created by Renaldo on 2/16/2015.
 */
public class AbsenceRequestSynchronizer implements ISynchronize {

    @Override
    public SynchronizedResult Synchronize(Context context) {

        SynchronizedResult result = new SynchronizedResult();
        try {
            AbsenceRequestDatasource ds = new AbsenceRequestDatasource(context);
            List<AbsenceRequest> itemsToSync = ds.GetUnsynhronizedAbsenceRequests();

            for (AbsenceRequest items : itemsToSync) {
                //Invoke webservice to sync items
            }
        }catch (Exception ex)
        {
            //Renaldo
        }

        return result;
    }

}
