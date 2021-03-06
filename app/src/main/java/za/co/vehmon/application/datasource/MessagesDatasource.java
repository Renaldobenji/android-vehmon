package za.co.vehmon.application.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import za.co.vehmon.application.core.Message;
import za.co.vehmon.application.core.MessageConversation;

/**
 * Created by Renaldo on 2/8/2015.
 */
public class MessagesDatasource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context context;

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
        this.context = context;
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long InsertMessage(Integer messageConvoID, String message, String from ,String to, String date)
    {
        MessageConversationDatasource ds = new MessageConversationDatasource(context);
        MessageConversation convo = ds.GetConversation((int)messageConvoID);

        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_MSG_CONVOID, convo.getMessageConversationID());
        values.put(MySQLiteHelper.TABLE_MSG_MSG, message);
        values.put(MySQLiteHelper.TABLE_MSG_TO, to);
        values.put(MySQLiteHelper.TABLE_MSG_FROM, from);
        values.put(MySQLiteHelper.TABLE_MSG_DATE, date);
        values.put(MySQLiteHelper.TABLE_MSG_SYNCED, 0);
        //Insert into database
        long id = database.insert(MySQLiteHelper.TABLE_MSG, null,values);
        close();
        return id;
    }

    public long InsertMessage(long messageConvoID, String message, String from ,String to, String date, int synced)
    {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.TABLE_MSG_CONVOID, messageConvoID);
        values.put(MySQLiteHelper.TABLE_MSG_MSG, message);
        values.put(MySQLiteHelper.TABLE_MSG_TO, to);
        values.put(MySQLiteHelper.TABLE_MSG_FROM, from);
        values.put(MySQLiteHelper.TABLE_MSG_DATE, date);
        values.put(MySQLiteHelper.TABLE_MSG_SYNCED, synced);
        //Insert into database
        long id = database.insert(MySQLiteHelper.TABLE_MSG, null,values);
        close();
        return id;
    }

    public List<Message> GetMessagesForConversation(long conversationID)
    {
        List<Message> messages = new ArrayList<Message>();

        MessageConversationDatasource ds = new MessageConversationDatasource(context);
        MessageConversation convo = ds.GetConversation((int)conversationID);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MSG,allColumns, MySQLiteHelper.TABLE_MSG_CONVOID + "= ?", new String[] {String.valueOf(convo.getMessageConversationID())}, null, null, MySQLiteHelper.TABLE_MSG_ID+ " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message log = cursorToMsgConvo(cursor);
            messages.add(log);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        close();

        return messages;
    }

    public List<Message> GetUnsyncedMessages(Context context)
    {
        List<Message> messages = new ArrayList<Message>();
        MessageConversationDatasource msgDS = new MessageConversationDatasource(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MSG,allColumns, MySQLiteHelper.TABLE_MSG_SYNCED + "= ?", new String[] {String.valueOf(0)}, null, null, MySQLiteHelper.TABLE_MSG_ID+ " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message log = cursorToMsgConvo(cursor);
            //Get server Conversation ID
            log.setMessageServerConversationID(msgDS.GetServerConversationID(log.getMessageConversationID()));
            messages.add(log);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        close();

        return messages;
    }

    private Message cursorToMsgConvo(Cursor cursor) {
        Message msg = new Message();
        msg.setMessageID(cursor.getInt(0));
        msg.setMessageConversationID(cursor.getInt(1));
        msg.setMessage(cursor.getString(2));
        msg.setTo(cursor.getString(3));
        msg.setFrom(cursor.getString(4));
        msg.setDate(cursor.getString(5));
        return msg;
    }

    public Boolean updateSynced(Integer id)
    {
        //This is set when the file has been successfully uploaded to server.
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.TABLE_MSG_SYNCED, 1);
        database.update(MySQLiteHelper.TABLE_MSG, args, MySQLiteHelper.TABLE_MSG_ID + "=" + id, null);

        close();

        return true;
    }
}
