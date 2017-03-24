
package com.xiaomai.geek.common.wrapper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaomai.geek.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by XiaoMai on 2017/3/9 16:01. 图片加载库封装 封装一个ImageLoader工具类来对外提供接口加载图片
 */

public class ImageLoader {

    public static void load(Context context, Object source, ImageView imageView) {
        Glide.with(context)
                .load(source)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public static void load(Object source, ImageView imageView) {
        load(imageView.getContext(), source, imageView);
    }

    public static void loadWithCircle(Context context, Object source, ImageView imageView) {
        Glide.with(context)
                .load(source)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public static void loadWithCircle(Object source, ImageView imageView) {
        loadWithCircle(imageView.getContext(), source, imageView);
    }
}
