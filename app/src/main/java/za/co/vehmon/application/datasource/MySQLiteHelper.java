package za.co.vehmon.application.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Renaldo on 2/8/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vehmon.db";
    private static final int DATABASE_VERSION = 6;

    //Absence Request
    public static final String TABLE_ABSENCEREQUEST = "ABSENCEREQUEST";
    public static final String TABLE_ABSENCEREQUEST_ID = "ID";
    public static final String TABLE_ABSENCEREQUEST_FROMDATE = "FROMDATE";
    public static final String TABLE_ABSENCEREQUEST_TODATE = "TODATE";
    public static final String TABLE_ABSENCEREQUEST_USERID = "USERID";
    public static final String TABLE_ABSENCEREQUEST_LEAVETYPE = "LEAVETYPE";
    public static final String TABLE_ABSENCEREQUEST_SYNCED = "SYNCED";

    private static final String TABLE_CREATE_ABSENCEREQUEST = "CREATE TABLE "
            +TABLE_ABSENCEREQUEST + "("
            +TABLE_ABSENCEREQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLE_ABSENCEREQUEST_FROMDATE + " TEXT NOT NULL, "
            +TABLE_ABSENCEREQUEST_TODATE + " TEXT NOT NULL, "
            +TABLE_ABSENCEREQUEST_USERID + " TEXT NOT NULL, "
            +TABLE_ABSENCEREQUEST_LEAVETYPE +  " TEXT NOT NULL, "
            +TABLE_ABSENCEREQUEST_SYNCED + " INTEGER)";

    public static final String TABLE_TIMEMNG = "TIMEMANAGEMENT";
    public static final String TABLE_TIMEMNG_ID = "ID";
    public static final String TABLE_TIMEMNG_CLOCKINTIME = "CLOCKINTIME";
    public static final String TABLE_TIMEMNG_CLOCKOUTTIME = "CLOCKOUTTIME";
    public static final String TABLE_TIMEMNG_INLAT = "INLAT";
    public static final String TABLE_TIMEMNG_INLNG = "INLNG";
    public static final String TABLE_TIMEMNG_OUTLAT = "OUTLAT";
    public static final String TABLE_TIMEMNG_OUTLNG = "OUTLNG";
    public static final String TABLE_TIMEMNG_USERID = "USERID";
    public static final String TABLE_TIMEMNG_SHIFTID = "SHIFTID";
    public static final String TABLE_TIMEMNG_INSYNCED = "INSYNCED";
    public static final String TABLE_TIMEMNG_OUTSYNCED = "OUTSYNCED";

    private static final String TABLE_CREATE_TIMEMNG = "create table "
            +TABLE_TIMEMNG + "("
            +TABLE_TIMEMNG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLE_TIMEMNG_CLOCKINTIME + " TEXT, "
            +TABLE_TIMEMNG_CLOCKOUTTIME + " TEXT, "
            +TABLE_TIMEMNG_INLAT + " TEXT, "
            +TABLE_TIMEMNG_INLNG + " TEXT, "
            +TABLE_TIMEMNG_OUTLAT + " TEXT, "
            +TABLE_TIMEMNG_OUTLNG + " TEXT, "
            +TABLE_TIMEMNG_USERID + " TEXT NOT NULL, "
            +TABLE_TIMEMNG_SHIFTID + " INTEGER, "
            +TABLE_TIMEMNG_INSYNCED + " INTEGER, "
            +TABLE_TIMEMNG_OUTSYNCED + " INTEGER)";

    public static final String TABLE_MSGCONVO = "MESSAGECONVERSATION";
    public static final String TABLE_MSGCONVO_ID = "ID";
    public static final String TABLE_MSGCONVO_DATE = "DATE";
    public static final String TABLE_MSGCONVO_FROM = "MSGFROM";
    public static final String TABLE_MSGCONVO_TO = "MSGTO";
    public static final String TABLE_MSGCONVO_SYNCED = "SYNCED";
    public static final String TABLE_MSGCONVO_SERVERCONVOID = "SERVERCONVOID";
    public static final String TABLE_MSGCONVO_UNREADMESSAGES = "UNREADMESSAGES";
    public static final String TABLE_MSGCONVO_LASTUPDATED = "LASTUPDATED";

    private static final String TABLE_CREATE_MSGCONVO = "CREATE TABLE "
            +TABLE_MSGCONVO + "("
            +TABLE_MSGCONVO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLE_MSGCONVO_DATE + " TEXT NOT NULL, "
            +TABLE_MSGCONVO_FROM + " TEXT NOT NULL, "
            +TABLE_MSGCONVO_TO + " TEXT NOT NULL, "
            +TABLE_MSGCONVO_SERVERCONVOID + " INTEGER, "
            +TABLE_MSGCONVO_UNREADMESSAGES + " INTEGER, "
            +TABLE_MSGCONVO_LASTUPDATED + " TEXT, " //TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
            +TABLE_MSGCONVO_SYNCED + " INTEGER)";

    public static final String TABLE_MSG = "MESSAGES";
    public static final String TABLE_MSG_ID = "ID";
    public static final String TABLE_MSG_CONVOID = "CONVOID";
    public static final String TABLE_MSG_MSG = "MSG";
    public static final String TABLE_MSG_TO = "MSGTO";
    public static final String TABLE_MSG_FROM = "MSGFROM";
    public static final String TABLE_MSG_DATE = "DATE";
    public static final String TABLE_MSG_SYNCED = "SYNCED";

    private static final String TABLE_CREATE_MSG = "CREATE TABLE "
            +TABLE_MSG + "("
            +TABLE_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLE_MSG_CONVOID + " INTEGER NOT NULL, "
            +TABLE_MSG_MSG + " TEXT NOT NULL, "
            +TABLE_MSG_TO + " TEXT NOT NULL, "
            +TABLE_MSG_FROM + " TEXT NOT NULL, "
            +TABLE_MSG_DATE + " TEXT NOT NULL, "
            +TABLE_MSG_SYNCED + " INTEGER)";

    public static final String TABLE_GPSLOG = "GPSLOG";
    public static final String TABLE_GPSLOG_ID = "ID";
    public static final String TABLE_GPSLOG_LAT = "GPSLAT";
    public static final String TABLE_GPSLOG_LNG = "GPSLNG";
    public static final String TABLE_GPSLOG_ACCURACY = "GPSACCURACY";
    public static final String TABLE_GPSLOG_DATE = "GPSDATE";
    public static final String TABLE_GPSLOG_SYNC = "SYNCED";
    public static final String TABLE_GPSLOG_TIMEMNG_ID = "TIMEMNGID";

    private static final String TABLE_CREATE_GPSLOG = "CREATE TABLE "
            +TABLE_GPSLOG + "("
            +TABLE_GPSLOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLE_GPSLOG_TIMEMNG_ID + " INTEGER NOT NULL, "
            +TABLE_GPSLOG_LAT + " TEXT NOT NULL, "
            +TABLE_GPSLOG_LNG + " TEXT NOT NULL, "
            +TABLE_GPSLOG_ACCURACY + " TEXT NOT NULL, "
            +TABLE_GPSLOG_DATE + " TEXT NOT NULL, "
            +TABLE_GPSLOG_SYNC + " INTEGER)";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_ABSENCEREQUEST);
        sqLiteDatabase.execSQL(TABLE_CREATE_TIMEMNG);
        sqLiteDatabase.execSQL(TABLE_CREATE_MSG);
        sqLiteDatabase.execSQL(TABLE_CREATE_MSGCONVO);
        sqLiteDatabase.execSQL(TABLE_CREATE_GPSLOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSENCEREQUEST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MSGCONVO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMEMNG);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GPSLOG);
        onCreate(sqLiteDatabase);

    }
}
