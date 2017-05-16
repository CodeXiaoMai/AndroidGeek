package com.xiaomai.geek.ui.module.articel;

import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Chapter;

import java.util.List;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ChapterListAdapter extends BaseQuickAdapter<Chapter> {

    private final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

    public ChapterListAdapter(List<Chapter> data) {
        super(R.layout.item_chapter, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Chapter chapter) {
        TextView tvName = holder.getView(R.id.tv_name);
        String articleName = chapter.getName();
        tvName.setBackgroundColor(colorGenerator.getColor(articleName));
        tvName.setText(articleName);
    }
}
