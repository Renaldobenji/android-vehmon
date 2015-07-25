package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import za.co.vehmon.application.core.GPSLog;
import za.co.vehmon.application.core.TimeManagement;

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
        values.put(MySQLiteHelper.TABLE_GPSLOG_SYNC, 0);

        long id = database.insert(MySQLiteHelper.TABLE_GPSLOG, null,values);

        close();
        return id;
    }

    public List<GPSLog> GetUnsynchronizedGPSLogs(Context context)
    {
        List<GPSLog> logs = new ArrayList<GPSLog>();
        TimeManagementDatasource timeDS = new TimeManagementDatasource(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = database.query(MySQLiteHelper.TABLE_GPSLOG,allColumns, MySQLiteHelper.TABLE_GPSLOG_SYNC + "= ?", new String[]{"0"}, null, null, MySQLiteHelper.TABLE_GPSLOG_ID+ " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GPSLog obj = cursorToObject(cursor);
            obj.setShiftID(timeDS.GetShiftID(obj.getTimeManagementID()));//Set Shift ID
            logs.add(obj);
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return logs;
    }

    public Boolean UpdateGPSLog(long id)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //This is set when the file has been successfully uploaded to server.
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_GPSLOG_SYNC, 1);
        long success = database.update(MySQLiteHelper.TABLE_GPSLOG, args, MySQLiteHelper.TABLE_GPSLOG_ID + "=" + id, null);

        close();
        return true;
    }

    public Boolean DeleteGPSLog(long id)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //This is set when the file has been successfully uploaded to server.
        long success = database.delete(MySQLiteHelper.TABLE_GPSLOG, MySQLiteHelper.TABLE_GPSLOG_ID + "=" + id,null);

        close();
        return true;
    }

    private GPSLog cursorToObject(Cursor cursor)
    {
        GPSLog obj = new GPSLog();

        obj.setId(cursor.getInt(0));
        obj.setLat(cursor.getString(1));
        obj.setLng(cursor.getString(2));
        obj.setAccuracy(cursor.getString(3));
        obj.setDate(cursor.getString(4));
        obj.setSynced(cursor.getInt(5));
        obj.setTimeManagementID(cursor.getInt(6));

        return obj;
    }
}
