package com.xiaomai.geek.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by xiaomai on 2017/10/25.
 */

public class PasswordRepository implements IPasswordDataSource {

    private static PasswordRepository INSTANCE;

    public static PasswordRepository getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (PasswordRepository.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PasswordRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        PasswordDBHelper.destroyInstance();
        INSTANCE = null;
    }

    @NonNull
    private PasswordDBHelper mPasswordDBHelper;

    private PasswordRepository(@NonNull Context context) {
        mPasswordDBHelper = PasswordDBHelper.getInstance(context);
    }

    @Override
    public Flowable<List<Password>> getPasswords() {
        Cursor cursor = mPasswordDBHelper.getWritableDatabase().query(
                PasswordDBHelper.PasswordEntry.TABLE_NAME, null, null, null, null, null, PasswordDBHelper.PasswordEntry.COLUMN_NAME_PLATFORM);
        final List<Password> passwordList = getPasswords(cursor);
        cursor.close();

        return Flowable.create(new FlowableOnSubscribe<List<Password>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<List<Password>> e) throws Exception {
                e.onNext(passwordList);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Observable<List<Password>> getPasswords(@NonNull String keyword) {
        String[] columns = {
                PasswordDBHelper.PasswordEntry._ID,
                PasswordDBHelper.PasswordEntry.COLUMN_NAME_PLATFORM,
                PasswordDBHelper.PasswordEntry.COLUMN_NAME_USERNAME,
                PasswordDBHelper.PasswordEntry.COLUMN_NAME_PASSWORD,
                PasswordDBHelper.PasswordEntry.COLUMN_NAME_NOTE
        };
        String selection = String.format("%s LIKE ?",
                PasswordDBHelper.PasswordEntry.COLUMN_NAME_PLATFORM);
        String[] selectionArgs = {String.format("%s%s%s", "%", keyword, "%")};
        Cursor cursor = mPasswordDBHelper.getReadableDatabase().query(
                PasswordDBHelper.PasswordEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );
        final List<Password> passwordList = getPasswords(cursor);
        cursor.close();
        return Observable.create(new ObservableOnSubscribe<List<Password>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<Password>> e) throws Exception {
                e.onNext(passwordList);
                e.onComplete();
            }
        });
    }

    private List<Password> getPasswords(@NonNull Cursor cursor) {
        List<Password> passwordList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(PasswordDBHelper.PasswordEntry._ID));
            String platform = cursor.getString(cursor.getColumnIndex(PasswordDBHelper.PasswordEntry.COLUMN_NAME_PLATFORM));
            String userName = cursor.getString(cursor.getColumnIndex(PasswordDBHelper.PasswordEntry.COLUMN_NAME_USERNAME));
            String pwd = cursor.getString(cursor.getColumnIndex(PasswordDBHelper.PasswordEntry.COLUMN_NAME_PASSWORD));
            String note = cursor.getString(cursor.getColumnIndex(PasswordDBHelper.PasswordEntry.COLUMN_NAME_NOTE));
            Password password = new Password(id, platform, userName, pwd, note);
            passwordList.add(password);
        }
        return passwordList;
    }

    @Override
    public Observable<Boolean> savePassword(@NonNull Password password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PasswordDBHelper.PasswordEntry._ID, password.getId());
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_PLATFORM, password.getPlatform());
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_USERNAME, password.getUserName());
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_PASSWORD, password.getPassword());
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_NOTE, password.getNote());
        long insert = mPasswordDBHelper.getWritableDatabase().insert(PasswordDBHelper.PasswordEntry.TABLE_NAME, null, contentValues);
        return Observable.just(insert > 0);
    }

    @Override
    public Observable<Boolean> updatePassword(@NonNull Password password) {
        String whereClause = String.format("%s = ?", PasswordDBHelper.PasswordEntry._ID);
        ContentValues contentValues = new ContentValues();
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_PLATFORM, password.getPlatform());
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_USERNAME, password.getUserName());
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_PASSWORD, password.getPassword());
        contentValues.put(PasswordDBHelper.PasswordEntry.COLUMN_NAME_NOTE, password.getNote());
        String[] whereArgs = {password.getId()};
        long update = mPasswordDBHelper.getWritableDatabase().update(PasswordDBHelper.PasswordEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
        return Observable.just(update > 0);
    }

    @Override
    public Observable<Boolean> deletePassword(@NonNull String passwordId) {
        String whereClause = String.format("%s = ?", PasswordDBHelper.PasswordEntry._ID);
        String[] whereArgs = {passwordId};
        int delete = mPasswordDBHelper.getWritableDatabase().delete(PasswordDBHelper.PasswordEntry.TABLE_NAME, whereClause, whereArgs);
        return Observable.just(delete > 0);
    }
}
