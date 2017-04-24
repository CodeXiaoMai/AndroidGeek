package com.xiaomai.geek.presenter.password;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.geek.view.IFileExploreView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoMai on 2017/4/12 11:33.
 */

public class FileExplorePresenter extends BaseRxPresenter<IFileExploreView> {

    public void scanStorage(Context context) {
        List<String> storages = new ArrayList<>();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            String[] paths = (String[]) storageManager.getClass().getMethod("getVolumePaths")
                    .invoke(storageManager);
            for (int i = 0; i < paths.length; i++) {
                String status = (String) storageManager.getClass()
                        .getMethod("getVolumeState", String.class).invoke(storageManager, paths[i]);
                if (Environment.MEDIA_MOUNTED.equals(status)) {
                    storages.add(paths[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMvpView().showSelectStorageView(storages);
    }
}
