package za.co.vehmon.application.core;

import android.content.Context;

import za.co.vehmon.application.datasource.GPSLogDatasource;
import za.co.vehmon.application.util.VehmonCurrentDate;

/**
 * Created by Renaldo on 2/23/2015.
 */
public class GPSLogWrapper {

    public class GPSLogResult {
        public boolean isSuccessful() {
            return IsSuccessful;
        }

        public void setSuccessful(boolean isSuccessful) {
            IsSuccessful = isSuccessful;
        }

        public String getErrorMessage() {
            return ErrorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            ErrorMessage = errorMessage;
        }

        public boolean IsSuccessful;
        public String ErrorMessage;

        public long getGPSLogID() {
            return GPSLogID;
        }

        public void setGPSLogID(long GPSLogID) {
            this.GPSLogID = GPSLogID;
        }

        public long GPSLogID;
    }

    public GPSLogResult LogGPSCoordinates(Context context, String lat, String lng, String accuracy, String timeManagementID)
    {
        GPSLogResult result = new GPSLogResult();
        GPSLogDatasource ds = new GPSLogDatasource(context);
        long id = ds.logGPSCoordinates(lat,lng,accuracy, VehmonCurrentDate.GetCurrentDate(),timeManagementID);

        result.setGPSLogID(id);

        return result;
    }
}
