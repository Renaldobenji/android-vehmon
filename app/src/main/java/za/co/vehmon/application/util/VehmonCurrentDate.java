package za.co.vehmon.application.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Renaldo on 2/14/2015.
 */
public class VehmonCurrentDate {

    public static String GetCurrentDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        return sdf.format(new Date());
    }

}
