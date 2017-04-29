package com.xiaomai.geek.data.api;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.module.User;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by XiaoMai on 2017/4/21.
 */

public interface GitHubApi {

    String LANG_ANDROID = "android";

    String LANG_JAVA = "java";

    String LANG_OC = "objective-c";

    String LANG_SWIFT = "swift";

    String LANG_HTML = "html";

    String LANG_PYTHON = "python";

    String LANG_BASH = "shell";

    String LANG_PHP = "php";

    Observable<ArrayList<Repo>> getTrendingRepos(@LanguageType String language);

    Observable<User> getSingleUser(String name);

    Observable<ArrayList<Repo>> getMyRepos();

    Observable<ArrayList<Repo>> getUserRepos(String userName);

    Observable<ArrayList<Repo>> getMyStarredRepos();

    Observable<ArrayList<Repo>> getUserStarredRepos(String userName);

    Observable<ArrayList<User>> getMyFollowers();

    Observable<ArrayList<User>> getUserFollowers(String userName);

    Observable<ArrayList<User>> getMyFollowing();

    Observable<ArrayList<User>> getUserFollowing(String userName);


    @StringDef({
            LANG_ANDROID, LANG_JAVA, LANG_OC, LANG_SWIFT, LANG_HTML, LANG_PYTHON, LANG_BASH
    })
    @interface LanguageType {
    }

    int OWNER_REPO = 1;

    int STARRED_REPO = 2;

    @IntDef({
            OWNER_REPO, STARRED_REPO
    })
    @interface RepoType {
    }
}
