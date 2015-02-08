package za.co.vehmon.application.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Renaldo on 2/8/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vehmon.db";
    private static final int DATABASE_VERSION = 1;

    //Absence Request
    public static final String TABLE_ABSENCEREQUEST = "ABSENCEREQUEST";
    public static final String TABLE_ABSENCEREQUEST_ID = "ID";
    public static final String TABLE_ABSENCEREQUEST_FROMDATE = "FROMDATE";
    public static final String TABLE_ABSENCEREQUEST_TODATE = "TODATE";
    public static final String TABLE_ABSENCEREQUEST_USERID = "USERID";
    public static final String TABLE_ABSENCEREQUEST_LEAVETYPE = "LEAVETYPE";
    public static final String TABLE_ABSENCEREQUEST_SYNCED = "SYNCED";

    private static final String TABLE_CREATE_ABSENCEREQUEST = "create table "
            +TABLE_ABSENCEREQUEST + "("
            +TABLE_ABSENCEREQUEST_ID + " integer primary key autoincrement, "
            +TABLE_ABSENCEREQUEST_FROMDATE + " text not null, "
            +TABLE_ABSENCEREQUEST_TODATE + " text not null, "
            +TABLE_ABSENCEREQUEST_USERID + " text not null, "
            +TABLE_ABSENCEREQUEST_LEAVETYPE +  " text not null, "
            +TABLE_ABSENCEREQUEST_SYNCED + " integer)";

    public static final String TABLE_TIMEMNG = "TIMEMANAGEMENT";
    public static final String TABLE_TIMEMNG_ID = "ID";
    public static final String TABLE_TIMEMNG_CLOCKINTIME = "CLOCKINTIME";
    public static final String TABLE_TIMEMNG_CLOCKOUTTIME = "CLOCKOUTTIME";
    public static final String TABLE_TIMEMNG_INLAT = "INLAT";
    public static final String TABLE_TIMEMNG_INLNG = "INLNG";
    public static final String TABLE_TIMEMNG_OUTLAT = "OUTLAT";
    public static final String TABLE_TIMEMNG_OUTLNG = "OUTLNG";
    public static final String TABLE_TIMEMNG_USERID = "USERID";
    public static final String TABLE_TIMEMNG_SYNCED = "SYNCED";

    private static final String TABLE_CREATE_TIMEMNG = "create table "
            +TABLE_TIMEMNG + "("
            +TABLE_TIMEMNG_ID + " integer primary key autoincrement, "
            +TABLE_TIMEMNG_CLOCKINTIME + " text not null, "
            +TABLE_TIMEMNG_CLOCKOUTTIME + " text not null, "
            +TABLE_TIMEMNG_INLAT + " text not null, "
            +TABLE_TIMEMNG_INLNG + " text not null, "
            +TABLE_TIMEMNG_OUTLAT + " text not null, "
            +TABLE_TIMEMNG_OUTLNG + " text not null, "
            +TABLE_TIMEMNG_USERID + " text not null, "
            +TABLE_TIMEMNG_SYNCED + " integer)";

    public static final String TABLE_MSGCONVO = "MESSAGECONVERSATION";
    public static final String TABLE_MSGCONVO_ID = "ID";
    public static final String TABLE_MSGCONVO_DATE = "DATE";
    public static final String TABLE_MSGCONVO_FROM = "FROM";
    public static final String TABLE_MSGCONVO_TO = "TO";
    public static final String TABLE_MSGCONVO_SYNCED = "SYNCED";

    private static final String TABLE_CREATE_MSGCONVO = "create table "
            +TABLE_MSGCONVO + "("
            +TABLE_MSGCONVO_ID + " integer primary key autoincrement, "
            +TABLE_MSGCONVO_DATE + " text not null, "
            +TABLE_MSGCONVO_FROM + " text not null, "
            +TABLE_MSGCONVO_TO + " text not null, "
            +TABLE_MSGCONVO_SYNCED + " integer)";

    public static final String TABLE_MSG = "MESSAGES";
    public static final String TABLE_MSG_ID = "ID";
    public static final String TABLE_MSG_CONVOID = "CONVOID";
    public static final String TABLE_MSG_MSG = "MSG";
    public static final String TABLE_MSG_TO = "TO";
    public static final String TABLE_MSG_FROM = "FROM";
    public static final String TABLE_MSG_DATE = "DATE";
    public static final String TABLE_MSG_SYNCED = "SYNCED";

    private static final String TABLE_CREATE_MSG = "create table "
            +TABLE_MSG + "("
            +TABLE_MSG_ID + " integer primary key autoincrement, "
            +TABLE_MSG_CONVOID + " integer not null, "
            +TABLE_MSG_MSG + " text not null, "
            +TABLE_MSG_TO + " text not null, "
            +TABLE_MSG_FROM + " text not null, "
            +TABLE_MSG_DATE + " text not null, "
            +TABLE_MSG_SYNCED + " integer)";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_ABSENCEREQUEST);
        sqLiteDatabase.execSQL(TABLE_CREATE_MSG);
        sqLiteDatabase.execSQL(TABLE_CREATE_MSGCONVO);
        sqLiteDatabase.execSQL(TABLE_CREATE_TIMEMNG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSENCEREQUEST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MSGCONVO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMEMNG);
        onCreate(sqLiteDatabase);

    }
}
