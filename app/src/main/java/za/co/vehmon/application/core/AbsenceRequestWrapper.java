package za.co.vehmon.application.core;

import android.content.Context;

import java.util.Date;

import za.co.vehmon.application.datasource.AbsenceRequestDatasource;
import za.co.vehmon.application.ui.AbsenceRequestActivity;

/**
 * Created by Renaldo on 1/15/2015.
 */
public class AbsenceRequestWrapper {

    public class AbsenceRequestResult
    {
        public AbsenceRequestResult()
        {
            setSuccessful(true);
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }

        public void setSuccessful(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        public boolean isSuccessful;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String errorMessage;
    }

    public String[] FetchAbsenceTypes()
    {
        return new AbsenceRequest().getAbsenceRequestTypes();
    }

    public AbsenceRequestResult SubmitAbsenceRequest(Context context, int absenceRequestTypeID, Date fromDate, Date toDate)
    {
        String userID = "Renaldob";
        AbsenceRequestResult result = new AbsenceRequestResult();
        AbsenceRequestDatasource ds = new AbsenceRequestDatasource(context);
        if (fromDate == null || toDate == null)
        {
            result.setSuccessful(false);
            result.setErrorMessage("FromDate or ToDate is not set");
            return result;
        }

        if (fromDate.after(toDate)) {
            result.setSuccessful(false);
            result.setErrorMessage("FromDate cannot be after ToDate");
            return result;
        }

        //String fromDate, String toDate, String userID, String leaveTypeID
        if (ds.InsertAbsenceRequest(fromDate.toString(),toDate.toString(),userID,Integer.toString(absenceRequestTypeID)) == -1)
        {
            //Error inserting into table
            result.setSuccessful(false);
            result.setErrorMessage("Error inserting into table");
        }


        return result;
    }
}
