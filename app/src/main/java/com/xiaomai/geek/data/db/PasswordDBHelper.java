package com.xiaomai.geek.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaomai.geek.common.utils.AESUtils;
import com.xiaomai.geek.common.utils.SecretUtil;
import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.data.module.Password;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/30 15:16.
 */

public class PasswordDBHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_PLATFORM = "PLATFORM";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_CATEGORY = "CATEGORY";
    public static final String COLUMN_NOTE = "NOTE";
    public static final String COLUMN_STAR = "STAR";
    public static final String COLUMN_TIME = "TIME";
    private static final String DATABASE_NAME = "geek.db";
    private static final String TABLE_NAME = "table_password";
    private static final String ORDER_BY = COLUMN_PLATFORM;

    private static final int DATABASE_VERSION = 2;

    private static PasswordDBHelper sDBHelper;

    private PasswordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "_ID INTEGER NOT NULL PRIMARY KEY, " + "PLATFORM TEXT NOT NULL, "
                + "USERNAME TEXT NOT NULL, " + "PASSWORD TEXT NOT NULL, "
                + "CATEGORY  TEXT NOT NULL DEFAULT 默认, " + "NOTE TEXT, " + "STAR BOOLEAN, "
                + "TIME INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                upgradeToVersion2();
        }
    }

    private void upgradeToVersion2() {
        AppLog.e("upgradeToVersion2");
        Observable.create(new Observable.OnSubscribe<List<Password>>() {
            @Override
            public void call(Subscriber<? super List<Password>> subscriber) {
                List<Password> passwords = getAllPasswords(1);
                if (passwords.size() > 0) {
                    subscriber.onNext(passwords);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<Password>>() {
                    @Override
                    public void call(List<Password> passwords) {
                        for (Password password : passwords) {
                            insert(password);
                        }
                        for (Password password : passwords) {
                            deletePasswordById(password.getId());
                        }
                    }
                });
    }

    public long insert(String platform, String userName, String password, String note, int isStar) throws Exception {
        Password pwd = new Password();
        pwd.setPlatform(platform);
        pwd.setUserName(userName);
        pwd.setPassword(password);
        pwd.setNote(note);
        pwd.setStar(isStar);
        return insert(pwd);
    }

    public long insert(Password password) {
        ContentValues contentValues;
        try {
            contentValues = getContentValuesFromPassword(password);
            return getReadableDatabase().insert(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private ContentValues getContentValuesFromPassword(Password password) throws Exception {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLATFORM, password.getPlatform());
        contentValues.put(COLUMN_USERNAME, AESUtils.encrypt(password.getUserName()));
        contentValues.put(COLUMN_PASSWORD, AESUtils.encrypt(password.getPassword()));
//        contentValues.put(COLUMN_CATEGORY, password.getCategory());
        contentValues.put(COLUMN_NOTE, AESUtils.encrypt(password.getNote()));
        contentValues.put(COLUMN_STAR, password.isStar());
        contentValues.put(COLUMN_TIME, new Date().getTime());
        return contentValues;
    }

    private Password getPasswordByCurse(Cursor cursor) throws Exception {
        return getPasswordByCurse(cursor, DATABASE_VERSION);
    }

    private Password getPasswordByCurse(Cursor cursor, int databaseVersion) throws Exception {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        String platform = cursor.getString(cursor.getColumnIndex(COLUMN_PLATFORM));
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String pwd = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        String note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
        if (databaseVersion == 1) {
            userName = SecretUtil.decrypt(userName);
            pwd = SecretUtil.decrypt(pwd);
            note = SecretUtil.decrypt(note);
        } else if (databaseVersion == 2) {
            userName = AESUtils.decrypt(userName);
            pwd = AESUtils.decrypt(pwd);
            note = AESUtils.decrypt(note);
        }
        String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
        int star = cursor.getInt(cursor.getColumnIndex(COLUMN_STAR));
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
    public List<Password> getAllPasswords() {
        return getAllPasswords(DATABASE_VERSION);
    }

    public List<Password> getAllPasswords(int databaseVersion) {
        Cursor cursor = getWritableDatabase().query(TABLE_NAME, null, null, null, null, null,
                ORDER_BY);
        List<Password> passwords = new ArrayList<>();
        if (cursor == null) {
            return passwords;
        }
        while (cursor.moveToNext()) {
            try {
                Password password = getPasswordByCurse(cursor, databaseVersion);
                if (password != null)
                    passwords.add(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return passwords;
    }

    public Password getPasswordById(int id) throws Exception {
        final String selection = COLUMN_ID + "=?";
        final String[] args = new String[]{
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

    public List<Password> getPasswordByKeywords(String keywords) throws Exception {
        final String selection = COLUMN_PLATFORM + " LIKE ?";
        final String[] selectionArgs = new String[]{
                "%" + keywords + "%"
        };
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, selection, selectionArgs,
                null, null, null);
        List<Password> passwords = new ArrayList<>();
        if (cursor == null) {
            return passwords;
        }
        while (cursor.moveToNext()) {
            Password password = getPasswordByCurse(cursor);
            if (password != null)
                passwords.add(password);
        }
        cursor.close();
        return passwords;
    }

    public int updatePassword(Password password) throws Exception {
        ContentValues contentValues = getContentValuesFromPassword(password);
        return updatePasswordById(password.getId(), contentValues);
    }

    private int updatePasswordById(int id, ContentValues contentValues) {
        final String where = COLUMN_ID + "=?";
        final String[] args = new String[]{
                String.valueOf(id)
        };
        if (!contentValues.containsKey(COLUMN_TIME)) {
            contentValues.put(COLUMN_TIME, System.currentTimeMillis());
        }
        return getWritableDatabase().update(TABLE_NAME, contentValues, where, args);
    }

    public int deletePasswordById(int id) {
        final String where = COLUMN_ID + "=?";
        final String[] args = new String[]{
                String.valueOf(id)
        };
        return getWritableDatabase().delete(TABLE_NAME, where, args);
    }

    public int deleteAllPasswords() {
        return getWritableDatabase().delete(TABLE_NAME, null, null);
    }
}
