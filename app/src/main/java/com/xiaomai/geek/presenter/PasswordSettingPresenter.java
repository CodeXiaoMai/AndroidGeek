
package com.xiaomai.geek.presenter;

import android.content.Context;
import android.os.Environment;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.view.IPasswordSettingView;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/4/1 18:19.
 */

public class PasswordSettingPresenter extends BaseRxPresenter<IPasswordSettingView> {

    public void deleteAllPasswords(final Context context) {
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                int count = PasswordDBHelper.getInstance(context).deleteAllPasswords();
                subscriber.onNext(count);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getMvpView().onDeleteAllPasswords(integer);
                    }
                }));
    }

    public void backupPasswords(final Context context) {
        Observable.create(new Observable.OnSubscribe<List<Password>>() {
            @Override
            public void call(Subscriber<? super List<Password>> subscriber) {
                List<Password> passwords = PasswordDBHelper.getInstance(context).getAllPasswords();
                subscriber.onNext(passwords);
                subscriber.onCompleted();
            }})
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        getMvpView().showBackupIng();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Func1<List<Password>, Integer>() {
                    @Override
                    public Integer call(List<Password> passwords) {
                        int count = 0;
                        StringWriter stringWriter = new StringWriter();
                        try {
                            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                            XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
                            xmlSerializer.setOutput(stringWriter);
                            xmlSerializer.startDocument("UTF-8", true);
                            xmlSerializer.startTag("", "password_list");
                            for (Password password : passwords) {
                                xmlSerializer.startTag("", "entry");

                                xmlSerializer.startTag("", "platform");
                                xmlSerializer.text(password.getPlatform());
                                xmlSerializer.endTag("", "platform");

                                xmlSerializer.startTag("", "username");
                                xmlSerializer.text(password.getUserName());
                                xmlSerializer.endTag("", "username");

                                xmlSerializer.startTag("", "password");
                                xmlSerializer.text(password.getPassword());
                                xmlSerializer.endTag("", "password");

                                xmlSerializer.startTag("", "note");
                                xmlSerializer.text(password.getNote());
                                xmlSerializer.endTag("", "note");

                                xmlSerializer.startTag("", "category");
                                xmlSerializer.text(password.getCategory());
                                xmlSerializer.endTag("", "category");

                                xmlSerializer.endTag("", "entry");
                                count ++;
                            }
                            xmlSerializer.endTag("", "password_list");
                            xmlSerializer.endDocument();

                            FileOutputStream fileOutputStream = new FileOutputStream(
                                    Environment.getExternalStorageDirectory() + "/"
                                            + context.getPackageName() + "/back_up.xml");
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                                    fileOutputStream);
                            outputStreamWriter.write(stringWriter.toString());
                            outputStreamWriter.close();
                            fileOutputStream.close();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return count;
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getMvpView().onBackupComplete(integer);
                    }
                });
    }
}
