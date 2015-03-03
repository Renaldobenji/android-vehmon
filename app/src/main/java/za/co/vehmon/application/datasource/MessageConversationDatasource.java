package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import za.co.vehmon.application.core.MessageConversation;

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
                    MySQLiteHelper.TABLE_MSGCONVO_SERVERCONVOID,
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

    public long InsertMessageConversation(String date, String from, String to, int conversationID)
    {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_MSGCONVO_DATE, date);
        values.put(MySQLiteHelper.TABLE_MSGCONVO_FROM, from);
        values.put(MySQLiteHelper.TABLE_MSGCONVO_TO, to);
        values.put(MySQLiteHelper.TABLE_MSGCONVO_SERVERCONVOID, conversationID);
        values.put(MySQLiteHelper.TABLE_MSGCONVO_SYNCED, 0);
        //Insert into database
        try {
            open();
        } catch (SQLException e) {
            return -1;
        }
        long dbID = database.insert(MySQLiteHelper.TABLE_MSGCONVO, null,values);
        close();
        return dbID;
    }

    public List<MessageConversation> GetAllConversations()
    {
        List<MessageConversation> msgs = new ArrayList<MessageConversation>();
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MSGCONVO,allColumns, null, null, null, null, MySQLiteHelper.TABLE_MSGCONVO_ID+ " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MessageConversation log = cursorToMsgConvo(cursor);
            msgs.add(log);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        close();
        return msgs;
    }

    private MessageConversation cursorToMsgConvo(Cursor cursor) {
        MessageConversation msg = new MessageConversation();
        msg.setMessageConversationID(cursor.getInt(0));
        msg.setDate(cursor.getString(1));
        msg.setFrom(cursor.getString(2));
        msg.setTo(cursor.getString(3));

        return msg;
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
