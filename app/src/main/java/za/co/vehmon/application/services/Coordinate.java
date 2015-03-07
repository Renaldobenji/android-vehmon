package za.co.vehmon.application.services;

/**
 * Created by Renaldo on 3/7/2015.
 */
public class Coordinate {

    public Coordinate(String lat, String lng, String date)
    {
        this.Lattitude = lat;
        this.Longitude = lng;
        this.Date = date;
    }

    public String Lattitude;
    public String Longitude;
    public String Date;
}
