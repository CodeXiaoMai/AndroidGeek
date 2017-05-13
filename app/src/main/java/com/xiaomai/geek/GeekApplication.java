
package com.xiaomai.geek;

import android.content.Context;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.di.component.ApplicationComponent;
import com.xiaomai.geek.di.component.DaggerApplicationComponent;
import com.xiaomai.geek.di.module.ApplicationModule;
import com.xiaomai.geek.service.InitializeService;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

/**
 * Created by XiaoMai on 2017/3/29 17:30.
 */

public class GeekApplication extends TinkerApplication {

    public GeekApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.xiaomai.geek.GeekApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.init();
        InitializeService.start(getApplication().getApplicationContext());
    }

    public static GeekApplication get(Context context) {
        return (GeekApplication) context.getApplicationContext();
    }

    ApplicationComponent mApplicationComponent;

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this)).build();
        }
        return mApplicationComponent;
    }
}
