package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Renaldo on 2/8/2015.
 */
public class MessageConversationDatasource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns =
            {
                    MySQLiteHelper.TABLE_MSGCONVO_ID,
                    MySQLiteHelper.TABLE_MSGCONVO_DATE,
                    MySQLiteHelper.TABLE_MSGCONVO_FROM,
                    MySQLiteHelper.TABLE_MSGCONVO_TO,
                    MySQLiteHelper.TABLE_MSGCONVO_SYNCED
            };

    public MessageConversationDatasource(Context context)
    {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long InsertMessageConversation(String date, String from, String to)
    {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_MSGCONVO_DATE, date);
        values.put(MySQLiteHelper.TABLE_MSGCONVO_FROM, from);
        values.put(MySQLiteHelper.TABLE_MSGCONVO_TO, to);
        values.put(MySQLiteHelper.TABLE_MSGCONVO_SYNCED, 0);
        //Insert into database
        return database.insert(MySQLiteHelper.TABLE_TIMEMNG, null,values);
    }

    public Boolean updateSynced(Integer id)
    {
        //This is set when the file has been successfully uploaded to server.
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_MSGCONVO_SYNCED, 1);
        database.update(MySQLiteHelper.TABLE_MSGCONVO, args, MySQLiteHelper.TABLE_MSGCONVO_ID + "=" + id, null);
        return true;
    }
}
