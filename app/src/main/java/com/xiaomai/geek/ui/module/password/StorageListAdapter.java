
package com.xiaomai.geek.ui.module.password;

import android.os.Environment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.FileUtils;

import java.util.List;

/**
 * Created by XiaoMai on 2017/4/12 14:01.
 */

public class StorageListAdapter extends BaseQuickAdapter<String> {
    public StorageListAdapter(List<String> data) {
        super(R.layout.item_storage, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String filePath) {
        if (filePath.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            holder.setText(R.id.tv_storage_name, "手机存储");
        } else {
            holder.setText(R.id.tv_storage_name, "SD卡");
        }
        holder.setText(R.id.tv_available_space,
                FileUtils.formatSize(FileUtils.getDirAvailableSize(filePath)));
    }
}
