package za.co.vehmon.application.core;

/**
 * Created by Renaldo on 1/15/2015.
 */
public class AbsenceRequest {

    public String[] getAbsenceRequestTypes() {
        return AbsenceRequestTypes;
    }

    public void setAbsenceRequestTypes(String[] absenceRequestTypes) {
        AbsenceRequestTypes = absenceRequestTypes;
    }

    public String[] AbsenceRequestTypes;

    public AbsenceRequest()
    {
        String[] absenceTypes = new String[5];
        absenceTypes[0] = "Sick Leave";
        absenceTypes[1] = "Annual Leave";
        absenceTypes[2] = "Family Leave";
        absenceTypes[3] = "Maternity Leave";
        absenceTypes[4] = "MIA Leave";

        this.setAbsenceRequestTypes(absenceTypes);
    }
}
