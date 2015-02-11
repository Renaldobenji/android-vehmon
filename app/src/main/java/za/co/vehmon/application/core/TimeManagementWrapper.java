package za.co.vehmon.application.core;

import android.content.Context;

import java.util.Date;

import za.co.vehmon.application.datasource.TimeManagementDatasource;

/**
 * Created by Renaldo on 1/12/2015.
 */
public class TimeManagementWrapper {


    public class TimeManagementResult
    {
        private Boolean isSuccessful;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Boolean getIsSuccessful() {
            return isSuccessful;
        }

        public void setIsSuccessful(Boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        private String  errorMessage;
    }

    public TimeManagementResult ClockIn(Context context, Date clockInDateTime)
    {
        TimeManagementResult result = new TimeManagementResult();
        TimeManagementDatasource ds = new TimeManagementDatasource(context);

        long id = ds.clockIn(clockInDateTime.toString(), "Renaldo");
        if (id == -1)
        {
            result.setErrorMessage("Unable to clock in");
            result.setIsSuccessful(false);
        }

        return result;
    }

    public TimeManagementResult ClockOut(Context context, Date clockOutDateTime)
    {
        TimeManagementResult result = new TimeManagementResult();
        TimeManagementDatasource ds = new TimeManagementDatasource(context);

        long id = ds.clockIn(clockOutDateTime.toString(), "Renaldo");
        if (id == -1)
        {
            result.setErrorMessage("Unable to clock out");
            result.setIsSuccessful(false);
        }

        return result;
    }
}
