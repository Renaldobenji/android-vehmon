package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Renaldo on 2/23/2015.
 */
public class GPSLogDatasource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns =
            {
                    MySQLiteHelper.TABLE_GPSLOG_ID,
                    MySQLiteHelper.TABLE_GPSLOG_LAT,
                    MySQLiteHelper.TABLE_GPSLOG_LNG,
                    MySQLiteHelper.TABLE_GPSLOG_ACCURACY,
                    MySQLiteHelper.TABLE_GPSLOG_DATE,
                    MySQLiteHelper.TABLE_GPSLOG_SYNC,
                    MySQLiteHelper.TABLE_GPSLOG_TIMEMNG_ID
            };

    public GPSLogDatasource(Context context)
    {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long logGPSCoordinates(String lat, String lng, String accuracy, String date, String timeManagementID)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_GPSLOG_TIMEMNG_ID, timeManagementID);
        values.put(MySQLiteHelper.TABLE_GPSLOG_LAT, lat);
        values.put(MySQLiteHelper.TABLE_GPSLOG_LNG, lng);
        values.put(MySQLiteHelper.TABLE_GPSLOG_ACCURACY, accuracy);
        values.put(MySQLiteHelper.TABLE_GPSLOG_DATE, date);


        long id = database.insert(MySQLiteHelper.TABLE_GPSLOG, null,values);

        close();
        return id;
    }
}
