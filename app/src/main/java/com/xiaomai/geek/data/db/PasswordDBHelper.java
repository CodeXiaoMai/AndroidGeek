
package com.xiaomai.geek.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaomai.geek.data.module.Password;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoMai on 2017/3/30 15:16.
 */

public class PasswordDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "geek.db";

    private static final String TABLE_NAME = "table_password";

    public static final String COLUMN_ID = "_ID";

    public static final String COLUMN_PLATFORM = "PLATFORM";

    public static final String COLUMN_USERNAME = "USERNAME";

    public static final String COLUMN_PASSWORD = "PASSWORD";

    public static final String COLUMN_CATEGORY = "CATEGORY";

    public static final String COLUMN_NOTE = "NOTE";

    public static final String COLUMN_STAR = "STAR";

    public static final String COLUMN_TIME = "TIME";

    private static final String ORDER_BY = COLUMN_TIME + " DESC";

    private static final int DATABASE_VERSION = 1;

    private static PasswordDBHelper sDBHelper;

    public static PasswordDBHelper getInstance(Context context) {
        if (null == sDBHelper) {
            synchronized (PasswordDBHelper.class) {
                if (null == sDBHelper) {
                    sDBHelper = new PasswordDBHelper(context);
                }
            }
        }
        return sDBHelper;
    }

    private PasswordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "_ID INTEGER NOT NULL PRIMARY KEY, " + "PLATFORM TEXT NOT NULL, "
                + "USERNAME TEXT NOT NULL, " + "PASSWORD TEXT NOT NULL, " + "CATEGORY TEXT, "
                + "NOTE TEXT, " + "STAR BOOLEAN, " + "TIME INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(String platform, String userName, String password, String note, int isStar,
            long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLATFORM, platform);
        contentValues.put(COLUMN_USERNAME, userName);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_NOTE, note);
        contentValues.put(COLUMN_STAR, isStar);
        contentValues.put(COLUMN_TIME, time);
        return getReadableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    public long insert(Password password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLATFORM, password.getPlatform());
        contentValues.put(COLUMN_USERNAME, password.getUserName());
        contentValues.put(COLUMN_PASSWORD, password.getPassword());
        contentValues.put(COLUMN_NOTE, password.getNote());
        contentValues.put(COLUMN_STAR, password.isStar());
        if (!contentValues.containsKey(COLUMN_TIME)) {
            contentValues.put(COLUMN_TIME, System.currentTimeMillis());
        }
        return getReadableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    private Password getPasswordByCurse(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        String platform = cursor.getString(cursor.getColumnIndex(COLUMN_PLATFORM));
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String pwd = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        String note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
        int star = cursor.getInt(cursor.getColumnIndex(COLUMN_STAR));
        String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
        Password password = new Password();
        password.setId(id);
        password.setPlatform(platform);
        password.setUserName(userName);
        password.setPassword(pwd);
        password.setNote(note);
        password.setStar(star);
        password.setCategory(category);
        return password;
    }

    /**
     * 查询所有的密码
     * 
     * @return
     */
    public List<Password> queryAllPasswords() {
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null,
                ORDER_BY);
        List<Password> passwords = new ArrayList<>();
        while (cursor.moveToNext()) {
            Password password = getPasswordByCurse(cursor);
            if (password != null)
                passwords.add(password);
        }
        cursor.close();
        return passwords;
    }

    public Password getPasswordById(int id) {
        final String selection = COLUMN_ID + "=?";
        final String[] args = new String[] {
                String.valueOf(id)
        };
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, selection, args, null, null,
                null);
        Password password = null;
        if (cursor != null && cursor.moveToNext()) {
            password = getPasswordByCurse(cursor);
            cursor.close();
        }
        return password;
    }

    public int updatePasswordById(int id, ContentValues contentValues) {
        final String where = COLUMN_ID + "=?";
        final String[] args = new String[] {
                String.valueOf(id)
        };
        if (!contentValues.containsKey(COLUMN_TIME)) {
            contentValues.put(COLUMN_TIME, System.currentTimeMillis());
        }
        return getWritableDatabase().update(TABLE_NAME, contentValues, where, args);
    }

    public int deletePasswordById(int id) {
        final String where = COLUMN_ID + "=?";
        final String[] args = new String[] {
                String.valueOf(id)
        };
        return getWritableDatabase().delete(TABLE_NAME, where, args);
    }
}
