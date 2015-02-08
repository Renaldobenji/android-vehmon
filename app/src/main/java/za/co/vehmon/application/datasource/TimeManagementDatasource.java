package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

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
                    MySQLiteHelper.TABLE_TIMEMNG_SYNCED
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
        values.put(MySQLiteHelper.TABLE_TIMEMNG_SYNCED, 0);
        //Insert into database
        return database.insert(MySQLiteHelper.TABLE_TIMEMNG, null,values);
    }

    public Boolean updateSynced(Integer id)
    {
        //This is set when the file has been successfully uploaded to server.
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_TIMEMNG_SYNCED, 1);
        database.update(MySQLiteHelper.TABLE_TIMEMNG, args, MySQLiteHelper.TABLE_TIMEMNG_ID + "=" + id, null);
        return true;
    }
}
