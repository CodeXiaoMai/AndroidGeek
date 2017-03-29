
package com.xiaomai.geek.module.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.api.bean.NewsInfo;
import com.xiaomai.geek.common.util.ListUtils;
import com.xiaomai.geek.common.util.NewsUtils;
import com.xiaomai.geek.common.wrapper.ImageLoader;

import java.util.List;

import static com.xiaomai.geek.api.bean.NewsInfo.ITEM_TYPE_NORMAL;
import static com.xiaomai.geek.api.bean.NewsInfo.ITEM_TYPE_PHOTO_SET;

/**
 * Created by XiaoMai on 2017/3/28 11:30.
 */

public class NewsListAdapter extends BaseMultiItemQuickAdapter<NewsInfo> {

    public NewsListAdapter(List<NewsInfo> data) {
        super(data);
        addItemType(NewsInfo.ITEM_TYPE_NORMAL, R.layout.item_news);
        addItemType(ITEM_TYPE_PHOTO_SET, R.layout.item_news_photo_set);
    }

    @Override
    public int getItemViewType(int position) {
        NewsInfo item = (NewsInfo) getItem(position);
        if (NewsUtils.isNewsPhotoSet(item.getSkipType())) {
            return ITEM_TYPE_PHOTO_SET;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsInfo item) {
        if (NewsUtils.isNewsPhotoSet(item.getSkipType())) {
            ImageView[] newsPhoto = new ImageView[3];
            newsPhoto[0] = holder.getView(R.id.iv_icon_1);
            newsPhoto[1] = holder.getView(R.id.iv_icon_2);
            newsPhoto[2] = holder.getView(R.id.iv_icon_3);
            holder.setVisible(R.id.iv_icon_2, false);
            holder.setVisible(R.id.iv_icon_3, false);
            ImageLoader.load(item.getImgsrc(), newsPhoto[0]);
            if (!ListUtils.isEmpty(item.getImgextra())) {
                for (int i = 0; i < Math.min(2, item.getImgextra().size()); i++) {
                    newsPhoto[i + 1].setVisibility(View.VISIBLE);
                    ImageLoader.load(item.getImgextra().get(i).getImgsrc(), newsPhoto[i + 1]);
                }
            }
        } else {
            holder.setText(R.id.tv_title, item.getTitle());
            holder.setText(R.id.tv_source, item.getSource());
            holder.setText(R.id.tv_time, item.getPtime());
            ImageLoader.load(item.getImgsrc(), (ImageView) holder.getView(R.id.iv_icon));
        }
    }
}
