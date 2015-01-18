package za.co.vehmon.application.core;

import java.util.Date;

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

    public AbsenceRequestResult SubmitAbsenceRequestToServer(int absenceRequestTypeID, Date fromDate, Date toDate)
    {
        AbsenceRequestResult result = new AbsenceRequestResult();

        if (fromDate == null || toDate == null)
        {
            result.setSuccessful(false);
            result.setErrorMessage("FromDate or ToDate is not set");
        }

        if (fromDate.after(toDate)) {
            result.setSuccessful(false);
            result.setErrorMessage("FromDate cannot be after ToDate");
        }

        return result;
    }
}
