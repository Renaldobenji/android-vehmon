package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Renaldo on 2/8/2015.
 */
public class AbsenceRequestDatasource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns =
    {
            MySQLiteHelper.TABLE_ABSENCEREQUEST_ID,
            MySQLiteHelper.TABLE_ABSENCEREQUEST_FROMDATE,
            MySQLiteHelper.TABLE_ABSENCEREQUEST_TODATE,
            MySQLiteHelper.TABLE_ABSENCEREQUEST_USERID,
            MySQLiteHelper.TABLE_ABSENCEREQUEST_LEAVETYPE,
            MySQLiteHelper.TABLE_ABSENCEREQUEST_SYNCED
    };

    public AbsenceRequestDatasource(Context context)
    {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long InsertAbsenceRequest(String fromDate, String toDate, String userID, String leaveType)
    {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_FROMDATE, fromDate);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_TODATE, toDate);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_USERID, userID);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_LEAVETYPE, leaveType);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_SYNCED, 0);

        //Insert into database
        return database.insert(MySQLiteHelper.TABLE_ABSENCEREQUEST, null,values);
    }

    public Boolean updateSynced(Integer absenceRequestID)
    {
        //This is set when the file has been successfully uploaded to server.
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_SYNCED, 1);
        database.update(MySQLiteHelper.TABLE_ABSENCEREQUEST, args, MySQLiteHelper.TABLE_ABSENCEREQUEST_ID + "=" + absenceRequestID, null);
        return true;
    }
}
