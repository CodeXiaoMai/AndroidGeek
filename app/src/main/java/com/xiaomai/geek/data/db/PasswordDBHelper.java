package com.xiaomai.geek.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.xiaomai.geek.common.wrapper.AppLog;


/**
 * Created by xiaomai on 2017/10/25.
 */

public class PasswordDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "PasswordDBHelper";

    private static final String DATABASE_NAME = "Password.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PasswordEntry.TABLE_NAME + " ("
                    + PasswordEntry._ID + " PRIMARY KEY, "
                    + PasswordEntry.COLUMN_NAME_PLATFORM + TEXT_TYPE + COMMA_SEP
                    + PasswordEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP
                    + PasswordEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP
                    + PasswordEntry.COLUMN_NAME_NOTE + TEXT_TYPE
                    + ");";

    private static PasswordDBHelper INSTANCE;

    public static PasswordDBHelper getInstance(Context context) {
        if (null == INSTANCE) {
            synchronized (PasswordDBHelper.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PasswordDBHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private PasswordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AppLog.d(TAG, SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static abstract class PasswordEntry implements BaseColumns {
        public static final String TABLE_NAME = "password";
        public static final String COLUMN_NAME_PLATFORM = "platform";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_NOTE = "note";
    }
}