package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import za.co.vehmon.application.core.AbsenceRequest;

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

    public long InsertAbsenceRequest(String fromDate, String toDate, String userID, String leaveTypeID)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_FROMDATE, fromDate);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_TODATE, toDate);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_USERID, userID);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_LEAVETYPE, leaveTypeID);
        values.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_SYNCED, 0);

        //Insert into database
        long id = database.insert(MySQLiteHelper.TABLE_ABSENCEREQUEST, null,values);
        close();
        return id;
    }

    public List<AbsenceRequest> GetUnsynhronizedAbsenceRequests()
    {
        List<AbsenceRequest> absenceRequests = new ArrayList<AbsenceRequest>();
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ABSENCEREQUEST,allColumns, MySQLiteHelper.TABLE_ABSENCEREQUEST_SYNCED + "= ?", new String[]{"0"}, null, null, MySQLiteHelper.TABLE_ABSENCEREQUEST_ID+ " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AbsenceRequest obj = cursorToAbsenceRequest(cursor);
            absenceRequests.add(obj);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        close();

        return absenceRequests;
    }

    private AbsenceRequest cursorToAbsenceRequest(Cursor cursor)
    {
        AbsenceRequest req = new AbsenceRequest();
        req.setId(cursor.getInt(0));
        req.setFromDate(cursor.getString(1));
        req.setToDate(cursor.getString(2));
        req.setUserID(cursor.getString(3));
        req.setLeaveTypeID(cursor.getString(4));

        return req;
    }

    public Boolean updateSynced(Integer absenceRequestID)
    {
        //This is set when the file has been successfully uploaded to server.
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_ABSENCEREQUEST_SYNCED, 1);
        database.update(MySQLiteHelper.TABLE_ABSENCEREQUEST, args, MySQLiteHelper.TABLE_ABSENCEREQUEST_ID + "=" + absenceRequestID, null);

        close();

        return true;
    }
}
