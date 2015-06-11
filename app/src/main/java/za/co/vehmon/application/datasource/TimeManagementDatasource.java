package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import za.co.vehmon.application.core.TimeManagement;

/**
 * Created by Renaldo on 2/8/2015.
 */
public class TimeManagementDatasource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;


    private String[] allColumns =
            {
                    MySQLiteHelper.TABLE_TIMEMNG_ID,
                    MySQLiteHelper.TABLE_TIMEMNG_CLOCKINTIME,
                    MySQLiteHelper.TABLE_TIMEMNG_CLOCKOUTTIME,
                    MySQLiteHelper.TABLE_TIMEMNG_INLAT,
                    MySQLiteHelper.TABLE_TIMEMNG_INLNG,
                    MySQLiteHelper.TABLE_TIMEMNG_OUTLAT,
                    MySQLiteHelper.TABLE_TIMEMNG_OUTLNG,
                    MySQLiteHelper.TABLE_TIMEMNG_USERID,
                    MySQLiteHelper.TABLE_TIMEMNG_SHIFTID,
                    MySQLiteHelper.TABLE_TIMEMNG_INSYNCED,
                    MySQLiteHelper.TABLE_TIMEMNG_OUTSYNCED
            };

    public TimeManagementDatasource(Context context)
    {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long clockIn(String clockinTime, String userID)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_TIMEMNG_CLOCKINTIME, clockinTime);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_USERID, userID);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_INSYNCED, 0);
        //Insert into database
        long id = database.insert(MySQLiteHelper.TABLE_TIMEMNG, null,values);
        close();

        return id;
    }

    public long clockout(String clockoutTime, String userID, int timeTrackingID)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int timeManagementID = timeTrackingID;

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_TIMEMNG_CLOCKOUTTIME, clockoutTime);
        args.put(MySQLiteHelper.TABLE_TIMEMNG_OUTSYNCED, 0);

        long id = database.update(MySQLiteHelper.TABLE_TIMEMNG, args, MySQLiteHelper.TABLE_TIMEMNG_ID + "=" + timeManagementID, null);

        close();

        return id;
    }

    public long InsertTimeManagement(String clockinTime, String clockoutTime, String inLat, String inLng, String outLat, String outLng, String userID)
    {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_TIMEMNG_CLOCKINTIME, clockinTime);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_CLOCKOUTTIME, clockoutTime);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_INLAT, inLat);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_INLNG, inLng);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_OUTLAT, outLat);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_OUTLNG, outLng);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_USERID, userID);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_INSYNCED, 0);
        values.put(MySQLiteHelper.TABLE_TIMEMNG_OUTSYNCED, 0);
        //Insert into database
        return database.insert(MySQLiteHelper.TABLE_TIMEMNG, null,values);
    }

    public Boolean updateClockInSynced(Integer id, String shiftID)
    {
        //This is set when the file has been successfully uploaded to server.
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_TIMEMNG_INSYNCED, 1);
        args.put(MySQLiteHelper.TABLE_TIMEMNG_SHIFTID,Integer.valueOf(shiftID));

        long success = database.update(MySQLiteHelper.TABLE_TIMEMNG, args, MySQLiteHelper.TABLE_TIMEMNG_ID + "=" + id, null);

        close();
        return true;
    }

    public Boolean updateClockOutSynced(Integer id)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //This is set when the file has been successfully uploaded to server.
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_TIMEMNG_OUTSYNCED, 1);
        long success = database.update(MySQLiteHelper.TABLE_TIMEMNG, args, MySQLiteHelper.TABLE_TIMEMNG_ID + "=" + id, null);

        close();
        return true;
    }

    public List<TimeManagement> GetUnsynchronizedClockIn()
    {
        List<TimeManagement> listTime = new ArrayList<TimeManagement>();
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMEMNG,allColumns, MySQLiteHelper.TABLE_TIMEMNG_INSYNCED + "= ?", new String[]{"0"}, null, null, MySQLiteHelper.TABLE_TIMEMNG_ID+ " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeManagement obj = cursorToTimeManagement(cursor);
            listTime.add(obj);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        close();

        return listTime;
    }

    public int GetShiftID(int timeManagementID)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMEMNG,allColumns, MySQLiteHelper.TABLE_TIMEMNG_ID + "= ?", new String[]{String.valueOf(timeManagementID)}, null, null, MySQLiteHelper.TABLE_TIMEMNG_ID+ " ASC");

        cursor.moveToFirst();

        int shiftId = cursor.getInt(8);

        cursor.close();
        close();

        return shiftId;
    }

    public List<TimeManagement> GetUnsynchronizedClockOut()
    {
        List<TimeManagement> listTime = new ArrayList<TimeManagement>();
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMEMNG,allColumns, MySQLiteHelper.TABLE_TIMEMNG_OUTSYNCED + "= ?", new String[]{"0"}, null, null, MySQLiteHelper.TABLE_TIMEMNG_ID+ " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeManagement obj = cursorToTimeManagement(cursor);
            listTime.add(obj);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        close();

        return listTime;
    }

    private TimeManagement cursorToTimeManagement(Cursor cursor)
    {
        TimeManagement obj = new TimeManagement();
        obj.setId(cursor.getInt(0));
        obj.setClockInTime(cursor.getString(1));
        obj.setClockOutTime(cursor.getString(2));
        obj.setInLat(cursor.getString(3));
        obj.setInLng(cursor.getString(4));
        obj.setOutLat(cursor.getString(5));
        obj.setOutLng(cursor.getString(6));
        obj.setUserID(cursor.getString(7));
        obj.setUserID(cursor.getString(7));
        obj.setShiftID(String.valueOf(cursor.getInt(8)));
        obj.setInSynced(cursor.getInt(9));
        obj.setOutSynced(cursor.getInt(10));

        return obj;
    }


}
