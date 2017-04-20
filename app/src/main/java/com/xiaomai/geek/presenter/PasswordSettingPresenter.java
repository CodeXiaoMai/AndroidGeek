
package com.xiaomai.geek.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.xiaomai.geek.common.utils.FileUtils;
import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.view.IPasswordSettingView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
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

    public void backupPasswords(final Context context, final String path) {
        Observable.create(new Observable.OnSubscribe<List<Password>>() {
            @Override
            public void call(Subscriber<? super List<Password>> subscriber) {
                try {
                    List<Password> passwords = PasswordDBHelper.getInstance(context).getAllPasswords();
                    subscriber.onNext(passwords);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                getMvpView().showBackupIng();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io())
                .map(new Func1<List<Password>, Integer>() {
                    @Override
                    public Integer call(List<Password> passwords) {
                        if (passwords.size() == 0) {
                            return 0;
                        }
                        int count = 0;
                        StringWriter stringWriter = new StringWriter();
                        try {
                            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
                                    .newInstance();
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
                                count++;
                            }
                            xmlSerializer.endTag("", "password_list");
                            xmlSerializer.endDocument();

                            FileUtils.checkDirs(new File(path).getParent());
                            FileOutputStream fileOutputStream = new FileOutputStream(path);
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                                    fileOutputStream);
                            outputStreamWriter.write(stringWriter.toString());
                            outputStreamWriter.close();
                            fileOutputStream.close();
                            return count;
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                            return -1;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            return -2;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return -3;
                        }
                    }

                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getMvpView().onBackupComplete(integer);
                    }
                });
    }

    public void importPassword(final Context context, final String filePath) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                BufferedReader bufferedReader = null;
                InputStreamReader streamReader = null;
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(filePath);
                    streamReader = new InputStreamReader(fileInputStream);
                    bufferedReader = new BufferedReader(streamReader);
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    streamReader.close();
                    fileInputStream.close();
                    subscriber.onNext(stringBuilder.toString());
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable("读取文件错误"));
                } finally {
                    try {
                        if (bufferedReader != null)
                            bufferedReader.close();
                        if (streamReader != null)
                            streamReader.close();
                        if (fileInputStream != null)
                            fileInputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).map(new Func1<String, List<Password>>() {
            @Override
            public List<Password> call(String source) {
                StringReader stringReader = new StringReader(source);
                List<Password> passwordList = new ArrayList<>();
                Password password = null;
                try {
                    XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                    xmlPullParser.setInput(stringReader);
                    int event = xmlPullParser.getEventType();
                    while (event != XmlPullParser.END_DOCUMENT) {
                        String nodeName = xmlPullParser.getName();
                        switch (event) {
                            case XmlPullParser.START_TAG:
                                if (TextUtils.equals("entry", nodeName)) {
                                    password = new Password();
                                } else if (TextUtils.equals("platform", nodeName)) {
                                    password.setPlatform(xmlPullParser.nextText());
                                } else if (TextUtils.equals("username", nodeName)) {
                                    password.setUserName(xmlPullParser.nextText());
                                } else if (TextUtils.equals("password", nodeName)) {
                                    password.setPassword(xmlPullParser.nextText());
                                } else if (TextUtils.equals("note", nodeName)) {
                                    password.setNote(xmlPullParser.nextText());
                                } else if (TextUtils.equals("category", nodeName)) {
                                    password.setCategory(xmlPullParser.nextText());
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if (TextUtils.equals("entry", nodeName) && password != null) {
                                    passwordList.add(password);
                                    password = null;
                                }
                                break;
                            default:
                                break;
                        }
                        event = xmlPullParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return passwordList;
            }
        }).map(new Func1<List<Password>, Integer>() {
            @Override
            public Integer call(List<Password> passwords) {
                int count = 0;
                for (Password password : passwords) {
                    long insert = PasswordDBHelper.getInstance(context).insert(password);
                    if (insert > 0) {
                        count++;
                    }
                }
                return count;
            }
        })

                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getMvpView().importComplete(integer);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getMvpView().importFail(throwable.getMessage());
                    }
                });
    }
}
