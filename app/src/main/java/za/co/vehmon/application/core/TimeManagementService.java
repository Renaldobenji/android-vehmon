package za.co.vehmon.application.core;

import java.util.Date;

/**
 * Created by Renaldo on 1/12/2015.
 */
public interface TimeManagementService {

    TimeManagementWrapper ClockOut(Date date);

    TimeManagementWrapper ClockIn(Date date);

    ShiftReportContract GetUserShifts(String token, String startDate, String endDate);
}
