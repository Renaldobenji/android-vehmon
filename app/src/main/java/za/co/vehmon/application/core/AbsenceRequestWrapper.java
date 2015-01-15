package za.co.vehmon.application.core;

/**
 * Created by Renaldo on 1/15/2015.
 */
public class AbsenceRequestWrapper {

    public String[] FetchAbsenceTypes()
    {
        return new AbsenceRequest().getAbsenceRequestTypes();
    }
}
