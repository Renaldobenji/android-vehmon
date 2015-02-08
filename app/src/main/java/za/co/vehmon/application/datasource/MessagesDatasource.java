package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Renaldo on 2/8/2015.
 */
public class MessagesDatasource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns =
            {
                    MySQLiteHelper.TABLE_MSG_ID,
                    MySQLiteHelper.TABLE_MSG_CONVOID,
                    MySQLiteHelper.TABLE_MSG_MSG,
                    MySQLiteHelper.TABLE_MSG_TO,
                    MySQLiteHelper.TABLE_MSG_FROM,
                    MySQLiteHelper.TABLE_MSG_DATE,
                    MySQLiteHelper.TABLE_MSG_SYNCED
            };

    public MessagesDatasource(Context context)
    {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long InsertMessage(Integer messageConvoID, String message, String to, String from, String date)
    {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_MSG_CONVOID, messageConvoID);
        values.put(MySQLiteHelper.TABLE_MSG_MSG, message);
        values.put(MySQLiteHelper.TABLE_MSG_TO, to);
        values.put(MySQLiteHelper.TABLE_MSG_FROM, from);
        values.put(MySQLiteHelper.TABLE_MSG_DATE, date);
        values.put(MySQLiteHelper.TABLE_MSG_SYNCED, 0);
        //Insert into database
        return database.insert(MySQLiteHelper.TABLE_MSG, null,values);
    }

    public Boolean updateSynced(Integer id)
    {
        //This is set when the file has been successfully uploaded to server.
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_MSG_SYNCED, 1);
        database.update(MySQLiteHelper.TABLE_MSG, args, MySQLiteHelper.TABLE_MSG_ID + "=" + id, null);
        return true;
    }
}
